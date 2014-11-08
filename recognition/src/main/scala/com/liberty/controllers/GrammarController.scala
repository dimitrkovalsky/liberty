package com.liberty.controllers

import com.liberty.transmission.TransmissionManager

/**
 * Created by Maxxis on 11/8/2014.
 */
object GrammarController {
  private var activeGrammarGroup: Int = _

  def changeGrammarGroup(grammarGroup: Int): Unit = {
    if (grammarGroup != activeGrammarGroup) {
      activeGrammarGroup = grammarGroup
      TransmissionManager.activateGrammar(grammarGroup)
      println(s"GrammarGroup changed to $activeGrammarGroup")
    } else {
      println(s"GrammarGroup is equal $activeGrammarGroup")
    }
  }
}
