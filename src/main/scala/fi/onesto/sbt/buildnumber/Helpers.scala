package fi.onesto.sbt.buildnumber

import scala.util.Try
import sbt._


private[buildnumber] object Helpers {
  object C {
    val Unstaged    = "*"
    val Untracked   = "%"
    val Uncommitted = "+"
  }

  implicit class BooleanToMark(val underlying: Boolean) extends AnyVal {
    def toMark(mark: String): String = if (underlying) mark else ""
  }

  def boolCommand(command: ProcessBuilder): Boolean =
    Try(command.! != 0) getOrElse false
  def linesCommand(command: ProcessBuilder): Seq[String] =
    Try(command.lines_!.toVector) getOrElse Vector.empty
  def firstLineCommand(command: ProcessBuilder): Option[String] =
    linesCommand(command).headOption
  def filesCommand(command: ProcessBuilder): Seq[File] =
    linesCommand(command).map(file)
}

