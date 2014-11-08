package com.liberty.helpers

import com.liberty.entities.{ComplexGrammar, Grammar, RecognitionResult}


object GrammarRegistry {
  var grammars: Map[Int, ComplexGrammar] = Map()

  def addGrammar(grammar: ComplexGrammar) {
    grammars += grammar.label -> grammar
  }

  def getGrammar(id: Int) = grammars.get(id)
}
