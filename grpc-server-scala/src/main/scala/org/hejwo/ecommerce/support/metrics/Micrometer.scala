//package org.hejwo.ecommerce.support.metrics
//
//import cats.effect.unsafe.implicits.global
//import cats.effect.{IO, Ref, Resource}
//import io.micrometer.core.instrument.{ImmutableTag, MeterRegistry, Tag}
//import org.hejwo.ecommerce.base.application.IOLogging
//import org.http4s.metrics.{MetricsOps, TerminationType}
//import org.http4s.{Method, Status}
//
//import java.util.concurrent.TimeUnit
//import scala.jdk.CollectionConverters._
//
//private[metrics] object Micrometer extends IOLogging {
//
//  val slowRequestThresholdMillis = 100
//
//  def metricsOps(meterRegistry: MeterRegistry): Resource[IO, MetricsOps[IO]] = {
//    meterRegistry.config().commonTags(List(new ImmutableTag("application", "grpc-server"): Tag).asJava)
//    Resource.pure[IO, MetricsOps[IO]](prepareMetrics(meterRegistry))
//  }
//
//  private def prepareMetrics(meterRegistry: MeterRegistry) = new MetricsOps[IO] {
//    private val prefix = "http.server"
//    private val failureTime = meterRegistry.timer(s"$prefix.failure-time")
//    private val activeRequests: Ref[IO, Long] = Ref.unsafe[IO, Long](0L)
//
//    meterRegistry.gauge(
//      s"$prefix.active-requests",
//      activeRequests,
//      (_: Ref[IO, Long]) => activeRequests.get.unsafeRunSync().toDouble
//    )
//
//    override def increaseActiveRequests(classifier: Option[String]): IO[Unit] = activeRequests.update(_ + 1)
//
//    override def decreaseActiveRequests(classifier: Option[String]): IO[Unit] = activeRequests.update(_ - 1)
//
//    override def recordHeadersTime(method: Method, elapsed: Long, classifier: Option[String]): IO[Unit] =
//      IO(meterRegistry.timer(s"$prefix.headers-time", "method", method.name).record(elapsed, TimeUnit.NANOSECONDS))
//
//    override def recordTotalTime(method: Method, status: Status, elapsed: Long, classifier: Option[String]): IO[Unit] = {
//      val requestLabel = classifier.getOrElse(s"$method $status")
//      val elapsedTimeMillis = elapsed / 1000 / 1000
//      val logRequestTime = if (elapsedTimeMillis > slowRequestThresholdMillis) {
//        logger.info(s"Slow request $requestLabel took $elapsedTimeMillis ms")
//      } else {
//        logger.trace(s"Request $requestLabel took $elapsedTimeMillis ms")
//      }
//      logRequestTime *>
//        IO(
//          meterRegistry
//            .timer(s"$prefix.requests", "status", s"${status.code}", "status-class", s"${status.code / 100}xx")
//            .record(elapsed, TimeUnit.NANOSECONDS)
//        )
//    }
//
//    override def recordAbnormalTermination(elapsed: Long, terminationType: TerminationType, classifier: Option[String]): IO[Unit] =
//      IO(failureTime.record(elapsed, TimeUnit.NANOSECONDS))
//  }
//}
