package com.liberty.controllers

import com.liberty.common.{Model, ProjectConfig, Register}
import com.liberty.generators.RestGenerator
import com.liberty.model.JavaClass

/**
 * Created by Dmytro_Kovalskyi on 02.09.2014.
 */
class RestController extends Controller {
  private val generator = new RestGenerator(ProjectConfig.basePackage.nested("rest"))

  def createRest(model: JavaClass): Option[String] = {
    val copy = model.deepCopy
    models += model.name -> copy
    val rest = generator.createRest(model)
    rest.flatMap {
      rs =>
        writer write rs
        if (!Register.wsCreated) {
          writer write generator.createWsClass
          Register.wsCreated = true
        }

        Register.getModel(model.name).foreach { m =>
          Register.changeModel(m.copy(restExists = true))
          if (!m.beanExists)
            createBeanSend(m)
        }
        Some("Rest service was created")
    }
  }

  def createBeanSend(model: Model) {}
}
