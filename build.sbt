import Dependencies._

lazy val rserrano = Developer("bilki", "Roberto Serrano", "r.semarcos@gmail.com", url("http://lambdarat.com"))

lazy val scalamad_di = (project in file(".")).
  settings(
    inThisBuild(List(
      developers   := List(rserrano),
      organization := "com.lambdarat",
      scalaVersion := "2.12.4",
      version      := "meetup"
    )),
    name := "scalamad-di",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      cats
    )
  )

scalacOptions += "-Ypartial-unification"

parallelExecution in Test := false
