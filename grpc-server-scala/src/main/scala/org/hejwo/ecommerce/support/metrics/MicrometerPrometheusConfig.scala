//package org.hejwo.ecommerce.support.metrics
//
//import io.micrometer.prometheus.{HistogramFlavor, PrometheusConfig}
//
//import java.util.concurrent.TimeUnit
//import scala.concurrent.duration.Duration
//
//private[metrics] final case class MicrometerPrometheusConfig(
//    _step: Duration = Duration(10, TimeUnit.SECONDS),
//    _prefix: String = "",
//    _descriptions: Boolean = true,
//    _histogramFlavor: HistogramFlavor = HistogramFlavor.Prometheus
//) extends PrometheusConfig {
//
//  override val step: java.time.Duration = java.time.Duration.ofMillis(_step.toMillis)
//  override val prefix: String = _prefix
//  override val descriptions: Boolean = _descriptions
//  override val histogramFlavor: HistogramFlavor = _histogramFlavor
//
//  // the method is @Nullable and we don't need to implement it here
//  @SuppressWarnings(Array("scalafix:DisableSyntax.null"))
//  override def get(key: String): String = null
//}
