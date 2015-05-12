import bintray.Keys._

sbtPlugin := true

scalaVersion := "2.10.4"

sbtVersion := "0.13.5"

name := "sbt-buildnumber"

version := "0.0.3"

organization := "fi.onesto.sbt"

organizationName := "Onesto Services Oy"

organizationHomepage := Some(new java.net.URL("http://onesto.fi"))

description := "VCS build number plugin for SBT"

startYear := Some(2013)

homepage := Some(url("https://github.com/onesto/sbt-buildnumber"))

scmInfo := Some(ScmInfo(new java.net.URL("https://github.com/onesto/sbt-buildnumber"), "scm:git:github.com/onesto/sbt-buildnumber.git", Some("scm:git:git@github.com:onesto/sbt-buildnumber.git")))

bintrayPublishSettings

publishMavenStyle := false

publishArtifact in Test := false

repository in bintray := "sbt-plugins"

bintrayOrganization in bintray := Some("onesto")

licenses += ("MIT", url("https://github.com/onesto/sbt-buildnumber/blob/master/LICENSE"))
