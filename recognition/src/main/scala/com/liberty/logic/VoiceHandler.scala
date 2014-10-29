package com.liberty.logic

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

  def handleRecognitionResult(recognized: RecognitionResult) {
    try {

      println("Recognized : " + recognized.getBest.sentence)
    } catch {
      case e: Throwable => System.err.println("[VoiceHandler] handleRecognitionResult error : ", e)
    }
  }
}
