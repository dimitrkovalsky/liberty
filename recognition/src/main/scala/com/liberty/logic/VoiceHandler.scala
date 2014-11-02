package com.liberty.logic

import com.liberty.entities.RecognitionResult
import com.liberty.generic.VoiceNotifier
import com.liberty.transmission.GrammarGroups

import scala.collection.immutable.Stack


/**
 * User: dkovalskyi
 * Date: 12.07.13
 * Time: 15:54
 */
class VoiceHandler {
  val subscribers: Stack[VoiceNotifier] = Stack.empty[VoiceNotifier]
  subscribers.push(new FunctionHandler)
  val handler = new ProjectHandler

  def handleRecognitionResult(recognized: RecognitionResult) {
    try {
      recognized.grammar match {
        case GrammarGroups.PROJECT_CREATION | GrammarGroups.PROJECT_NAMES => handler.onRecognized(recognized)
        case _ => println("[VoiceHandler] match fail")
      }
      println("Recognized : " + recognized.best.sentence)
    } catch {
      case e: Throwable => System.err.println("[VoiceHandler] handleRecognitionResult error : ", e)
    }
  }
}
