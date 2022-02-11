package org.hejwo.ecommerce
import cats.effect.{IO, Resource}
import fs2.grpc.syntax.all._
import fs2._
import io.grpc.{ManagedChannel, ManagedChannelBuilder, Metadata}
import io.grpc.netty.NettyChannelBuilder
import org.hejwo.ecommerce.productService.ProductServiceFs2Grpc
import org.hejwo.ecommerce.productServiceDto.ProductsSubscriber
import cats.effect.unsafe.implicits.global
import org.hejwo.ecommerce.base.application.IOLogging
import org.hejwo.ecommerce.base.infrastructure.ThreadUtils

import scala.concurrent.duration._
import java.lang.management.ThreadInfo
import java.util.concurrent.Executors

object ProductClientRunner extends IOLogging {

//  https://github.com/typelevel/fs2-grpc/#creating-a-client
  lazy val qqq = Executors.newFixedThreadPool(2)

  lazy val managedChannelResource: Resource[IO, ManagedChannel] = NettyChannelBuilder
    .forTarget("localhost:8088")
    .usePlaintext()
    .resource[IO]

  def main(args: Array[String]): Unit = {

    val ddd =
      ThreadUtils.printThreads("threads at beginning").flatTap(l => IO(l.sortBy(_.getThreadName).foreach(println))) >>
        managedChannelResource
          .flatMap(ch => ProductServiceFs2Grpc.stubResource[IO](ch))
          .use { service =>
            for {
              threadsBeforeSubscribe <- ThreadUtils.printThreads("threadsBeforeSubscribe")
              printingIO <- service
                .subscribeToProducts(ProductsSubscriber("aaa", 45), new Metadata())
                .take(5)
                .evalMap(prod => logger.info(s"!!!!!! $prod"))
                .compile
                .drain
              threadsAtEnd <- printCurrentThreads(threadsBeforeSubscribe, "threads after stream start")
            } yield printingIO
          }
    ddd.unsafeRunSync()
  }

  private def printCurrentThreads(initialThreads: List[ThreadInfo], name: String) =
    ThreadUtils.printThreads(name).map(ThreadUtils.diff(initialThreads, _))

}
