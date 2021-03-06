package com.liberty.transmission

import scala.beans.BeanProperty

case class DataPacket(@BeanProperty var requestType: Int, @BeanProperty var data: Object = null) {
  def this() = this(0, null)

  def this(rt: Int) = this(rt, null)
}

object RequestType {
  final val CLIENT_CONNECTED = 1
  final val LOAD_DICTIONARY = 10
  final val ADD_TO_DICTIONARY = 11
  final val CHANGE_ACTIVE_GRAMMAR = 13
  final val START_RECOGNITION = 15
  final val RECOGNITION_STARTED = 16
  final val RECOGNITION_RESULT = 20
  final val SYNTHESIZE = 30
}

