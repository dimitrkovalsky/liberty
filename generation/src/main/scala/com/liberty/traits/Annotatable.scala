package com.liberty.traits

import com.liberty.model.JavaAnnotation

/**
 * User: Dimitr
 * Date: 10.10.13
 * Time: 10:29
 */
trait Annotatable {

  var annotations: List[JavaAnnotation] = Nil

  def addAnnotation(annotation: JavaAnnotation) {
    if (!annotations.contains(annotation))
      annotations = annotations ::: List(annotation)
  }

  protected def annotationsToString(inline: Boolean = false): String = {
    annotations match {
      case Nil => ""
      case _ => if (inline) annotations.mkString(" ") else annotations.mkString("\n\t") + "\n"
    }
  }

  protected def annotationToPostShiftedString(shift: String): String = {
    annotationsToString() match {
      case "" => ""
      case s: String => s + shift
    }
  }

  protected def annotationToShiftedString(shift: String, inline: Boolean = false): String = {
    annotations match {
      case Nil => ""
      case _ => if (inline) shift + annotations.mkString(" ")
      else annotations.map(a => shift + a.toString).mkString("\n") + "\n"
    }
  }
}
