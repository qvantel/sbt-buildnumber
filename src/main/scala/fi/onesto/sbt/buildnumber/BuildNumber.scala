package fi.onesto.sbt.buildnumber

import sbt._


object BuildNumber extends AutoPlugin {
  import Helpers._
  import sbt.Keys.baseDirectory

  val scmType                   = taskKey[Scm]("detected type of version control")
  val unstagedChanges           = taskKey[Boolean]("true if the current repository has unstaged changes")
  val uncommittedChanges        = taskKey[Boolean]("true if the current repository has uncommitted changes")
  val untrackedFiles            = taskKey[Seq[File]]("list of files that are not in version control")
  val buildNumber               = taskKey[Option[String]]("current revision of the repository")
  val shortBuildNumber          = taskKey[Option[String]]("short version of the current revision of the repository")
  val branchName                = taskKey[Option[String]]("the current branch name")
  val decoratedBuildNumber      = taskKey[Option[String]]("current revision with decorations")
  val decoratedShortBuildNumber = taskKey[Option[String]]("short version of the current revision with decorations")

  override val trigger = allRequirements

  override val projectSettings = Seq(
    scmType := Scm.detect((baseDirectory in LocalRootProject).value),

    unstagedChanges    := boolCommand(scmType.value.getUnstaged(baseDirectory.value)),
    uncommittedChanges := boolCommand(scmType.value.getUncommitted(baseDirectory.value)),
    untrackedFiles     := filesCommand(scmType.value.getUntracked(baseDirectory.value)),
    buildNumber        := firstLineCommand(scmType.value.getBuildNumber(baseDirectory.value)),
    shortBuildNumber   := firstLineCommand(scmType.value.getShortBuildNumber(baseDirectory.value)),
    branchName         := firstLineCommand(scmType.value.getBranchName(baseDirectory.value)),

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
