package com.liberty.builders

import com.liberty.StubType
import com.liberty.model.{FunctionSignature, JavaInterface}
import com.liberty.traits.JavaPackage
import com.liberty.types.DataType

/**
 * User: Dimitr
 * Date: 14.09.13
 * Time: 11:50
 */
class InterfaceBuilder {
  private val javaInterface = new JavaInterface

  def setName(name: String) {
    javaInterface.name = name
  }

  def addFunctionSignature(signature: FunctionSignature) {
    javaInterface.addSignature(signature)
  }

  def addPackage(jPackage: JavaPackage) {
    javaInterface.javaPackage = jPackage
  }

  def addGeneric(generic: DataType) {
    javaInterface.addGenericType(generic)
  }

  def addGeneric(generic: String) {
    javaInterface.addGenericType(StubType(generic))
  }

  def getInterface = javaInterface
}
