name := "ChineseCheckers"
version := "0.1"
scalaVersion := "2.12.4"

enablePlugins(ScalaJSPlugin)

// ScalaJS Dependencies
libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.3",
  //"com.outr" %%% "scribe" % "1.4.3",
)

// Scala Dependencies
libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.12" % "3.0.4",
  "org.scalatest" % "scalatest_2.12" % "3.0.4" % "test",
  //"com.outr" %% "scribe" % "1.4.3",
)