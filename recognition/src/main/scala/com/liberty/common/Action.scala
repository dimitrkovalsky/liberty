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

case class CreateExceptionClassAction(exception: String, baseException: String = "Exception") extends Action

case class UserNotificationAction(notificationType: Int, result: Either[String, String]) extends Action

case class ClassEditAction(clazz: JavaClass) extends Action

/**
 * Model should be added to Register.addModel before action sending
 * @param name
 */
case class ActivateModel(name: String) extends Action

case class UIAction(uiActionType: Int) extends Action

