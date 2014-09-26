package com.liberty.controllers

import com.liberty.common.{Model, ProjectConfig, Register}
import com.liberty.generators.BeanGenerator
import com.liberty.model._

/**
 * Created by Dmytro_Kovalskyi on 02.09.2014.
 */
class BeanController extends Controller {
  private val generator = new BeanGenerator(ProjectConfig.basePackage.nested("beans"))

  def createBean(model: JavaClass): Option[String] = {
    val copy = model.deepCopy
    models += model.name -> copy
    generator.createBean(copy) match {
      case Some(packet) =>
        val bean = packet.bean
        val interface = packet.beanInterface
        writer write interface
        writer write bean

        if (!Register.beansXmlCreated) {
          val beansXml = generator.createBeansXml
          writer.writeToMetaInf(beansXml)
          Register.beansXmlCreated = true
        }

        Register.getModel(model.name).foreach { m =>
          Register.changeModel(m.copy(beanExists = true))
          if (!m.daoExists)
            createDaoSend(m)
        }
        Some("Bean was created")
      case _ => None
    }
  }

  def createDaoSend(model: Model) {

  }

}
