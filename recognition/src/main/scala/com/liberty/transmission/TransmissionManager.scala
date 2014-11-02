package com.liberty.transmission

import java.lang.Exception
import java.net.InetSocketAddress

import akka.actor.{ActorRef, Props, ActorSystem}
import akka.util.ByteString
import com.codahale.jerkson.Json
import com.liberty.entities.{RecognitionResult, RecognitionGrammar, Dictionary}
import com.liberty.helpers.JsonMapper
import com.liberty.loaders.DictionaryLoader
import com.liberty.logic.VoiceHandler


object GrammarGroups {
  val PROJECT_CREATION = 1
  val CLASS_EDITING = 2
  val COMPONENT_CREATION = 3 // create database, create bean ...


  val PROJECT_NAMES = 100
}

object GrammarIds {
  val CREATE_PROJECT = 1
  val CREATE_PACKAGE = 2
  val CREATE_CLASS = 3
  val ADD_DATABASE = 11
  val CHANGE_DATABASE = 12
  val NAME_OF_PROJECT = 100
}

object TransmissionManager {

  import GrammarIds._

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

    val gr1 = List(RecognitionGrammar(CREATE_PROJECT, "create project"), RecognitionGrammar(CREATE_PROJECT, "new project"),
      RecognitionGrammar(CREATE_PACKAGE, "add package"), RecognitionGrammar(CREATE_PACKAGE, "create package"),
      RecognitionGrammar(CREATE_PACKAGE, "add new package"), RecognitionGrammar(CREATE_PACKAGE, "create new package"),
      RecognitionGrammar(CREATE_PACKAGE, "create package"), RecognitionGrammar(CREATE_CLASS, "add class"), RecognitionGrammar(CREATE_CLASS, "new class"),
      RecognitionGrammar(CREATE_CLASS, "create class"), RecognitionGrammar(CREATE_CLASS, "create new class"))

    val gr2 = List(RecognitionGrammar(ADD_DATABASE, "add database"), RecognitionGrammar(ADD_DATABASE, "connect database"),
      RecognitionGrammar(ADD_DATABASE, "add database support"), RecognitionGrammar(CHANGE_DATABASE, "change database"),
      RecognitionGrammar(CHANGE_DATABASE, "change current database"))

    val gr3 = List(RecognitionGrammar(NAME_OF_PROJECT, "my project"), RecognitionGrammar(NAME_OF_PROJECT, "sample"),
      RecognitionGrammar(NAME_OF_PROJECT, "simple project"), RecognitionGrammar(NAME_OF_PROJECT, "test"),
      RecognitionGrammar(NAME_OF_PROJECT, "test project"))

    val dictionary = new Dictionary(Map(GrammarGroups.PROJECT_CREATION -> gr1, GrammarGroups.COMPONENT_CREATION -> gr2, GrammarGroups.PROJECT_NAMES -> gr3))

    // DictionaryLoader.loadDictionary()
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
