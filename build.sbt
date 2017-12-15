name := "PixiExp"
version := "0.1"
scalaVersion := "2.12.4"

enablePlugins(ScalaJSPlugin)

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.3",
  //"com.outr" %%% "scribe" % "1.4.3",
  //"be.doeraene" %%% "scalajs-jquery" % "0.9.1"
)