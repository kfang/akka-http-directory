name := "spray-directory"

version := "1.0"

scalaVersion := "2.10.4"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers += "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
      "io.spray" % "spray-can" % "1.3-M1",
      "io.spray" % "spray-routing" % "1.3-M1",
      "com.typesafe.akka" %% "akka-actor" % "2.2.4",
      "com.zipfworks" %% "sprongo" % "1.1.2-SNAPSHOT"
)

