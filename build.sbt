name := "akka-http-directory"

version := "0.0.1"

scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers += "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.7",

  "com.typesafe.akka" %% "akka-http-core"         % "2.4.8",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.8",
  "com.typesafe.akka" %% "akka-slf4j"             % "2.4.8",
  "org.reactivemongo" %% "reactivemongo"          % "0.11.14"
)

