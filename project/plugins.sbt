resolvers += Resolver.jcenterRepo

addSbtPlugin("com.softwaremill.sbt-softwaremill" % "sbt-softwaremill-common" % "2.0.8")
addSbtPlugin("com.softwaremill.sbt-softwaremill" % "sbt-softwaremill-extra" % "2.0.8")

addSbtPlugin("org.typelevel" % "sbt-fs2-grpc" % "2.4.3")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.8.1")

addDependencyTreePlugin
