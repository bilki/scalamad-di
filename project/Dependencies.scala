import sbt._

object Dependencies {
  lazy val scalaTest  = "org.scalatest"     %% "scalatest" % "3.0.4"
  lazy val cats       = "org.typelevel"     %% "cats-core" % "1.0.1"
  lazy val guice      = "com.google.inject" %  "guice"     % "4.1.0"
}
