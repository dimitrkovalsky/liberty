package com.liberty.loaders

import java.io.File

import com.liberty.common.{GrammarIds, GrammarGroups}
import com.liberty.entities.{ComplexGrammar, Dictionary, Grammar}
import com.liberty.helpers.GrammarRegistry

import scala.io.Source


object DictionaryLoader {

  import com.liberty.common.GrammarIds._

  private val PROJECT_PATH = getProjectFolder
  private val GRAMMARS_DIRECTORY = PROJECT_PATH + "/recognition/grammars/"
  private val GRAMMAR_EXTENSION = ".grammar"
  private val NAMES_DIRECTORY = PROJECT_PATH + "/recognition/names/"
  private val NAMES_EXTENSION = ".names"
  /**
   * Grammar label in files should be the same as in  GrammarIds
   * @return Dictionary with grammars loaded from files
   */
  def loadDictionary(): Dictionary = {
    val projectCreation = loadGrammarFile("project_creation")
    val componentCreation = loadGrammarFile("component_creation")
    val projectNames = loadNames("project", NAME_OF_PROJECT)
    val classNames = loadNames("class", NAME_OF_CLASS)
    val fieldCreation = loadFieldCreationGrammar()
    val dictionary = new Dictionary(Map(
      GrammarGroups.PROJECT_CREATION -> projectCreation,
      GrammarGroups.COMPONENT_CREATION -> componentCreation,
      GrammarGroups.PROJECT_NAMES -> projectNames,
      GrammarGroups.CLASS_NAMES -> classNames,
      GrammarGroups.CLASS_FIELD_CREATION -> fieldCreation))

    val loaded = dictionary.grammars.values.flatten.size
    println(s"[DictionaryLoader] loaded $loaded grammars")

    dictionary
  }
  private def getProjectFolder = {
    val path = new File(".").getAbsolutePath
    path.substring(0, path.length() - 1)
  }

  /**
   * Loads ComplexGrammar with Map[fieldType, fieldName] into GrammarRegistry
   * @return
   */
  def loadFieldCreationGrammar() = {
    var result: List[Grammar] = Nil
    val fieldNames = loadNames("field", NAME_OF_FIELD)
    val fieldTypes = loadGrammarFile("types")
    var grammarId = START_FIELD_CREATION
    fieldNames.foreach { n =>
      fieldTypes.foreach { t =>
        val g1 = Grammar(grammarId, t.command + " " + n.command)
        val g2 = Grammar(grammarId, n.command + " " + t.command)
        result = result ::: List(g1, g2)
        val complex = ComplexGrammar(grammarId,Map(1 -> t, 2 -> n))
        GrammarRegistry.addGrammar(complex)
        grammarId += 1
      }
    }
    result
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
