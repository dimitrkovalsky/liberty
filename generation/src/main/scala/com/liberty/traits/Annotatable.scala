package com.liberty.traits

import com.liberty.entities.JavaAnnotation

/**
 * User: Dimitr
 * Date: 10.10.13
 * Time: 10:29
 */
trait Annotatable {

    var annotations: List[JavaAnnotation] = Nil

    def addAnnotation(annotation: JavaAnnotation) {
        annotations = annotations ::: List(annotation)
    }

    protected def annotationToString(inline: Boolean = false): String = {
        annotations match {
            case Nil => ""
            case _ => if (inline) annotations.mkString(" ") else annotations.mkString("\n") + "\n"
        }
    }

    protected def annotationToPostShiftedString(shift: String): String = {
        annotationToString() match {
            case "" => ""
            case s:String => s + shift
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