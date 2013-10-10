package com.liberty.entities

/**
 * User: Dimitr
 * Date: 10.10.13
 * Time: 10:41
 */
case class JavaAnnotation(var name: String = "") {
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
        val params = if (parameters.isEmpty) ""
        else
            s"(${parameters.view.map {
                case (key, value) => key + " = \"" + value + "\""
            } mkString (", ")})"
        s"@$name$params"
    }
}
