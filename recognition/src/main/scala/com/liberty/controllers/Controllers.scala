package com.liberty.controllers

import akka.actor.Props
import com.liberty.common.Actors

/**
 * User: Dmytro_Kovalskyi
 * Date: 06.11.2014
 * Time: 12:46
 */
object Controllers {
  val beanController = new BeanController
  val restController = new RestController
  val daoController = new DaoController
  val additionalController = new AdditionalClassController
  val mavenController = new MavenProjectController
  val classController = new ClassController
  // UiNotifier located in ui module
  //val notificationController = new NotificationController(Actors.createActor(classOf[UiActor]))
}
