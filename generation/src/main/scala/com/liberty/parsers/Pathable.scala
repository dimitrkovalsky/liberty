package com.liberty.parsers

import java.io.File

/**
 * Created by Dmytro_Kovalskyi on 08.09.2014.
 */
/**
 *
 */
trait Pathable {
  private def getProjectFolder = {
    val path = new File(".").getAbsolutePath
    path.substring(0, path.length() - 1)
  }

  val PROJECT_PATH = getProjectFolder

  def getTemplatePath: String

}

trait InternalPath extends Pathable {
  override def getTemplatePath = PROJECT_PATH + "//generation//templates//"
}

