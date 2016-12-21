import sbt._

name in ThisBuild := "lagom-scala-helloservice"

organization in ThisBuild := "sample.lagom.scala"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.11.8"

scalacOptions in ThisBuild := Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")

lazy val helloServiceApi = project("helloservice-api")
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies += lagomJavadslApi
  )

lazy val helloServiceImpl = project("helloservice-impl")
  .enablePlugins(LagomJava)
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      //lagomJavadslPersistenceCassandra,
      lagomJavadslTestKit,
      // Lagom Consul Service Locator
      "com.lightbend.lagom" %% "lagom-service-locator-consul" % "1.0.0-SNAPSHOT"
      // Swagger
      //"io.swagger" %% "swagger-play2" % "1.5.2"
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(helloServiceApi, utils)


lazy val greetingServiceApi = project("greetingservice-api")
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies += lagomJavadslApi
  )

lazy val greetingServiceImpl = project("greetingservice-impl")
  .enablePlugins(LagomJava)
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      //lagomJavadslPersistenceCassandra,
      lagomJavadslTestKit,
      // Lagom Consul Service Locator
      "com.lightbend.lagom" %% "lagom-service-locator-consul" % "1.0.0-SNAPSHOT"
      // Swagger
      //"io.swagger" %% "swagger-play2" % "1.5.2"
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(greetingServiceApi, helloServiceApi, utils)

lazy val utils = project("utils")
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies += lagomJavadslApi,
    libraryDependencies += lagomJavadslServer
  )

def project(id: String) = Project(id, base = file(id))
  .settings(
    scalacOptions in Compile += "-Xexperimental" // this enables Scala lambdas to be passed as Java SAMs
  )
  .settings(
    libraryDependencies ++= Seq(
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.7.5" // actually, only api projects need this
    )
  )

// disable persistence (Cassandra)
lagomCassandraEnabled in ThisBuild := false
// do not delete database files on start
lagomCassandraCleanOnStart in ThisBuild := false
// diable message broker (Kafka)
lagomKafkaEnabled in ThisBuild := false

// See https://github.com/FasterXML/jackson-module-parameter-names
lazy val jacksonParameterNamesJavacSettings = Seq(
  javacOptions in compile += "-parameters"
)
