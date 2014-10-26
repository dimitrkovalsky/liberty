package com.liberty.transmission

import java.lang.Exception
import java.net.InetSocketAddress

import akka.actor.{ActorRef, Props, ActorSystem}
import akka.util.ByteString
import com.liberty.entities.{RecognitionGrammar, Dictionary}
import com.liberty.helpers.JsonMapper
import com.liberty.loaders.DictionaryLoader
import com.liberty.logic.VoiceHandler


object TransmissionManager {
  private final val DATA_PORT = 5555
  private final val LOCALHOST = "localhost"
  private final val PACKET_END = "EEENNNDDD"
  var connected = false
  private val voiceHandler = new VoiceHandler()
  private val jsonMapper = JsonMapper.getMapper
  private val system = ActorSystem("LibertySystem")
  private val endpoint = new InetSocketAddress(LOCALHOST, DATA_PORT)
  private var worker: ActorRef = null

  def startDataTransmission() {
    worker = system.actorOf(Props(new DataWorker(endpoint, dataReceived, onError)))

    while (!TransmissionManager.connected) {
      Thread.sleep(10)
    }

    val dictionary = new Dictionary()
    dictionary.setGrammar(List(RecognitionGrammar(1,"Sample"),RecognitionGrammar(1,"Example"),
      RecognitionGrammar(2,"Another"), RecognitionGrammar(2,"Else")))
    dictionary
    //DictionaryLoader.loadDictionary()
    TransmissionManager.sendData(new DataPacket(RequestType.LOAD_DICTIONARY, dictionary))

    TransmissionManager.sendData(DataPacket(RequestType.START_RECOGNITION))
   // Thread.sleep(1000)
    synthesize("Recognition started")
    println("Recognition started...")
  }

  def onConnected() {
    println("Scala client connected...")
    connected = true
  }

  private def onError(err: String): Unit = {
    System.err.println("Error : " + err)
  }

  private def onException(e: Exception): Unit = {
    System.err.println("An exception occurred : " + e.getMessage)
  }

  private def dataReceived(data: String) {
    try {
      println("RECEIVED : " + data)
      val packet = jsonMapper.readValue(data, classOf[DataPacket])
      packet.requestType match {
        case RequestType.CLIENT_CONNECTED => onConnected()
        case RequestType.RECOGNITION_RESULT => voiceHandler.handleRecognitionResult(packet)
        case _ => println("[TransmissionManager] unknown requestType : " + packet)
      }
    } catch {
      case e: IllegalArgumentException => System.err.println("Can't deserialize data : " + data)
    }
  }
  
  def synthesize(phrase:String): Unit ={
    sendData(DataPacket(RequestType.SYNTHESIZE, phrase))
  }
  

  def sendData(data: DataPacket) {
    try {
      val string = jsonMapper.writeValueAsString(data)
      worker ! ByteString(string + PACKET_END)
      println("SENT: " + string)
    } catch {
      case e: Exception => onException(e)
    }
  }
}
