package com.liberty.transmission

import java.net.InetSocketAddress

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.util.ByteString
import com.codahale.jerkson.Json
import com.liberty.common.{GrammarGroups, GrammarIds}
import com.liberty.entities.{Dictionary, Grammar, RecognitionResult}
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
      Thread.sleep(2)
    }

    val dictionary =   DictionaryLoader.loadDictionary()
    TransmissionManager.sendData(new DataPacket(RequestType.LOAD_DICTIONARY, dictionary))

    TransmissionManager.sendData(DataPacket(RequestType.START_RECOGNITION))
    // Thread.sleep(1000)
    synthesize("Recognition started")
    println("Recognition started...")
    activateGrammar(1)
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
        case RequestType.RECOGNITION_RESULT =>
          val recognized = JsonMapper.getMapper.convertValue(packet.getData, classOf[RecognitionResult])
          voiceHandler.handleRecognitionResult(recognized)
        case _ => println("[TransmissionManager] unknown requestType : " + packet)
      }
    } catch {
      case e: IllegalArgumentException =>
        System.err.println("Can't deserialize data : " + data)
        System.err.println("Error: " + e.getMessage)
    }
  }

  def synthesize(phrase: String): Unit = {
    sendData(DataPacket(RequestType.SYNTHESIZE, phrase))
  }

  def activateGrammar(grammarId: Int) {
    sendData(DataPacket(RequestType.CHANGE_ACTIVE_GRAMMAR, grammarId.toString))
  }

  def sendData(data: DataPacket) {
    try {
      val string = Json.generate(data) //jsonMapper.writeValueAsString(data)
      worker ! ByteString(string + PACKET_END)
      println("SENT: " + string)
    } catch {
      case e: Exception => onException(e)
    }
  }
}
