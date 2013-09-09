package liberty.logic

import liberty.transmission.DataPacket
import liberty.helpers.JsonMapper
import liberty.entities.RecognitionResult
import liberty.generic.VoiceNotifier
import scala.collection.immutable.Stack


/**
 * User: dkovalskyi
 * Date: 12.07.13
 * Time: 15:54
 */
class VoiceHandler {
    val subscribers: Stack[VoiceNotifier] = Stack.empty[VoiceNotifier]
    subscribers.push(new FunctionHandler)

    def handleRecognitionResult(packet: DataPacket) {
        try {
            val recognized = JsonMapper.getMapper.convertValue(packet.getData, classOf[RecognitionResult])

            //println("Recognized : " + recognized)
        } catch {
            case e: Throwable => System.err.println("[VoiceHandler] handleRecognitionResult error : ", e)
        }
    }
}
