package com.liberty.helpers

import com.liberty.entities.{Grammar, RecognitionResult}


object GrammarRegistry {
  var grammars: Map[Int, Grammar] = Map()
  var names: List[Grammar] = Nil

  def addGrammar(grammar: Grammar) {
    grammars += grammar.label -> grammar
  }

  def getGrammar(recognized: RecognitionResult): Grammar = grammars(recognized.getBest.label)

  def addName(name: Grammar) {
    names = names ::: List(name)
  }

  def addName(list: List[Grammar]) {
    list.foreach(addName)
  }

  def isName(recognized: RecognitionResult): Boolean = {
    names.foreach(x => if (x.label == recognized.getBest.label) return true)
    false
  }
}
