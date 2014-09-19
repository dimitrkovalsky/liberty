package com.liberty.controllers

import com.liberty.common.Implicits._
import com.liberty.common.{ProjectConfig, TypeMapper}
import com.liberty.model._
import com.liberty.operations.PatternOperation
import com.liberty.parsers.{InternalPath, JavaClassParser}
import com.liberty.traits.Writer
import com.liberty.types.DataType
import com.liberty.types.collections.CollectionType
import com.liberty.writers.FileClassWriter
import com.typesafe.config.ConfigFactory

import scala.util.{Success, Try}

/**
 * Created by Dmytro_Kovalskyi on 02.09.2014.
 */
class BeanController {
  private val writer: Writer = new FileClassWriter(ProjectConfig.projectPath)
  private val models = scala.collection.mutable.Map[String, JavaClass]()
  private val template = loadTemplate()
  private val baseModel = loadBaseModel()
  private val baseModelName = baseModel.name

  private def loadTemplate() = {
    val env = scala.util.Properties.envOrElse("templates", "templates")
    val config = ConfigFactory.load(env)
    val path = config.getString("bean.template") + ".tmpl"
    new JavaClassParser(path) with InternalPath parse()
  }

  private def loadBaseModel() = {
    val env = scala.util.Properties.envOrElse("templates", "templates")
    val config = ConfigFactory.load(env)
    val modelPath = "/models/" + config.getString("bean.model") + ".tmpl"
    new JavaClassParser(modelPath) with InternalPath parse()
  }

  def createBean(model: JavaClass): Try[String] = {
    //    println(model.toString)
    val copy = model.deepCopy
    models += model.name -> copy
    //    println(model)
    //    println(template.functions)
    template.functions.foreach(changeModel(_, copy))
    Success("Bean created")
  }

  def changeBody(body: FunctionBody, clazz: JavaClass) = {
    val ops = body.operations.map {
      case PatternOperation(pattern, _) if pattern.shouldChange => Some()
        // TODO: change pattern string
    }
  }

  private def changeModel(function: JavaFunction, model: JavaClass): Option[JavaFunction] = {
    //println(function)
    if (shouldExist(function.signature, model)) {
      val sign = changeSignature(function.signature, model)
      val body = changeBody(function.body, model)
      println(function.signature)
      println(sign)
      println(function.body)
      Some(function)
    }
    else
      None
    //println(model.name)
  }

  private implicit class ModelString(s: String) {
    def shouldChangeNotIgnoreCase: Boolean = s contains baseModelName

    def shouldChange: Boolean = s.toLowerCase contains baseModelName.toLowerCase
  }

  def changeName(name: String, model: JavaClass) = {
    if (name.shouldChangeNotIgnoreCase) name.replace(baseModelName, model.name)
    else name // TODO: change name if register is different (departmentsGet => intitutesGet <model : Department>)
  }


  private def changeSignature(signature: FunctionSignature, model: JavaClass) = {
    // TODO: process id in annotations
    val output = if (signature.output.toString.shouldChangeNotIgnoreCase) changeDataType(signature.output, model.dataType) else signature.output
    val name = if (signature.name.shouldChange) changeName(signature.name, model) else signature.name
    val input = signature.input.map(in => {
      // if it is idField
      if (baseModel.getIdField.exists(f => f.name.equals(in.paramName.name) && f.dataType.equals(in.paramType)) && model.getIdField.isDefined) {
        val idField = model.getIdField.get
        FunctionParameter(in.paramName.name, idField.dataType)
      } else {
        val newName = if (in.paramName.name.shouldChange) changeName(in.paramName.name, model) else in.paramName.name
        val newType = if (in.paramType.toString.shouldChangeNotIgnoreCase) changeDataType(in.paramType, model.dataType) else in.paramType
        FunctionParameter(newName, newType)
      }
    })

    // TODO: process throws
    FunctionSignature(name, output, signature.modifier, input, signature.functionThrows)
  }


  private def changeDataType(oldType: DataType, newType: DataType): DataType = {
    if (TypeMapper.isCollection(oldType))
      TypeMapper.changeType(oldType.asInstanceOf[CollectionType], oldType, newType).getOrElse(oldType)
    else
      newType
  }


  /**
   * Validate if function contains work with foreign key
   * @param signature
   * @param model
   * @return
   */
  private def shouldExist(signature: FunctionSignature, model: JavaClass): Boolean = {
    val line = signature.name
    // if function contains work with foreign key
    if (line.toLowerCase.containsOr(List("get", "by"), List("find", "by"))) {
      // TODO: contains id ignoring PK
      model.fields.count(_.name.toLowerCase.contains("id")) > 2
    }
    else true
  }


}
