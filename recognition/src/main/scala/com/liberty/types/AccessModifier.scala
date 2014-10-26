package com.liberty.types

/**
 * User: dkovalskyi
 * Date: 15.07.13
 * Time: 15:26
 */
object AccessModifier {
  final val NONE: Int = 0
  final val PUBLIC: Int = 1
  final val PROTECTED: Int = 2
  final val PRIVATE: Int = 3

  def toString(modifier: Int): String = {
    modifier match {
      case NONE => ""
      case PUBLIC => "public"
      case PROTECTED => "protected"
      case PRIVATE => "private"
      case _ => "[AccessModifier] ERROR"
    }
  }
}
