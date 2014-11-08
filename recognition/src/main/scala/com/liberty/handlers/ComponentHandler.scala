package com.liberty.handlers

import com.liberty.common.{GrammarGroups, GrammarIds}
import com.liberty.controllers.{Controllers, ClassController}
import com.liberty.entities.RecognitionResult

/**
 * Created by Maxxis on 11/8/2014.
 */
class ComponentHandler extends Handler {

  override def onRecognized(recognized: RecognitionResult) {
    recognized.best.label match {
      case GrammarIds.CREATE_CLASS => Controllers.classController.startClassCreation()
     }
  }
}
