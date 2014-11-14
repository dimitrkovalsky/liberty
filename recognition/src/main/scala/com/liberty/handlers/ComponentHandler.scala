package com.liberty.handlers

import com.liberty.common.{GrammarGroups, GrammarIds}
import com.liberty.controllers.{Controllers, ClassController}
import com.liberty.entities.RecognitionResult

/**
 * Created by Maxxis on 11/8/2014.
 */
class ComponentHandler extends Handler {
  private val beanController = Controllers.beanController
  private val classController = Controllers.classController

  override def onRecognized(recognized: RecognitionResult) {
    recognized.best.label match {
      case GrammarIds.CREATE_CLASS => classController.startClassCreation()
      case GrammarIds.CREATE_BEAN =>
        if (beanController.isModelActive)
          beanController.createBean()
        else
          System.err.println("[ComponentHandler] can not create bean") // TODO: show list to choose model
    }
  }
}
