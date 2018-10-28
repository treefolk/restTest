
name := "RESTSimple"

version := "0.1"

scalaVersion := "2.12.7"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= {
  Seq(
    //"org.scalatest" %% "scalatest" % "3.2.0-SNAP10" % Test,
    //"org.scalatestplus.play" %% "scalatestplus-play" % "4.0.0-M5" % Test,
    specs2 % Test,
      guice
  )
}