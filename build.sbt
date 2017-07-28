name := "shapeless-applied"
organization := "io.github.jeremyrsmith"
version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.11"

crossScalaVersions := Seq(
  "2.11.11", "2.12.2"
)

libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "2.3.2",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)