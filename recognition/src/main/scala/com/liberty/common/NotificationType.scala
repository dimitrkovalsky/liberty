package com.liberty.common

import scala.beans.BeanProperty

/**
 * User: Dmytro_Kovalskyi
 * Date: 04.11.2014
 * Time: 17:02
 */
object NotificationType {
  val GENERATION_COMPLETED = 1
  val FILES_SAVED = 2
  val CLASS_CHANGED = 3
  val PROCESS_STARTED = 4
  final val PROJECT_CREATED = 4
  final val PROJECT_CREATION_FAILED = 5
}


