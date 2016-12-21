organization := "com.lightbend.lagom"

name := "lagom-service-locator-consul"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.8"

val lagomVersion = "1.2.0"

libraryDependencies ++= Seq(
  "com.lightbend.lagom" %% "lagom-javadsl-api" % lagomVersion,
  "com.ecwid.consul"     % "consul-api"        % "1.1.11",
  "org.scalatest"       %% "scalatest"         % "2.2.4" % Test
)
