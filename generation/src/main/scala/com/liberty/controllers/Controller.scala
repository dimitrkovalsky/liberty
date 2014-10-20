package com.liberty.controllers

import com.liberty.common.ProjectConfig
import com.liberty.model.JavaClass
import com.liberty.traits.Writer
import com.liberty.writers.FileClassWriter

/**
 * Contains writer and map for modle storing
 * Created by Dmytro_Kovalskyi on 24.09.2014.
 */

trait Controller {
  protected val writer: Writer = new FileClassWriter(ProjectConfig.projectPath)
  /**
   * List of managed models
   */
  protected val models = scala.collection.mutable.Map[String, JavaClass]()
}
