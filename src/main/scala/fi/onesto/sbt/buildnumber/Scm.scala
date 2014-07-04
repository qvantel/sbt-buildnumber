package fi.onesto.sbt.buildnumber


sealed trait Scm {
  def getUnstaged: String
  def getUncommitted: String
  def getUntracked: String
  def getBuildNumber: String
  def getShortBuildNumber: String
  def getBranchName: String
}

case object Git extends Scm {
  override val getUnstaged         = "git diff --no-ext-diff --quiet --exit-code"
  override val getUncommitted      = "git diff-index --cached --quiet HEAD"
  override val getUntracked        = "git ls-files --others --exclude-standard"
  override val getBuildNumber      = "git rev-parse --quiet --verify HEAD"
  override val getShortBuildNumber = "git rev-parse --quiet --verify --short HEAD"
  override val getBranchName       = "git rev-parse --quiet --verify --abbrev-ref HEAD"
}

case object Mercurial extends Scm {
  override val getUnstaged         = "false"
  override val getUncommitted      = "hg id -i|grep \\+"
  override val getUntracked        = "hg st -un"
  override val getBuildNumber      = "hg id -i | sed 's/+//'"
  override val getShortBuildNumber = "hg id -i | sed 's/+//'"
  override val getBranchName       = "hg branch"
}

case object NoScm extends Scm {
  override val getUnstaged         = "false"
  override val getUncommitted      = "false"
  override val getUntracked        = "false"
  override val getBuildNumber      = "false"
  override val getShortBuildNumber = "false"
  override val getBranchName       = "false"
}