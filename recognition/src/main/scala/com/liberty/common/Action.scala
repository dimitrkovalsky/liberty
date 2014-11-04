package com.liberty.common

import com.liberty.model.JavaClass

/**
 * User: dimitr
 * Date: 04.11.2014
 * Time: 9:10
 */
sealed trait Action

case object StartProjectCreationAction extends Action

case class CreateProject(projectName: String) extends Action

case class CreateDaoAction(model: JavaClass) extends Action

case class CreateBeanAction(model: JavaClass) extends Action

case class CreateRestAction(model: JavaClass) extends Action

case class UserNotification(notificationType: Int, message: String) extends Action

