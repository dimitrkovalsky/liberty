package com.liberty.helpers

/**
 * User: Dimitr
 * Date: 02.11.13
 * Time: 11:33
 */
object StringHelper {
  def isBoolean(input: String): Boolean = {
    try {
      input.toBoolean
      true
    } catch {
      case _: Throwable => false
    }
  }

  def getPathByPackageName(packageName: String): String = {
    packageName.replace(".", "/")
  }

  def isNumeric(input: String): Boolean = input.forall(_.isDigit)
}
