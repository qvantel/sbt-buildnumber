sbtPlugin := true

name := "buildnumber"

version := "0.0.2"

organization := "fi.onesto.sbt"

description := "VCS build number plugin for SBT"

startYear := Some(2013)

homepage := Some(url("https://github.com/onesto/sbt-buildnumber"))


publishTo := Some(Classpaths.sbtPluginReleases) 

publishMavenStyle := false

publishArtifact in Test := false


scalaVersion := "2.10.4"

sbtVersion := "0.13.5"


net.virtualvoid.sbt.graph.Plugin.graphSettings
