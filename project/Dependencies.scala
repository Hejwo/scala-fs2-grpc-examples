import sbt._

object Dependencies {

  def grpcDependencies(version: String) = List(
    "io.grpc" % "grpc-netty" % version,
    "io.grpc" % "grpc-services" % version
  )

  def scalaCache(version: String) = List(
    "com.github.cb372" %% "scalacache-core" % version,
    "com.github.cb372" %% "scalacache-caffeine" % version
  )

  lazy val loggingDependencies = Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
    "ch.qos.logback" % "logback-classic" % "1.2.7",
    "org.typelevel" %% "log4cats-slf4j" % "2.1.1"
  )

  def tapirDependencies(tapirVersion: String, sttpVersion: String) = Seq(
    "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-sttp-client" % tapirVersion,
    "com.softwaremill.sttp.client3" %% "circe" % sttpVersion
  )

  def http4sDependencies(http4sVersion: String) = Seq(
    "org.http4s" %% "http4s-dsl" % http4sVersion,
    "org.http4s" %% "http4s-blaze-server" % http4sVersion,
    "org.http4s" %% "http4s-blaze-client" % http4sVersion,
    "org.http4s" %% "http4s-circe" % http4sVersion,
    "org.http4s" %% "http4s-prometheus-metrics" % http4sVersion
  )

  lazy val tapirVersion = "0.20.0-M3"
  lazy val sttpVersion = "3.3.16"
  lazy val http4sVersion = "0.23.9"

//  lazy val metricsDependencies =
//    tapirDependencies("0.20.0-M3", "3.3.16") ++
//      http4sDependencies("0.23.6") ++
//      Seq("io.micrometer" % "micrometer-registry-prometheus" % "1.8.0"
//    https://micrometer.io/docs/registry/prometheus
  // https://www.stackhawk.com/blog/prometheus-metrics-with-springboot-and-grpc-services/

}

//https://fs2.io/#/io?id=tcp
//https://blog.jdriven.com/2018/11/transcoding-grpc-to-http-json-using-envoy/
