package liberty.transmission

import liberty.logic.VoiceHandler
import liberty.helpers.JsonMapper
import liberty.loaders.{SharpLoader, DictionaryLoader}


object TransmissionManager {
    final val DATA_PORT = 5555
    var connected = false

    private var worker: DataWorker = null
    private val voiceHandler = new VoiceHandler()

    private val jsonMapper = JsonMapper.getMapper

    def startDataTransmission() {
        worker = new DataWorker(dataReceived)
        new Thread(worker).start()
        while (!TransmissionManager.connected) {
            Thread.sleep(10)
        }

        DictionaryLoader.loadDictionary()
        TransmissionManager.sendData(DataPacket(RequestType.START_RECOGNITION))
        println("Recognition started...")
    }

    def testWriteStream() {
        sendData(DataPacket(RequestType.SCALA_SERVER_STARTED, "Scala server received started data"))
        connected = true
    }

    private def dataReceived(data: String) {
        try {
            val packet = jsonMapper.readValue(data, classOf[DataPacket])
            packet.requestType match {
                case RequestType.SHARP_CLIENT_STARTED => testWriteStream()
                case RequestType.RECOGNITION_RESULT => voiceHandler.handleRecognitionResult(packet)
                case _ => println("[TransmissionManager] unknown requestType : " + packet)
            }
        } catch {
            case e: IllegalArgumentException => System.err.println("Can't deserialize data : " + data)
        }
    }

    def sendData(data: DataPacket) {
        if (connected)
            worker.writeData(jsonMapper.writeValueAsString(data))
    }
}
