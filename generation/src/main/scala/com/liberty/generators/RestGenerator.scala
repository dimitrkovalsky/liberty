package com.liberty.generators

import com.liberty.builders.ClassBuilder
import com.liberty.common.{ClassMapper, ProjectConfig}
import com.liberty.model.{JavaClass, SingleMemberAnnotation}
import com.liberty.traits.{JavaPackage, LocationPackage}

/**
 * Created by Dmytro_Kovalskyi on 26.09.2014.
 */
class RestGenerator(basePackage: LocationPackage = ProjectConfig.basePackage.nested("rest")) extends HybridGenerator {
  private val REST_PATH = "rest"
  private val template = loadClass("rest.template", "rest.base_package")
  private val baseModel = loadModel("rest.model", "rest.base_package")

  def createRest(model: JavaClass): Option[JavaClass] = {
    val mapper = ClassMapper(baseModel)
    val rest = mapper.changeModel(template, model, model.name + "Resource", basePackage)
    Some(rest)
  }

  def createWsClass: JavaClass = {
    val builder = ClassBuilder("WS")
    builder.addPackage(basePackage.nestedClass("WS"))
    builder.addAnnotation(new SingleMemberAnnotation("ApplicationPath", REST_PATH, JavaPackage("javax.ws.rs", "ApplicationPath")))
    builder.addExtend(new JavaClass("Application", JavaPackage("javax.ws.rs.core", "Application")))
    builder.getJavaClass
  }
}
