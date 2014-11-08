package com.liberty.handlers

import com.liberty.entities.RecognitionResult

trait Handler {
  def onRecognized(recognized: RecognitionResult)
}
