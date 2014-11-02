package com.liberty.loaders

import java.io.File

import com.liberty.common.GrammarGroups
import com.liberty.entities.{Dictionary, Grammar}

import scala.io.Source


object DictionaryLoader {

  import com.liberty.common.GrammarIds._

  private val PROJECT_PATH = getProjectFolder
  private val GRAMMARS_DIRECTORY = PROJECT_PATH + "/recognition/grammars/"
  private val GRAMMAR_EXTENSION = ".grammar"
  private val NAMES_DIRECTORY = PROJECT_PATH + "/recognition/names/"
  private val NAMES_EXTENSION = ".names"

  private def getProjectFolder = {
    val path = new File(".").getAbsolutePath
    path.substring(0, path.length() - 1)
  }

  /**
   * Grammar label in files should be the same as in  GrammarIds
   * @return Dictionary with grammars loaded from files
   */
  def loadDictionary(): Dictionary = {
    val projectCreation = loadGrammarFile("project_creation")
    val componentCreation = loadGrammarFile("component_creation")
    val projectNames = loadNames("project", NAME_OF_PROJECT)
    val dictionary = new Dictionary(Map(
      GrammarGroups.PROJECT_CREATION -> projectCreation,
      GrammarGroups.COMPONENT_CREATION -> componentCreation,
      GrammarGroups.PROJECT_NAMES -> projectNames))

    val loaded = dictionary.grammars.values.flatten.size
    println(s"[DictionaryLoader] loaded $loaded grammars")

    dictionary
  }


  private def loadNames(fileName: String, label: Int) = {
    var names: List[Grammar] = Nil
    for (line <- Source.fromFile(NAMES_DIRECTORY + fileName + NAMES_EXTENSION).getLines())
      if (!line.startsWith("//"))
        names = names ::: List(Grammar(label, line))
    println("[DictionaryLoader] " + names.size + " names loaded from " + fileName)
    names
  }

  /**
   * Ignores line that start with "//"
   * @param fileName
   * @return
   */
  private def loadGrammarFile(fileName: String): List[Grammar] = {
    var result: List[Grammar] = Nil
    for (line <- Source.fromFile(GRAMMARS_DIRECTORY + fileName + GRAMMAR_EXTENSION).getLines())
      if (!line.startsWith("//"))
        result = result ::: stringToGrammar(line)

    println("[DictionaryLoader] " + result.size + " grammars loaded from " + fileName)
    result
  }

  /**
   * Ignores phrase if it start with '!'
   * @param line
   * @return
   */
  private def stringToGrammar(line: String): List[Grammar] = {
    val labelIndex = line.indexOf(":")
    val label = line.substring(0, labelIndex).trim.toInt
    val phrases = line.substring(labelIndex + 1).split(",").map(_.trim).filterNot(_.startsWith("!"))
    phrases.map(Grammar(label, _)).toList
  }
}
