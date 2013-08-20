package fi.onesto.sbt.buildnumber

import scala.util._
import sbt._


object BuildNumber extends Plugin {
  object Keys {
    val unstagedChanges      = taskKey[Boolean]("true if the current repository has unstaged changes")
    val uncommittedChanges   = taskKey[Boolean]("true if the current repository has uncommitted changes")
    val untrackedFiles       = taskKey[Seq[File]]("list of files that are not in version control")
    val buildNumber          = taskKey[Option[String]]("current revision of the repository")
    val decoratedBuildNumber = taskKey[Option[String]]("current revision with decorations")
  }

  import Keys._

  val buildNumberSettings = Seq(
    unstagedChanges      := Try("git diff --no-ext-diff --quiet --exit-code".! != 0) getOrElse false,

    untrackedFiles       := Try("git ls-files --others --exclude-standard".lines_!.map(file)) getOrElse Seq.empty,

    uncommittedChanges   := Try("git diff-index --cached --quiet HEAD".! != 0) getOrElse false,

    buildNumber          := Try("git rev-parse --quiet --verify HEAD".lines_!.headOption) getOrElse None,

    decoratedBuildNumber := {
      buildNumber.value map { revision =>
        val unstagedMark    = if (unstagedChanges.value)        "*" else ""
        val untrackedMark   = if (untrackedFiles.value.isEmpty) "%" else ""
        val uncommittedMark = if (uncommittedChanges.value)     "+" else ""
        s"$revision$unstagedMark$untrackedMark$uncommittedMark"
      }
    }
  )
}
