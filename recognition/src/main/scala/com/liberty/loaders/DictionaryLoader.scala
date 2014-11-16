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
    val classEdit = loadGrammarFile("class_edit")
    val dictionary = new Dictionary(Map(
      GrammarGroups.PROJECT_CREATION -> projectCreation,
      GrammarGroups.COMPONENT_CREATION -> componentCreation,
      GrammarGroups.PROJECT_NAMES -> projectNames,
      GrammarGroups.CLASS_NAMES -> classNames,
      GrammarGroups.CLASS_EDITING -> classEdit,
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
    val gs = loadNames("field", NAME_OF_FIELD) x loadGrammarFile("types") cartesianProduct START_FIELD_CREATION
    gs.registerGrammars.foreach(GrammarRegistry.addGrammar)
    gs.grammars
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
   * Simple DSL for grammar loading
   * @param ls
   */
  private implicit class ComplexGrammarWrapper(ls: List[Grammar]) {

    def x(another: List[Grammar]): ComplexGrammarTemp = {
      new ComplexGrammarTemp(List(ls, another))
    }
  }

  case class ComplexGrammarContainer(grammars: List[Grammar], registerGrammars: List[ComplexGrammar])

  class ComplexGrammarTemp(lists: List[List[Grammar]]) {
    def x(another: List[Grammar]): ComplexGrammarTemp = {
      new ComplexGrammarTemp(lists ::: List(another))
    }

    /**
     * Cartesian product of two lists
     * @param startGrammarId Initial id for grammars
     */
    def cartesianProduct(startGrammarId: Int) = {
      var result: List[Grammar] = Nil
      var grammarId = startGrammarId

      def product(l1: List[Grammar], l2: List[Grammar]): List[ComplexGrammar] = {
        var complexGrammars: List[ComplexGrammar] = Nil
        for (e1 <- l1; e2 <- l2) {
          val g1 = Grammar(grammarId, e2.command + " " + e1.command)
          val g2 = Grammar(grammarId, e1.command + " " + e2.command)
          result = result ::: List(g1, g2)
          complexGrammars = complexGrammars ::: List(ComplexGrammar(grammarId, Map(1 -> e2, 2 -> e1)))
          grammarId += 1
        }
        complexGrammars
      }
      //TODO: Work only with 2 elements
      val complex = lists match {
        case x :: xs => product(x, xs.head)
        case _ => Nil
      }
      ComplexGrammarContainer(result, complex)
    }
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
