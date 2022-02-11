import com.softwaremill.SbtSoftwareMillCommon.commonSmlBuildSettings
import Dependencies._
import com.typesafe.sbt.packager.docker.DockerApiVersion

//====================================================================================================================
lazy val `grpc-server-protobuf` = project.in(file("grpc-server-protobuf")).settings(commonSettings).enablePlugins(Fs2Grpc)

//====================================================================================================================
//=== gRPC server scala ==============================================================================================
lazy val `grpc-server-scala` =
  project
    .in(file("grpc-server-scala"))
    .enablePlugins(DockerPlugin, JavaAppPackaging)
    .settings(commonSettings)
    .settings(
      Compile / mainClass := Some("org.hejwo.ecommerce.Main"),
      libraryDependencies ++= serverProductionDependencies
    )
    .settings(dockerSettings)
    .dependsOn(`grpc-server-protobuf`)

val serverProductionDependencies = grpcDependencies("1.43.0") ++
  scalaCache("1.0.0-M6") ++
  loggingDependencies ++ List(
  "com.github.pureconfig" %% "pureconfig" % "0.17.0",
  "org.scalacheck" %% "scalacheck" % "1.15.4"
)

lazy val dockerSettings = Seq(
  dockerExposedPorts += 8088,
  dockerBaseImage := "adoptopenjdk:11.0.5_10-jdk-hotspot",
  dockerApiVersion := Some(DockerApiVersion(1, 40)),
  Universal / javaOptions ++= Seq(
    // -J params will be added as jvm parameters
    "-J-XX:MaxRAMPercentage=70.0"
  ),
  dockerUpdateLatest := true
)

//====================================================================================================================
//=== gRPC client scala ==============================================================================================
lazy val `grpc-client-scala` =
  project
    .in(file("grpc-client-scala"))
    .settings(commonSettings)
    .settings(
      libraryDependencies ++= grpcDependencies("1.43.0") ++ loggingDependencies
    )
    .dependsOn(`grpc-server-protobuf`)

//====================================================================================================================
//=== Whole project ==================================================================================================
lazy val `scala-fs2-grpc-examples` = (project in file(".")).aggregate(`grpc-server-protobuf`, `grpc-server-scala`, `grpc-client-scala`)

//====================================================================================================================
//=== Commons ========================================================================================================

val commonSettings = commonSmlBuildSettings ++
  Seq(
    scalaVersion := "2.13.7",
    organization := "org.hejwo",
    Test / parallelExecution := false,
    Compile / scalacOptions ++= Seq(
      "-Dcats.effect.stackTracingMode=full",
      "-Dcats.effect.traceBufferLogSize=5",
      "-Wconf:cat=other-match-analysis:error",
      "-Wconf:cat=deprecation:error"
    )
  )

//lazy val catsVersion = "3.3.4"
