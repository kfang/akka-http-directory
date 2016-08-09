name := "akka-http-directory"

version := "0.0.1"

scalaVersion := "2.11.8"


scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers += "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

val akkaVersion = "2.4.8"
libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "joda-time"      % "joda-time"       % "2.9.4",

  "com.typesafe.akka" %% "akka-http-core"         % akkaVersion,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j"             % akkaVersion,
  "org.reactivemongo" %% "reactivemongo"          % "0.11.14",
  "com.github.t3hnar" %% "scala-bcrypt"           % "2.6"
)

