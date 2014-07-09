package com.liberty.traits

import com.liberty.model.JavaClass

import scala.util.Try

/**
 * Created by Dmytro_Kovalskyi on 09.07.2014.
 */
trait Changeable {
  /**
   * Notify that entity was changed
   * @param clazz changes class instance
   * @return Message from controller about changed entity
   */
  def changed(clazz: JavaClass): Try[String]
}
