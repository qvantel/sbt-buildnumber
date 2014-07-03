package fi.onesto.sbt.buildnumber

import scala.util._
import sbt._


object BuildNumber extends Plugin {
  import sbt.Keys.baseDirectory

  private[this] object C {
    val Unstaged    = "*"
    val Untracked   = "%"
    val Uncommitted = "+"
  }

  private[this] object Git {
    val GetUnstaged         = "git diff --no-ext-diff --quiet --exit-code"
    val GetUncommitted      = "git diff-index --cached --quiet HEAD"
    val GetUntracked        = "git ls-files --others --exclude-standard"
    val GetBuildNumber      = "git rev-parse --quiet --verify HEAD"
    val GetShortBuildNumber = "git rev-parse --quiet --verify --short HEAD"
    val GetBranchName       = "git rev-parse --quiet --verify --abbrev-ref HEAD"
  }

  val unstagedChanges           = taskKey[Boolean]("true if the current repository has unstaged changes")
  val uncommittedChanges        = taskKey[Boolean]("true if the current repository has uncommitted changes")
  val untrackedFiles            = taskKey[Seq[File]]("list of files that are not in version control")
  val buildNumber               = taskKey[Option[String]]("current revision of the repository")
  val shortBuildNumber          = taskKey[Option[String]]("short version of the current revision of the repository")
  val branchName                = taskKey[Option[String]]("the current branch name")
  val decoratedBuildNumber      = taskKey[Option[String]]("current revision with decorations")
  val decoratedShortBuildNumber = taskKey[Option[String]]("current revision with decorations")

  private[this] implicit class BooleanToMark(val underlying: Boolean) extends AnyVal {
    def toMark(mark: String): String = if (underlying) mark else ""
  }

  private[this] def boolCommand(in: File, command: String): Boolean =
    Try(Process(command, in).! != 0) getOrElse false
  private[this] def linesCommand(in: File, command: String): Seq[String] =
    Try(Process(command, in).lines_!.toVector) getOrElse Vector.empty
  private[this] def firstLineCommand(in: File, command: String): Option[String] =
    linesCommand(in, command).headOption
  private[this] def filesCommand(in: File, command: String): Seq[File] =
    linesCommand(in, command).map(file)

  val buildNumberSettings = Seq(
    unstagedChanges    := boolCommand(baseDirectory.value, Git.GetUnstaged),
    uncommittedChanges := boolCommand(baseDirectory.value, Git.GetUncommitted),
    untrackedFiles     := filesCommand(baseDirectory.value, Git.GetUntracked),
    buildNumber        := firstLineCommand(baseDirectory.value, Git.GetBuildNumber),
    shortBuildNumber   := firstLineCommand(baseDirectory.value, Git.GetShortBuildNumber),
    branchName         := firstLineCommand(baseDirectory.value, Git.GetBranchName),

    decoratedBuildNumber := {
      buildNumber.value map { revision =>
        val unstagedMark    = (unstagedChanges.value).toMark(C.Unstaged)
        val untrackedMark   = (untrackedFiles.value.nonEmpty).toMark(C.Untracked)
        val uncommittedMark = (uncommittedChanges.value).toMark(C.Uncommitted)
        s"$revision$unstagedMark$untrackedMark$uncommittedMark"
      }
    },

    decoratedShortBuildNumber := {
      shortBuildNumber.value map { revision =>
        val unstagedMark    = (unstagedChanges.value).toMark(C.Unstaged)
        val untrackedMark   = (untrackedFiles.value.nonEmpty).toMark(C.Untracked)
        val uncommittedMark = (uncommittedChanges.value).toMark(C.Uncommitted)
        s"$revision$unstagedMark$untrackedMark$uncommittedMark"
      }
    }
  )
}
