package com.liberty.common

import com.liberty.model._
import com.liberty.operations.{Expression, StringValue, Value, Variable}
import com.liberty.traits.JavaPackage
import com.liberty.types.{ObjectType, SimpleObjectType}

object Implicits {
  implicit def functionParameterToExpression(param: FunctionParameter): Expression = param.paramName

  implicit def simpleAnnotationToJavaAnnotation(annotation: SimpleAnnotation): JavaAnnotation =
    JavaAnnotation(annotation.name, JavaPackage(annotation.pack, annotation.name))

  implicit def variableToOption(v: Variable): Option[Variable] = Some(v)

  implicit def simpleObjectTypeToObjectType(t: SimpleObjectType): ObjectType = ObjectType(t.className, JavaPackage(t.packagePath, t.className))

  implicit def stringToValue(s: String) = new StringValue(s)

  implicit def javaFieldToVariableOption(field: JavaField): Option[Variable] = Some(Variable(field))

  implicit def stringToOption(string: String): Option[String] = Some(string)

  implicit def javaFieldToString(field: JavaField): String = field.name

  implicit def interfaceToObjectType(interface: JavaInterface): ObjectType = ObjectType(interface.name, interface.javaPackage)


  implicit class ClassParam(clazz: JavaClass) {
    def asClassParam: Expression = new Value(s"${clazz.name}.class")
  }

  implicit class StringExtended(s: String) {
    def firstToLowerCase: String = {
      if (s == null) null
      else if (s.length == 0) ""
      else {
        val chars = s.toCharArray
        chars(0) = chars(0).toLower
        new String(chars)
      }
    }

    def firstToUpperCase: String = {
      if (s == null) null
      else if (s.length == 0) ""
      else {
        val chars = s.toCharArray
        chars(0) = chars(0).toUpper
        new String(chars)
      }
    }

    def quotes: String = "\"" + s + "\""

    /**
     * Returns true if string contains all elements from one list passer as args
     * @param args lists
     * @return
     */
    def containsOr(args: List[String]*): Boolean = {
      args.exists(_.filterNot(str => s.contains(str)) == Nil)
    }

    def removeSpaces(): String = {
      s.replace(" ", "")
    }
  }

}
