package com.liberty.entities

import scala.beans.BeanProperty


case class Grammar(@BeanProperty var label: Int = 0, @BeanProperty var command: String = "", @BeanProperty var grammarType: Int = 0) {}
