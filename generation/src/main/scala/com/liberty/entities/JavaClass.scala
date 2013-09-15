package com.liberty.entities

import com.liberty.types.DataType
import com.liberty.patterns

/**
 * User: Dimitr
 * Date: 15.09.13
 * Time: 10:55
 */
// TODO: Add support function and field changing and removing
// TODO: Add constructor support
// TODO: Add method generation
class JavaClass {
    var name: String = ""
    var functions: List[JavaFunction] = Nil
    var fields: List[JavaField] = Nil

    def addField(field: JavaField) {
        fields = fields ::: List(field)
    }

    def addFunction(function: JavaFunction) {
        functions = functions ::: List(function)
    }

    override def toString: String = {
        val fieldsString = fields.map(_.toClassPart()) match {
            case Nil => ""
            case list: List[String] => list.mkString(";\n\t") + ";"
        }
        val functionsString = functions.map(_.toClassPart()) match {
            case Nil => ""
            case list: List[String] => list.mkString("\n\n")
        }

        patterns.JavaClassPattern(name, fieldsString, functionsString)
    }
}

trait ClassPart {
    def toClassPart(shift: String = "\t"): String
}

case class JavaField(name: String, dataType: DataType, var modifier: Modifier = DefaultModifier, var value: String = "")
    extends ClassPart {

    if (value.isEmpty)
        value = dataType.getDefaultValue

    override def toString: String = s"${
        if (modifier.toString.isEmpty) "" else modifier.toString + " "
    }${dataType.toString} $name = $value"

    override def toClassPart(shift: String = "\t"): String = toString
}