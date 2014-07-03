sbt-buildnumber
===============

This plugin adds some keys containing SCM information. It may be useful in
conjuction with [sbt-buildinfo](https://github.com/sbt/sbt-buildinfo).

Currently only Git and Mercurial (via command-line commands) are supported

Installation
------------

If you do not have it already, create a `project/project/Plugins.scala` with content like:

    import sbt._

    object Plugins extends Build {
      lazy val sbtBuildNumberPlugin = uri("git://github.com/onesto/sbt-buildnumber.git")
      lazy val plugins = Project("plugins", file(".")).dependsOn(sbtBuildNumberPlugin)
    }


Usage
-----

Add this to your `build.sbt`:

    buildNumberSettings

With [sbt-buildinfo](https://github.com/sbt/sbt-buildinfo) you can use the 
provided keys in `buildInfoKeys`. For example:

    buildInfoKeys := Seq[BuildInfoKey](organization, name, version, shortBuildNumber)


Keys
----

The available keys are:

* `scmType: Scm` - detected version control system
* `unstagedChanges: Boolean` – true if the current repository has unstaged changes
* `uncommittedChanges: Boolean` – true if the current repository has uncommitted changes
* `untrackedFiles: Seq[File]` – list of files that are not in version control
* `buildNumber: Option[String]` – current revision of the repository
* `shortBuildNumber: Option[String]` – short version of the current revision of the repository
* `branchName: Option[String]` – the current branch name
* `decoratedBuildNumber: Option[String]` – current revision with decorations
* `decoratedShortBuildNumber: Option[String]` – current revision with decorations (short version)

The values default to `false`, `None` or an empty list if the project is not under version
control of if there is an error.

The decorated build numbers are the corresponding build number with markers similar
to `__git_ps1`.

* `*` – project contains unstaged changes
* `%` – project contains untracked files
* `+` - project contains staged but uncommitted changes
