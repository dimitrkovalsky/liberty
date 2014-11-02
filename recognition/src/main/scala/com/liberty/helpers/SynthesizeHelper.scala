package com.liberty.helpers

import com.liberty.transmission.TransmissionManager

object SynthesizeHelper {

  def synthesize(phrase: String): Unit = {
    TransmissionManager.synthesize(phrase)
  }

}
