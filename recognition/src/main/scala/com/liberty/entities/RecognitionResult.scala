package com.liberty.entities

import scala.beans.BeanProperty


case class RecognitionResult(@BeanProperty timeStamp: Long, @BeanProperty grammar: Int,
                             @BeanProperty duration: Long, @BeanProperty scores: Array[SingleResult]) {
  def best: SingleResult = {
    scores.head
  }

  def this() = this(0, 0, 0, Array.empty)
}

case class SingleResult(@BeanProperty label: Int, @BeanProperty confidence: Int,
                        @BeanProperty sentence: String, @BeanProperty tags: String) {
  def this() = this(0, 0, "", "")
}