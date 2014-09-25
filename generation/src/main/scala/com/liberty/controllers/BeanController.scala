package com.liberty.controllers

import com.liberty.builders.{ClassBuilder, InterfaceBuilder}
import com.liberty.common.{ClassMapper, Model, ProjectConfig, Register}
import com.liberty.model._
import com.liberty.parsers.{InternalPath, JavaClassParser, JavaInterfaceParser}
import com.typesafe.config.ConfigFactory

import scala.util.{Success, Try}

/**
 * Created by Dmytro_Kovalskyi on 02.09.2014.
 */
class BeanController extends Controller {
  private val template = loadTemplate()
  private val baseInterface = loadInterface()
  private val baseModel = loadBaseModel()

  private def loadTemplate() = {
    val env = scala.util.Properties.envOrElse("templates", "templates")
    val config = ConfigFactory.load(env)
    val path = config.getString("bean.template") + ".tmpl"
    new JavaClassParser(path) with InternalPath parse()
  }

  private def loadInterface() = {
    val env = scala.util.Properties.envOrElse("templates", "templates")
    val config = ConfigFactory.load(env)
    val path = config.getString("bean.interface") + ".tmpl"
    new JavaInterfaceParser(path) with InternalPath parse()
  }

  private def loadBaseModel() = {
    val env = scala.util.Properties.envOrElse("templates", "templates")
    val config = ConfigFactory.load(env)
    val modelPath = "/models/" + config.getString("bean.model") + ".tmpl"
    new JavaClassParser(modelPath) with InternalPath parse()
  }

  //  TODO: DaoShould exists
  def createBean(model: JavaClass): Try[String] = {
    val beanPackage = ProjectConfig.basePackage.nested("beans")
    val copy = model.deepCopy
    models += model.name -> copy
    val mapper = ClassMapper(baseModel)
    val functions = template.functions.map(f => mapper.changeFunction(f, copy)).flatten
    val fields = template.fields.map(mapper.changeField(_, copy))
    val classBuilder = ClassBuilder(model.name + "Bean")
    classBuilder.addFunctions(functions)
    template.customImports.map(mapper.changeCustomImport).flatten.foreach(classBuilder.addCustomImport)
    fields.foreach(classBuilder.addField)
    classBuilder.addPackage(beanPackage)
    val bean = classBuilder.getJavaClass

    val interfaceBuilder = InterfaceBuilder("I" + model.name + "Bean")
    val interfaceSignatures = baseInterface.signatures.map(mapper.changeSignatureWithFilter(_, copy)).flatten
    interfaceSignatures foreach interfaceBuilder.addFunctionSignature
    baseInterface.annotations.foreach(interfaceBuilder.addAnnotation)
    baseInterface.customImports.map(mapper.changeCustomImport).flatten.foreach(interfaceBuilder.addCustomImport)
    interfaceBuilder.addPackage(beanPackage)
    val interface = interfaceBuilder.getInterface
    bean.addImplements(interface)
    writer.write(bean)
    writer.write(interface)

    Register.getModel(model.name).foreach { m =>
      Register.changeModel(m.copy(beanExists = true))
      if (!m.daoExists)
        createDaoSend(m)
    }

    Success("Bean created")
  }

  def createDaoSend(model: Model): Unit = {

  }

}
