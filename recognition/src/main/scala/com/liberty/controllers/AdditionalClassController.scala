package com.liberty.controllers

import com.liberty.common.{CreateExceptionClassAction, ProjectConfig, GeneratorSubscriber}
import com.liberty.generators.{AdditionalClassGenerator, BeanGenerator}

/**
 * User: Dmytro_Kovalskyi
 * Date: 05.11.2014
 * Time: 17:32
 */
class AdditionalClassController extends GeneratorController with GeneratorSubscriber{
  private val generator = new AdditionalClassGenerator
  override protected def onActionReceived: Received = {
    case CreateExceptionClassAction(name) => Right("OK")//generator.createException(name)
  }
}
