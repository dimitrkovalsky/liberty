package com.liberty.controllers

import com.liberty.common.{ClassMapper, ProjectConfig}
import com.liberty.model.JavaClass
import com.liberty.parsers.{InternalPath, JavaClassParser}
import com.typesafe.config.ConfigFactory

import scala.util.{Success, Try}

/**
 * Created by Dmytro_Kovalskyi on 02.09.2014.
 */
class RestController extends Controller {
  private val template = loadTemplate()
  private val baseModel = loadBaseModel()

  private def loadTemplate() = {
    val env = scala.util.Properties.envOrElse("templates", "templates")
    val config = ConfigFactory.load(env)
    val path = config.getString("rest.template") + ".tmpl"
    new JavaClassParser(path) with InternalPath parse()
  }

  private def loadBaseModel() = {
    val env = scala.util.Properties.envOrElse("templates", "templates")
    val config = ConfigFactory.load(env)
    val modelPath = "/models/" + config.getString("rest.model") + ".tmpl"
    new JavaClassParser(modelPath) with InternalPath parse()
  }

  def createRest(model: JavaClass): Try[String] = {
    println(template)
    val restPackage = ProjectConfig.basePackage.nested("rest")
    val copy = model.deepCopy
    models += model.name -> copy
    val mapper = ClassMapper(baseModel)
    val rest = mapper.changeModel(template, copy, model.name + "Resource", restPackage)
    println(rest)
    Success("Rest service was created")
  }
}
