//package org.hejwo.ecommerce.support.metrics
//
//import cats.effect.IO
//import cats.implicits.catsSyntaxEitherId
//import io.prometheus.client.CollectorRegistry
//import io.prometheus.client.exporter.common.TextFormat
//import sttp.tapir.server.ServerEndpoint.Full
//import sttp.tapir.{endpoint, stringBody}
//
//import java.io.StringWriter
//
//class MetricsRoutes(collector: CollectorRegistry) {
//
//  lazy val metricsRoute: Full[Unit, Unit, Unit, Unit, String, Any, IO] = endpoint.get
//    .in("metrics")
//    .out(stringBody)
//    .serverLogic { _ =>
//      val value: IO[String] = IO {
//        val writer = new StringWriter
//        TextFormat.write004(writer, collector.metricFamilySamples())
//        writer.toString
//      }
//      value.map(_.asRight[Unit])
//    }
//}
