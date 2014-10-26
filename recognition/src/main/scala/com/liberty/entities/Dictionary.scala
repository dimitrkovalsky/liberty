package com.liberty.entities

import java.util

import scala.beans.BeanProperty

case class RecognitionGrammar(@BeanProperty var label: Int, @BeanProperty var command: String)

class Dictionary() {

  @BeanProperty
  val grammars: java.util.List[RecognitionGrammar] = new util.ArrayList[RecognitionGrammar]()

  def setGrammar(list: List[RecognitionGrammar]) {
    list.foreach(grammars.add(_))
  }
}
