package com.liberty.model

import com.liberty.helpers.StringHelper
import com.liberty.traits.{Importable, JavaPackage, LocationPackage, NoPackage}

/**
 * User: Dimitr
 * Date: 10.10.13
 * Time: 10:41
 */
case class JavaAnnotation(name: String = "", pack: JavaPackage = new NoPackage()) extends Importable {
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

  def getParam(value: String): String = {
    if (StringHelper.isNumeric(value))
      return value
    if (StringHelper.isBoolean(value))
      return value
    "\"" + value + "\""
  }

  override def toString: String = {
    val params = if (parameters.isEmpty) ""
    else
      s"(${
        parameters.view.map {
          case (key, value) => key + " = " + getParam(value)
        } mkString (", ")
      })"
    s"@$name$params"
  }


  override def equals(obj: Any): Boolean = {
    if (!obj.isInstanceOf[JavaAnnotation])
      return false
    val annotation: JavaAnnotation = obj.asInstanceOf[JavaAnnotation]

    this.name.equals(annotation.name) && this.javaPackage.equals(annotation.javaPackage)
  }
}

/**
 * Represents annotation with one parameter
 */
class SingleMemberAnnotation(name: String, param: String, pack: JavaPackage, notString: Boolean = false) extends JavaAnnotation(name, pack) {

  override def toString: String = {
    if (notString)
      s"@$name($param)"
    else
      s"@$name(${getParam(param)})"
  }
}

case class SimpleAnnotation(var name: String = "", pack: String)

object SimpleAnnotation {
  def apply(name: String, pack: LocationPackage) = {
    new SimpleAnnotation(name, pack.packagePath)
  }
}
