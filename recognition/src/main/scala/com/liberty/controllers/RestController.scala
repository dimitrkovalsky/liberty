package com.liberty.controllers

import com.liberty.common._
import com.liberty.generators.RestGenerator
import com.liberty.helpers.SynthesizeHelper
import com.liberty.model.JavaClass

/**
 * Created by Dmytro_Kovalskyi on 02.09.2014.
 */
class RestController extends GeneratorController with GeneratorSubscriber {
  private val generator = new RestGenerator(ProjectConfig.basePackage.nested("rest"))

  def createRest(): Option[String] = {
    performCreation(createRest)
  }

  def createRest(model: JavaClass): Option[String] = {
    val copy = model.deepCopy
    models += model.name -> copy
    val rest = generator.createRest(model)
    rest.flatMap {
      rs =>
        writer write rs
        notifyClassChanged(rs)
        if (!Register.wsCreated) {
          writer write generator.createWsClass
          Register.wsCreated = true
        }

        Register.getComponentModel(model.name).foreach { m =>
          Register.changeComponentModel(m.copy(restExists = true))
          if (!m.beanExists)
            createBeanSend(copy)
        }
        checkAdditionalFiles()
        SynthesizeHelper.synthesize("Rest service created")
        Some("Rest service created")
    }
  }

  private def checkAdditionalFiles(): Unit = {
    Register.commonClasses.getOrElse("ApplicationException", notify(Topics.GENERATION, CreateExceptionClassAction("ApplicationException")))
  }

  private def createBeanSend(model: JavaClass) {
    notify(CreateBeanAction(model))
  }

  override protected def onActionReceived: Received = {
    case CreateRestAction(model) =>
      createRest(model).map(Right(_)).getOrElse(Left("Rest creation failed"))
    case ActivateModel(modelName) => activeModel = Some(modelName)
      Right("Ok")
  }
}
