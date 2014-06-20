package com.liberty.model

import com.liberty.traits.{NoPackage, JavaPackage, Importable}
import com.liberty.helpers.StringHelper

/**
 * User: Dimitr
 * Date: 10.10.13
 * Time: 10:41
 */
case class JavaAnnotation(var name: String = "", pack: JavaPackage = new NoPackage()) extends Importable {
    javaPackage = JavaPackage(pack.packagePath, name)

    def apply(name: String, value: String): JavaAnnotation = {
        addParameter(name, value)
        this
    }

    var parameters: Map[String, String] = Map()

    def addParameter(name: String, value: String) {
        parameters += name -> value
    }

    def changeParameter(name: String, value: String) {
        parameters += name -> value
    }

    override def toString: String = {
        def getParam(value: String): String = {
            if (StringHelper.isNumeric(value))
                return value
            if (StringHelper.isBoolean(value))
                return value
            "\"" + value + "\""
        }
        val params = if (parameters.isEmpty) ""
        else
            s"(${parameters.view.map {
                case (key, value) => key + " = " + getParam(value)
            } mkString (", ")})"
        s"@$name$params"
    }


    override def equals(obj: Any): Boolean = {
        if (!obj.isInstanceOf[JavaAnnotation])
            return false
        val annotation: JavaAnnotation = obj.asInstanceOf[JavaAnnotation]

        this.name.equals(annotation.name) && this.javaPackage.equals(annotation.javaPackage)
    }
}

case class SimpleAnnotation(var name: String = "", pack: String)
