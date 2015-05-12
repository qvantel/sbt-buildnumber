package fi.onesto.sbt.buildnumber

import sbt._


sealed trait Scm {
  def getUnstaged(cwd: File):         ProcessBuilder
  def getUncommitted(cwd: File):      ProcessBuilder
  def getUntracked(cwd: File):        ProcessBuilder
  def getBuildNumber(cwd: File):      ProcessBuilder
  def getShortBuildNumber(cwd: File): ProcessBuilder
  def getBranchName(cwd: File):       ProcessBuilder
}

case object Git extends Scm {
  override def getUnstaged(cwd: File)         = Process("git diff --no-ext-diff --quiet --exit-code", cwd)
  override def getUncommitted(cwd: File)      = Process("git diff-index --cached --quiet HEAD", cwd)
  override def getUntracked(cwd: File)        = Process("git ls-files --others --exclude-standard", cwd)
  override def getBuildNumber(cwd: File)      = Process("git rev-parse --quiet --verify HEAD", cwd)
  override def getShortBuildNumber(cwd: File) = Process("git rev-parse --quiet --verify --short HEAD", cwd)
  override def getBranchName(cwd: File)       = Process("git rev-parse --quiet --verify --abbrev-ref HEAD", cwd)
}

case object Mercurial extends Scm {
  override def getUnstaged(cwd: File)         = Process("true")
  override def getUncommitted(cwd: File)      = Process("hg id -i", cwd) #| Process(Seq("fgrep", "-v", "+"))
  override def getUntracked(cwd: File)        = Process("hg st -un", cwd)
  override def getBuildNumber(cwd: File)      = Process("hg id -i", cwd) #| Process(Seq("sed", "s/+//"))
  override def getShortBuildNumber(cwd: File) = Process("hg id -i", cwd) #| Process(Seq("sed", "s/+//"))
  override def getBranchName(cwd: File)       = Process("hg branch", cwd)
}

case object NoScm extends Scm {
  override def getUnstaged(cwd: File)         = Process("false")
  override def getUncommitted(cwd: File)      = Process("false")
  override def getUntracked(cwd: File)        = Process("false")
  override def getBuildNumber(cwd: File)      = Process("false")
  override def getShortBuildNumber(cwd: File) = Process("false")
  override def getBranchName(cwd: File)       = Process("false")
}

object Scm {
  def detect(rootDirectory: File): Scm = {
    if ((rootDirectory / ".git").exists())
      Git
    else if ((rootDirectory / ".hg").exists())
      Mercurial
    else
      NoScm
  }
}