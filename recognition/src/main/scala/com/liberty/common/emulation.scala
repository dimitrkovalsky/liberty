package com.liberty.common

import java.lang

import com.codahale.jerkson.Json
import com.liberty.entities._
import com.liberty.helpers.GrammarRegistry
import com.liberty.transmission.{TransmissionManager, DataPacket, RequestType}
import com.liberty.types.DataType

import scala.util.Random

package object emulation {

  protected[emulation] sealed trait EmulatedAction

  implicit class EmulateIdImplicit(id: Int) extends EmulatedAction {
    /**
     * Send voice notification after timeout
     * @param timeout millis
     */
    def *>(timeout: Long) {
      VoiceEmulator.addAction(new GrammarWithTimeout(id, timeout))
    }

    /**
     * Send voice notification
     */
    def *>() {
      VoiceEmulator.addAction(this) //emulateRecognized(id)
    }

    /**
     * Uses to set appropriate name
     * @param name
     * @return
     */
    def :>(name: String) = {
      new GrammarWithCommand(id, name).*>()
    }

    def :>>(name: String) = {
      new CartesianProductionBase(name)
    }

    protected[emulation] def emulate(): Unit = {
      VoiceEmulator.emulateRecognized(id)
    }
  }

  implicit class ChangeActiveGrammar(activate: Int) extends EmulatedAction {
    protected def add(): Unit = {
      VoiceEmulator.addAction(this)
    }

    def <*(timeout: Long = 0): Unit = {
      ChangeGrammarWithTimeout(activate, timeout).add()
    }
  }

  protected[emulation] class CartesianProductionBase(name: String) extends EmulatedAction {

    protected def add(): Unit = {
      VoiceEmulator.addAction(this)
    }

    def x(dataType: DataType): Unit = {
      CartesianProductionFull(name, dataType).add()
    }
  }

  case class CartesianProductionFull(name: String, dataType: DataType) extends CartesianProductionBase(name)

  case class ChangeGrammarWithTimeout(activate: Int, timeout: Long) extends ChangeActiveGrammar(activate)

  case class GrammarWithTimeout(id: Int, timeout: Long) extends EmulateIdImplicit(id)

  case class GrammarWithCommand(id: Int, command: String) extends EmulateIdImplicit(id)


  /**
   * User: dimitr
   * Date: 15.11.2014
   * Time: 22:00
   */
  object VoiceEmulator {
    private var activeGrammar = 0
    var enabled = false
    // represents grammars like grammarGroup -> (grammarId -> List[grammars])
    private var grammars: Map[Int, Map[Int, List[Grammar]]] = Map.empty
    private var actions: List[EmulatedAction] = Nil

    protected[emulation] def addAction(act: EmulatedAction) {
      actions = actions ::: List(act)
    }

    /**
     * Should be invoked before transmition
     * @param f
     */
    def emulate(f: => Unit) {
      f
    }

    private def onLoadDictionary(dictionary: Dictionary) = {
      for ((id, group) <- dictionary.grammars) {
        grammars += id -> group.groupBy(_.label)
      }
      println("[EMULATOR] Dictionary loaded")
      println(grammars)
    }

    def receive(packet: DataPacket): Unit = {
      try {
        packet.requestType match {
          case RequestType.SYNTHESIZE => println("[EMULATOR] SYNTHESIZE : " + packet.data)
          case RequestType.CHANGE_ACTIVE_GRAMMAR => println("[EMULATOR] active grammar changed to : " + packet.data)
            activeGrammar = Integer.parseInt(packet.data.toString)
          case RequestType.LOAD_DICTIONARY => onLoadDictionary(packet.data.asInstanceOf[Dictionary])
          case RequestType.START_RECOGNITION =>
            send(DataPacket(RequestType.RECOGNITION_STARTED))
            startEmulation()
          case _ => println("[EMULATOR] unhandled packet : " + packet)
        }
      } catch {
        case e: Throwable => System.err.println("[EMULATOR] error : " + e.getMessage)
      }
    }

    private def send(packet: DataPacket): Unit = {
      if (enabled)
        TransmissionManager.dataReceived(Json.generate(packet))
      else
        System.err.println("[EMULATOR] emulation is disabled")

    }

    protected[emulation] def emulateRecognized(grammarId: Int) {
      grammars.get(activeGrammar) match {
        case None => println("[Emulator] there are no grammars in grammar group : " + activeGrammar)
        case Some(map) =>
          map.get(grammarId) match {
            case None => println("[Emulator] grammar with id : " + grammarId + " is missing in grammar group : " + activeGrammar)
            case Some(ls) => val grammar = ls(Random.nextInt(ls.size))
              sendRecognized(grammar)
          }
      }
    }


    protected[emulation] def emulateRecognized(grammarId: Int, command: String) {
      grammars.get(activeGrammar) match {
        case None => println("[Emulator] there are no grammars in grammar group : " + activeGrammar)
        case Some(map) =>
          map.get(grammarId) match {
            case None => println("[Emulator] grammar with id : " + grammarId + " is missing in grammar group : " + activeGrammar)
            case Some(ls) =>
              val grammar = ls(Random.nextInt(ls.size))
              sendRecognized(grammar.copy(command = command))
          }
      }
    }

    private def sendRecognized(grammar: Grammar) = {
      if (enabled) {
        val packet = DataPacket(RequestType.RECOGNITION_RESULT, RecognitionResult(System.currentTimeMillis(),
          activeGrammar, Random.nextLong(), Array(SingleResult(grammar.label, Random.nextInt(149) - 50, grammar.command, "Emulator tag"))))
        TransmissionManager.dataReceived(Json.generate(packet))
      }
      else
        System.err.println("[EMULATOR] emulation is disabled")
    }

    private def startEmulation(): Unit = {
      new Thread(new Runnable {
        override def run(): Unit = {
          println("[EMULATOR] emulation started")
          for (a <- actions) {
            a match {
              case GrammarWithCommand(id, command) => VoiceEmulator.emulateRecognized(id, command)
              case GrammarWithTimeout(id, timeout) =>
                Thread.sleep(timeout)
                VoiceEmulator.emulateRecognized(id)
              case ChangeGrammarWithTimeout(activate, timeout) =>
                if (timeout != 0)
                  Thread.sleep(timeout)
                activeGrammar = activate
                println("[EMULATOR] active grammar changed to : " + activate)

              case c: CartesianProductionFull => emulateCartesianProduction(c)
              case a: Any if a.isInstanceOf[EmulateIdImplicit] => a.asInstanceOf[EmulateIdImplicit].emulate()
              case _ => println("[EMULATOR] unhandled action")
            }
          }
        }
      }).start()
    }

    private def emulateCartesianProduction(c: CartesianProductionFull): Unit = {
      def findGrammar(): Option[ComplexGrammar] = {
        for (cg <- GrammarRegistry.grammars.values) {
          for (n <- cg.grammars.get(1); t <- cg.grammars.get(2)) {
            if ((t.command.toLowerCase == c.dataType.getTypeName.toLowerCase && n.command.toLowerCase == c.name.toLowerCase) ||
              (n.command.toLowerCase == c.dataType.getTypeName.toLowerCase && t.command.toLowerCase == c.name.toLowerCase)) {
              return Some(cg)
            }
          }
        }
        None
      }

      findGrammar() match {
        case Some(cg) =>
          VoiceEmulator.emulateRecognized(cg.label)
        case None => System.err.println("[EMULATOR] can't find grammar for" + c)
      }
    }
  }

}
