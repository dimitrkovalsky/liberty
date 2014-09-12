package com.liberty.controllers

import com.liberty.common.ProjectConfig
import com.liberty.traits.Writer
import com.liberty.writers.FileClassWriter
import com.typesafe.config.ConfigFactory

/**
 * Created by Dmytro_Kovalskyi on 02.09.2014.
 */
class BeanController extends InternalPath{
  private val writer: Writer = new FileClassWriter(ProjectConfig.projectPath)
  private val template =

  private def loadConfig(){
    val env = scala.util.Properties.envOrElse("runMode", "prod")
    val config = ConfigFactory.load(env)
    val url = config.getString("db.url")
    val username = config.getString("db.username")
    val password = config.getString("db.password")
    val driver = config.getString("db.driver")
  }
}
