//package org.hejwo.ecommerce.support.metrics
//
//import cats.effect.IO
//import cats.effect.kernel.Resource
//import io.micrometer.core.instrument.Clock
//import io.micrometer.core.instrument.binder.jvm.{ClassLoaderMetrics, JvmGcMetrics, JvmMemoryMetrics, JvmThreadMetrics}
//import io.micrometer.core.instrument.binder.system.ProcessorMetrics
//import io.micrometer.prometheus.PrometheusMeterRegistry
//import io.prometheus.client.CollectorRegistry
//import fs2._
//import org.hejwo.ecommerce.base.api.RequestClassifier
//import org.http4s.blaze.server.BlazeServerBuilder
//import org.http4s.metrics.MetricsOps
//import org.http4s.server.middleware.Metrics
//import org.http4s.server.{Router, Server}
//import sttp.tapir.server.ServerEndpoint.Full
//import sttp.tapir.server.http4s.Http4sServerInterpreter
//
//import java.util.concurrent.Executors
//import scala.concurrent.ExecutionContext
//
//trait MetricsModule {
//
//  private lazy val collector: CollectorRegistry = CollectorRegistry.defaultRegistry
//  private lazy val registry = new PrometheusMeterRegistry(MicrometerPrometheusConfig(), collector, Clock.SYSTEM)
//
//  private lazy val metricsOpsResource: Resource[IO, MetricsOps[IO]] = Micrometer.metricsOps(registry)
//  private lazy val metricsRoute: Full[Unit, Unit, Unit, Unit, String, Any, IO] = new MetricsRoutes(collector).metricsRoute
//  private lazy val metricsRoutes = Http4sServerInterpreter[IO]().toRoutes(metricsRoute)
//
//  private def initMetricsModule(): Unit = {
//    new ClassLoaderMetrics().bindTo(registry)
//    new JvmMemoryMetrics().bindTo(registry)
//    new JvmGcMetrics().bindTo(registry)
//    new ProcessorMetrics().bindTo(registry)
//    new JvmThreadMetrics().bindTo(registry)
//  }
//
//  private val executor: ExecutionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(1))
//
//  lazy val metricsServerResource: Stream[IO, Server] = Stream.resource(
//    metricsOpsResource
//      .map(metricsOps => Metrics[IO](ops = metricsOps, classifierF = RequestClassifier.requestLabel)(metricsRoutes))
//      .flatMap { _ =>
//        initMetricsModule()
//        BlazeServerBuilder[IO]
//          .withExecutionContext(executor)
//          .bindHttp(8089, "0.0.0.0")
//          .withHttpApp(Router("" -> (metricsRoutes)).orNotFound)
//          .resource
//      }
//  )
//
//}
