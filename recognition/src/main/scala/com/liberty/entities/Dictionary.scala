package com.liberty.entities

import java.util

import scala.beans.BeanProperty

case class Grammar(@BeanProperty var label: Int, @BeanProperty var command: String)

case class Dictionary(grammars: Map[Int, List[Grammar]]) {


}
