
name := "RESTSimple"

version := "0.1"

scalaVersion := "2.12.7"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= {
  Seq(
    specs2 % Test,
    guice
  )
}

fork in Test := true
javaOptions in Test += "-Dconfig.file=test/resources/application.test.conf"
