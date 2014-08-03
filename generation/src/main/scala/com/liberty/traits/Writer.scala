package com.liberty.traits

import com.liberty.model.{XmlFile, JavaClass, JavaInterface}
import scala.xml.Elem

/**
 * Created by Dmytro_Kovalskyi on 07.07.2014.
 */
/**
 * Unifies writing of Java classes
 */
trait Writer {
  def writeToMetaInf(file: XmlFile)

  def write(clazz: JavaClass)

  def write(interface: JavaInterface)
}
