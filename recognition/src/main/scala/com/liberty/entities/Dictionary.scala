package com.liberty.entities

import java.util

import scala.beans.BeanProperty

case class RecognitionGrammar(@BeanProperty var label: Int, @BeanProperty var command: String)

case class Dictionary(grammars: Map[Int, List[RecognitionGrammar]]) {


}
