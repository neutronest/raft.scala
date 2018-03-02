import Dependencies._

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.3",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "Hello",
      libraryDependencies += scalaTest % Test,
    // required for the RPC Server:

  )
