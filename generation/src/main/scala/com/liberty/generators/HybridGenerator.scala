package com.liberty.generators

import com.liberty.model.{JavaInterface, TemplateClass}
import com.liberty.parsers.{InternalPath, JavaClassParser, JavaInterfaceParser}
import com.typesafe.config.ConfigFactory

/**
 * Uses for generation based on templates and models
 * Created by Dmytro_Kovalskyi on 25.09.2014.
 */
trait HybridGenerator {
  protected def createClassParser(path: String): JavaClassParser = {
    new JavaClassParser(path) with InternalPath
  }

  protected def createInterfaceParser(path: String): JavaInterfaceParser = {
    new JavaInterfaceParser(path) with InternalPath
  }

  /**
   * Use to load and parse to TemplateClass from template folder
   * Use loadModel for model loading
   * @param configName
   * @return
   */
  protected def loadClass(configName: String, basePackageConfig: String): TemplateClass = {
    val env = scala.util.Properties.envOrElse("templates", "templates")
    val config = ConfigFactory.load(env)
    val path = config.getString(configName) + ".tmpl"
    val baseTemplatePackage = config.getString(basePackageConfig)
    val template = createClassParser(path) parse(baseTemplatePackage)
    template.baseTemplatePackage = baseTemplatePackage
    template
  }

  protected def loadConfig(configName: String): String = {
    val env = scala.util.Properties.envOrElse("templates", "templates")
    val config = ConfigFactory.load(env)
    config.getString(configName)
  }

  /**
   * Uses different path for templates than loadClass
   * @param configName
   * @return
   */
  protected def loadModel(configName: String, basePackageConfig: String): TemplateClass = {
    val env = scala.util.Properties.envOrElse("templates", "templates")
    val config = ConfigFactory.load(env)
    val modelPath = "/models/" + config.getString(configName) + ".tmpl"
    val baseTemplatePackage = config.getString(basePackageConfig)
    val template = createClassParser(modelPath) parse(baseTemplatePackage)
    template.baseTemplatePackage = baseTemplatePackage
    template
  }


  protected def loadInterface(configName: String): JavaInterface = {
    val env = scala.util.Properties.envOrElse("templates", "templates")
    val config = ConfigFactory.load(env)
    val path = config.getString(configName) + ".tmpl"
    createInterfaceParser(path) parse()
  }
}
