package org.hejwo.ecommerce

import cats.effect.unsafe.implicits.global
import cats.effect.{IO, Resource}
import fs2.grpc.syntax.all._
import io.grpc.{Server, ServerServiceDefinition}
import org.hejwo.ecommerce.base.application.IOLogging
import org.hejwo.ecommerce.base.infrastructure.{NettyBuilder, ThreadUtils}
import org.hejwo.ecommerce.products.config.ProductsModuleConfig
import pureconfig.generic.auto._
import fs2._
import org.hejwo.ecommerce.Main.serverRunner

import scala.concurrent.duration._
import java.lang.management.ThreadInfo

object Main extends MainModule with IOLogging {

  override def productsModuleConfig: ProductsModuleConfig = appConfig.productsModule

  def main(args: Array[String]): Unit =
    (for {
//      _ <- metricsServerResource
      _ <- productServiceGrpcStream.evalMap(productServiceDefinition => serverRunner(productServiceDefinition))
    } yield ()).compile.drain
      .unsafeRunSync()

  def serverRunner(service: ServerServiceDefinition*): IO[Nothing] = {
    def startServer(server: Server): IO[Server] = logger.info(s"Starting gRPC server on port ${appConfig.grpc.port}") >> IO(server.start())

    (for {
      threadsAtStart <- Resource.eval(ThreadUtils.printThreads("threads at beginning"))
      _ <- printCurrentThreadsAtFixedRate(threadsAtStart)
      server <- NettyBuilder(appConfig.grpc.port)(service).resource[IO].evalMap(startServer)
    } yield server).useForever
  }

  private def printCurrentThreadsAtFixedRate(initialThreads: List[ThreadInfo]) =
    Stream
      .fixedRate[IO](8.seconds)
      .evalMap(_ => ThreadUtils.printThreads("ppp").map(ThreadUtils.diff(initialThreads, _)))
      .compile
      .drain
      .background

}
