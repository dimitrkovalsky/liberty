package com.liberty.controllers

import com.liberty.common.{Register, CreateExceptionClassAction, ProjectConfig, GeneratorSubscriber}
import com.liberty.generators.{AdditionalClassGenerator, BeanGenerator}

/**
 * User: Dmytro_Kovalskyi
 * Date: 05.11.2014
 * Time: 17:32
 */
class AdditionalClassController extends GeneratorController with GeneratorSubscriber {
  private val generator = new AdditionalClassGenerator

  override protected def onActionReceived: Received = {
    case CreateExceptionClassAction(name, base) =>
      CreateException(name, base)
      Right(s"$name class created")
  }

  private def CreateException(name: String, baseException: String): Unit = {
    val clazz = generator.createException(name, baseException)

    writer write clazz

    Register.commonClasses.put(clazz.name, true)
    Register.commonClasses.getOrElse(baseException, if (baseException != "Exception") CreateException(baseException, "Exception"))
  }
}
