package com.liberty.controllers

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
  val notificationController = new NotificationController
}
