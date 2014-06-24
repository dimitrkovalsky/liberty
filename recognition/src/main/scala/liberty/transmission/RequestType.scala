package liberty.transmission

import scala.beans.BeanProperty

case class DataPacket(@BeanProperty var requestType: Int, @BeanProperty var data: Object = null) {
  def this() = this(0, null)

  def this(rt: Int) = this(rt, null)
}

object RequestType {
  final val SCALA_SERVER_STARTED = 1
  final val SHARP_CLIENT_STARTED = 2
  final val LOAD_DICTIONARY: Int = 10
  final val ADD_TO_DICTIONARY: Int = 11
  final val START_RECOGNITION: Int = 15
  final val RECOGNITION_RESULT: Int = 20
}
