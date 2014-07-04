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

  val scmType                   = taskKey[Scm]("detected type of version control")
  val unstagedChanges           = taskKey[Boolean]("true if the current repository has unstaged changes")
  val uncommittedChanges        = taskKey[Boolean]("true if the current repository has uncommitted changes")
  val untrackedFiles            = taskKey[Seq[File]]("list of files that are not in version control")
  val buildNumber               = taskKey[Option[String]]("current revision of the repository")
  val shortBuildNumber          = taskKey[Option[String]]("short version of the current revision of the repository")
  val branchName                = taskKey[Option[String]]("the current branch name")
  val decoratedBuildNumber      = taskKey[Option[String]]("current revision with decorations")
  val decoratedShortBuildNumber = taskKey[Option[String]]("short version of the current revision with decorations")

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
    scmType            := {
      if ((((baseDirectory in LocalRootProject).value) / ".git").exists())
        Git
      else if ((((baseDirectory in LocalRootProject).value) / ".hg").exists())
        Mercurial
      else
        NoScm
    },
    unstagedChanges    := boolCommand(baseDirectory.value, scmType.value.getUnstaged),
    uncommittedChanges := boolCommand(baseDirectory.value, scmType.value.getUncommitted),
    untrackedFiles     := filesCommand(baseDirectory.value, scmType.value.getUntracked),
    buildNumber        := firstLineCommand(baseDirectory.value, scmType.value.getBuildNumber),
    shortBuildNumber   := firstLineCommand(baseDirectory.value, scmType.value.getShortBuildNumber),
    branchName         := firstLineCommand(baseDirectory.value, scmType.value.getBranchName),

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
