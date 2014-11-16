package com.liberty.transmission

import java.net.InetSocketAddress

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.util.ByteString
import com.codahale.jerkson.Json
import com.liberty.common.emulation.VoiceEmulator
import com.liberty.common.{GrammarGroups, Actors}
import com.liberty.controllers.GrammarController
import com.liberty.entities.{SingleResult, Grammar, Dictionary, RecognitionResult}
import com.liberty.helpers.JsonMapper
import com.liberty.loaders.DictionaryLoader
import com.liberty.handlers.VoiceHandler

import scala.util.Random


object TransmissionManager {
  private final val DATA_PORT = 5555
  private final val LOCALHOST = "localhost"
  private final val PACKET_END = "EEENNNDDD"
  var connected = false
  private val voiceHandler = new VoiceHandler()
  private val jsonMapper = JsonMapper.getMapper
  private val system = Actors.actorSystem
  private val endpoint = new InetSocketAddress(LOCALHOST, DATA_PORT)
  private var worker: ActorRef = null
  private var emulate = false

  def startDataTransmission(emulate: Boolean = false) {
    this.emulate = emulate
    if (!emulate) {
      worker = system.actorOf(Props(new DataWorker(endpoint, dataReceived, onError)))

      while (!TransmissionManager.connected) {
        Thread.sleep(2)
      }

    } else {
      VoiceEmulator.enabled = true
      print("[Emulator] ")
      onConnected()
    }
    val dictionary = DictionaryLoader.loadDictionary()
    TransmissionManager.sendData(new DataPacket(RequestType.LOAD_DICTIONARY, dictionary))

    TransmissionManager.sendData(DataPacket(RequestType.START_RECOGNITION))


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

  def dataReceived(data: String) {
    try {
      println("RECEIVED : " + data)
      val packet = jsonMapper.readValue(data, classOf[DataPacket])
      packet.requestType match {
        case RequestType.CLIENT_CONNECTED => onConnected()
        case RequestType.RECOGNITION_RESULT =>
          val recognized = JsonMapper.getMapper.convertValue(packet.getData, classOf[RecognitionResult])
          voiceHandler.handleRecognitionResult(recognized)
        case RequestType.RECOGNITION_STARTED =>
          //          GrammarController.changeGrammarGroup(GrammarGroups.CLASS_FIELD_CREATION)
          GrammarController.changeGrammarGroup(GrammarGroups.PROJECT_CREATION)
          synthesize("Recognition started")
          println("Recognition started...")
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
      val string = Json.generate(data)
      println("SENT: " + string)
      if (!emulate) {
        worker ! ByteString(string + PACKET_END)
      } else {
        VoiceEmulator.receive(data)
      }
    } catch {
      case e: Exception => onException(e)
    }
  }
}


