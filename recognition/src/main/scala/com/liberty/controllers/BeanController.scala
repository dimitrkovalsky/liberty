package com.liberty.controllers

import com.liberty.common._
import com.liberty.generators.BeanGenerator
import com.liberty.helpers.SynthesizeHelper
import com.liberty.model._

/**
 * Created by Dmytro_Kovalskyi on 02.09.2014.
 */
class BeanController extends GeneratorController with GeneratorSubscriber {
  private val generator = new BeanGenerator(ProjectConfig.basePackage.nested("beans"))

  def createBean(): Option[String] = {
    val res = performCreation(createBean)
    SynthesizeHelper.synthesize("Bean created")
    res
  }

  def testNotification(): Unit = {
    notify(Topics.USER_NOTIFICATION, UserNotificationAction(1, Right("Test notification")))
  }

  def createBean(model: JavaClass): Option[String] = {
    val copy = model.deepCopy
    models += model.name -> copy
    generator.createBean(copy) match {
      case Some(packet) =>
        val bean = packet.bean
        val interface = packet.beanInterface
        writer write interface
        writer write bean
        notifyClassChanged(bean)
        if (!Register.beansXmlCreated) {
          val beansXml = generator.createBeansXml
          writer.writeToMetaInf(beansXml)
          Register.beansXmlCreated = true
        }
        // Model should be added to register
        Register.getComponentModel(model.name).foreach { m =>
          Register.changeComponentModel(m.copy(beanExists = true))
          if (!m.daoExists)
            createDaoSend(copy)
        }
        checkAdditionalFiles()
        activeModel = None
        Some("Bean created")
      case _ => None
    }
  }

  private def checkAdditionalFiles(): Unit = {
    Register.commonClasses.getOrElse("ApplicationException", notify(Topics.GENERATION, CreateExceptionClassAction("ApplicationException")))
  }

  def createDaoSend(model: JavaClass) {
    notify(CreateDaoAction(model))
  }

  override protected def onActionReceived: Received = {
    case CreateBeanAction(model) =>
      createBean(model).map(s => Right(s)).getOrElse(Left("Bean creation failed"))
    case ActivateModel(modelName) => activeModel = Some(modelName)
      Right("Ok")
  }

}
