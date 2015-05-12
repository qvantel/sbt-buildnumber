sbtPlugin := true

scalaVersion := "2.10.5"

sbtVersion := "0.13.8"

name := "sbt-buildnumber"

version := "0.1.0"

organization := "fi.onesto.sbt"

organizationName := "Onesto Services Oy"

organizationHomepage := Some(new java.net.URL("http://onesto.fi"))

description := "VCS build number plugin for SBT"

startYear := Some(2013)

homepage := Some(url("https://github.com/onesto/sbt-buildnumber"))

scmInfo := Some(ScmInfo(new java.net.URL("https://github.com/onesto/sbt-buildnumber"), "scm:git:github.com/onesto/sbt-buildnumber.git", Some("scm:git:git@github.com:onesto/sbt-buildnumber.git")))

publishMavenStyle := false

publishArtifact in Test := false

bintrayOrganization := Option("onesto")

licenses += ("MIT", url("https://github.com/onesto/sbt-buildnumber/blob/master/LICENSE"))
