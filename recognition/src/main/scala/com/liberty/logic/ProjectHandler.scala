package com.liberty.logic

import com.liberty.common.GrammarGroups
import com.liberty.controllers.MavenProjectController
import com.liberty.entities.RecognitionResult
import com.liberty.transmission.TransmissionManager

class ProjectHandler extends Handler {

  import com.liberty.common.GrammarIds._

  private var controller: Option[MavenProjectController] = None

  override def onRecognized(recognized: RecognitionResult) = {
    recognized.grammar match {
      case GrammarGroups.PROJECT_CREATION =>
        recognized.best.label match {
          case CREATE_PROJECT => controller = Some(new MavenProjectController)
            controller.foreach(_.startProjectCreation())
          case _ =>
        }

      case GrammarGroups.PROJECT_NAMES =>
        controller.foreach { c => c.setProjectName(recognized.best.sentence); c.createProject()}


    }
  }
}

