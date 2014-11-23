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
  private val daoController = Controllers.daoController
  private val restController = Controllers.restController
  private val mavenController = Controllers.mavenController

  override def onRecognized(recognized: RecognitionResult) {
    recognized.best.label match {
      case GrammarIds.CREATE_DAO => println("[ComponentHandler] CREATE_DAO"); daoController.createDao()
      case GrammarIds.CREATE_CLASS => classController.startClassCreation()
      case GrammarIds.CREATE_REST => restController.createRest()
      case GrammarIds.CREATE_REST => restController.createRest()
      case GrammarIds.CREATE_BEAN =>
        if (beanController.isModelActive)
          beanController.createBean()
        else
          System.err.println("[ComponentHandler] can not create bean") // TODO: show list to choose model

      case GrammarIds.BUILD => mavenController.build()
      case GrammarIds.DEPLOY => mavenController.deploy()
      case GrammarIds.BUILD_AND_DEPLOY => mavenController.buildAndDeploy()
      case GrammarIds.OPEN_BROWSER => mavenController.openBrowser()
      case GrammarIds.RUN_MONGO => mavenController.runMongo()

      case _ => System.err.println("[ComponentHandler] can't find handler for id : " + recognized.best.label)
    }
  }
}
