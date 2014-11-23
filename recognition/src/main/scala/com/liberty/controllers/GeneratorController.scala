package com.liberty.controllers

import com.liberty.common._
import com.liberty.model.JavaClass
import com.liberty.traits.Writer
import com.liberty.writers.FileClassWriter


trait Controller

/**
 * Contains writer and map for model storing
 * Created by Dmytro_Kovalskyi on 24.09.2014.
 */
trait GeneratorController extends Controller {
  this: GeneratorSubscriber =>
  protected val writer: Writer = new FileClassWriter(ProjectConfig.projectPath)
  protected var activeModel: Option[String] = None
  /**
   * List of managed models
   */
  protected val models = scala.collection.mutable.Map[String, JavaClass]()

  def notifyClassChanged(clazz: JavaClass): Unit = {
    println("Changed class : " + clazz)
    Register.changeModel(clazz)
    notify(Topics.USER_NOTIFICATION, ClassEditAction(clazz))
  }

  /**
   * Indicates that active model is present in controller and component can be created using this model
   * @return
   */
  def isModelActive: Boolean = activeModel.isDefined

  /**
   * Handles creation element command. Checks active model, generates components and notifies UI
   * @param creator
   */
  protected def performCreation(creator: JavaClass => Option[String]): Option[String] = {
    activeModel match {
      case Some(modelName) =>
        Register.getModel(modelName) match {
          case Some(clazz) =>
            val result = creator(clazz)
            result.foreach(r => notify(Topics.USER_NOTIFICATION,
              UserNotificationAction(NotificationType.GENERATION_COMPLETED, Right(r))))
            result
          case _ => None
        }
      case _ => None
    }
  }
}
