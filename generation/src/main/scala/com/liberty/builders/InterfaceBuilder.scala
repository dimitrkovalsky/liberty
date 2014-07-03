package com.liberty.builders

import com.liberty.StubType
import com.liberty.model._
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

object InterfaceBuilder {
  def apply(name: String): InterfaceBuilder = {
    val b = new InterfaceBuilder
    b.setName(name)
    b
  }

  /**
   * Creates builder class with interface from all public methods except constructor and locate interface in the same package as dao class
   * @param name
   * @param fromClass
   * @return
   */
  def apply(name: String, fromClass: JavaClass): InterfaceBuilder = {
    val builder = apply(name)
    builder.addPackage(fromClass.javaPackage)
    fromClass.functions.foreach { f =>
      if (f.signature.isModifierPresent(PublicModifier) && f.signature.name != fromClass.name)
        builder.addFunctionSignature(f.signature)
    }
    builder
  }

  def apply(name: String, functions: List[JavaFunction]): InterfaceBuilder = {
    val builder = apply(name)
    functions.foreach { f =>
      builder.addFunctionSignature(f.signature)
    }
    builder
  }
}
