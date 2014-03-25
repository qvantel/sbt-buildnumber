sbtPlugin := true

name := "buildnumber"

organization := "fi.onesto.sbt"

description := "VCS build number plugin for SBT"

startYear := Some(2013)

homepage := Some(url("https://github.com/onesto/sbt-buildnumber"))


publishTo := Some(Classpaths.sbtPluginReleases) 

publishMavenStyle := false

publishArtifact in Test := false


scalaVersion := "2.10.4"

sbtVersion := "0.13.1"


net.virtualvoid.sbt.graph.Plugin.graphSettings


testOptions in Test += Tests.Argument("-oD")

javacOptions in ThisBuild ++= Seq(
  "-deprecation",
  "-g",
  "-source", "7",
  "-Xlint")

scalacOptions in ThisBuild ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  //"-explaintypes",
  //"-optimise",
  "-target:jvm-1.7",
  //"-Xcheck-null",
  "-Xcheckinit",
  "-Xlint",
  "-Yclosure-elim",
  "-Ydead-code",
  "-Yinline",
  "-Yinline-handlers",
  "-Ywarn-all",
  "-Xlog-reflective-calls",
  "-Xmax-classfile-name", "130"  // avoid problems on eCryptFS
)

