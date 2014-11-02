package com.liberty.logic

import com.liberty.entities.RecognitionResult

trait Handler {
  def onRecognized(recognized: RecognitionResult)
}
