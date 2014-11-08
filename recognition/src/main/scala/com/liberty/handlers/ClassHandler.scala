package com.liberty.handlers

import com.liberty.common.{GrammarGroups, GrammarIds}
import com.liberty.controllers.Controllers
import com.liberty.entities.RecognitionResult
import com.liberty.common.Implicits._
import com.liberty.helpers.GrammarRegistry

/**
 * Created by Maxxis on 11/8/2014.
 */
class ClassHandler extends Handler {

  override def onRecognized(recognized: RecognitionResult) = recognized.best.label match {
    case GrammarGroups.CLASS_NAMES => Controllers.classController.createClass(recognized.best.sentence.removeSpaces())
    case GrammarGroups.CLASS_FIELD_CREATION =>
      //TODO: create field return Either[String,String]
      GrammarRegistry.getGrammar(recognized.best.label).
        fold(println(s"Can't find grammar id ${recognized.best.label}"))(Controllers.classController.createField)
  }
}
