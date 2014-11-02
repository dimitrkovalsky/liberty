package com.liberty.logic

import com.liberty.controllers.MavenProjectController
import com.liberty.entities.RecognitionResult
import com.liberty.transmission.{TransmissionManager, GrammarGroups}

class ProjectHandler extends Handler {

  import com.liberty.transmission.GrammarIds._

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

