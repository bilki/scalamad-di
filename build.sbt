import Dependencies._

lazy val scalamad_di = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.lambdarat",
      scalaVersion := "2.12.4",
      version      := "meetup"
    )),
    name := "scalamad-di",
    libraryDependencies ++= Seq(
      scalaTest         % Test
    )
  )
