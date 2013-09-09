package liberty.entities

import scala.beans.BeanProperty

case class RecognitionResult(@BeanProperty var dictation: String = "", @BeanProperty var label: Int = 0, @BeanProperty var confidence: Int = 0) {
    def this() = this("", 0, 0)
}
