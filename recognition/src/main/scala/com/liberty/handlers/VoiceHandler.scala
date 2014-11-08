package com.liberty.handlers

import com.liberty.common.GrammarGroups
import com.liberty.entities.RecognitionResult
import com.liberty.generic.VoiceNotifier

import scala.collection.immutable.Stack


/**
 * User: dkovalskyi
 * Date: 12.07.13
 * Time: 15:54
 */
class VoiceHandler {
  val subscribers: Stack[VoiceNotifier] = Stack.empty[VoiceNotifier]
  subscribers.push(new FunctionHandler)
  val projectHandler = new ProjectHandler
  val componentHandler = new ComponentHandler
  val classHandler = new ClassHandler

  def handleRecognitionResult(recognized: RecognitionResult) {
    try {
      println("Recognized : " + recognized.best.sentence)
      recognized.grammar match {
        case GrammarGroups.PROJECT_CREATION | GrammarGroups.PROJECT_NAMES => projectHandler.onRecognized(recognized)
        case GrammarGroups.COMPONENT_CREATION => componentHandler.onRecognized(recognized)
        case GrammarGroups.CLASS_NAMES | GrammarGroups.CLASS_EDITING | GrammarGroups.CLASS_FIELD_CREATION => classHandler.onRecognized(recognized)
        case _ => println("[VoiceHandler] match fail")
      }
    } catch {
      case e: Throwable => System.err.println("[VoiceHandler] handleRecognitionResult error : ", e)
    }
  }
}
