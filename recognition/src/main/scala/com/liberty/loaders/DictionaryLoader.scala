package com.liberty.loaders

import com.liberty.entities.{Dictionary, Grammar, RecognitionGrammar}
import com.liberty.helpers.GrammarRegistry
import com.liberty.transmission.{DataPacket, RequestType, TransmissionManager}
import com.liberty.types.GrammarType

import scala.io.Source


object DictionaryLoader {
  private final val GRAMMARS_DIRECTORY = "grammars/"
  private final val GRAMMAR_EXTENSION = ".grammar"
  private final val NAMES_DIRECTORY = "names/"
  private final val NAMES_EXTENSION = ".names"
  private var nameLabel = 10000

  def loadDictionary() {
    TransmissionManager.sendData(new DataPacket(RequestType.LOAD_DICTIONARY, loadGrammarFiles()))
    println("[DictionaryLoader] dictionary loaded...")
  }

  private def loadGrammarFiles(): Dictionary = {
    val grammars: List[Grammar] = loadGrammarFile("functions", GrammarType.FUNCTION_GRAMMAR) :::
      loadGrammarFile("types", GrammarType.TYPE_GRAMMAR) ::: loadGrammarFile("commands", GrammarType.FUNCTION_GRAMMAR) ::: loadNamesFile("function") ::: Nil
    println("[DictionaryLoader] " + grammars.size + " grammars loaded")

    val dictionary = new Dictionary()
    dictionary.setGrammar(grammarToRecognitionGrammar(grammars))
    dictionary
  }


  def nameStringToGrammar(string: String): Grammar = {
    val grammar = Grammar(nameLabel, string)
    nameLabel += 1
    grammar
  }

  private def loadNamesFile(path: String): List[Grammar] = {
    var result: List[Grammar] = Nil
    for (line <- Source.fromFile(NAMES_DIRECTORY + path + NAMES_EXTENSION).getLines())
      if (!line.isEmpty && !line.startsWith("//"))
        result = result ::: List(nameStringToGrammar(line))
    GrammarRegistry.addName(result)
    result
  }

  private def loadGrammarFile(path: String, grammarType: Int): List[Grammar] = {
    var result: List[Grammar] = Nil
    for (line <- Source.fromFile(GRAMMARS_DIRECTORY + path + GRAMMAR_EXTENSION).getLines())
      if (!line.startsWith("//"))
        result = result ::: List(stringToGrammar(line, grammarType))

    println("[DictionaryLoader] " + result.size + " elements loaded from " + path)
    result
  }

  private def stringToGrammar(line: String, grammarType: Int): Grammar = {
    val spaceIndex = line.indexOf(" ")
    Grammar(line.substring(0, spaceIndex).toInt, line.substring(spaceIndex), grammarType)
  }

  private def grammarToRecognitionGrammar(grammar: Grammar) = RecognitionGrammar(grammar.label, grammar.command)

  private def grammarToRecognitionGrammar(grammars: List[Grammar]): List[RecognitionGrammar] = {
    var result: List[RecognitionGrammar] = Nil
    for (grammar <- grammars)
      result = result ::: List(RecognitionGrammar(grammar.label, grammar.command))
    result
  }
}
