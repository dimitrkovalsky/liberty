package com.liberty.controllers

import com.liberty.common._
import com.liberty.generators.BeanGenerator
import com.liberty.model._

/**
 * Created by Dmytro_Kovalskyi on 02.09.2014.
 */
class BeanController extends Controller with GeneratorSubscriber {
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
            createDaoSend(copy)
        }
        Some("Bean created")
      case _ => None
    }
  }

  def createDaoSend(model: JavaClass) {
    notify(CreateDaoAction(model))
  }

  override protected def onActionReceived: Receive = {
    case CreateBeanAction(model) =>
      createBean(model).map(Right(_)).getOrElse(Left("Bean creation failed"))
  }

}
