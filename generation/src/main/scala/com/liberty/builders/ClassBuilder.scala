package com.liberty.builders

import com.liberty.model._
import com.liberty.operations.Operation
import com.liberty.traits.JavaPackage
import com.liberty.types.DataType

/**
 * User: Dimitr
 * Date: 15.09.13
 * Time: 10:47
 */
// TODO : Add abstract class support
class ClassBuilder(var javaClass: JavaClass = new JavaClass) {
  private var inStaticSection = false
  private var withStatic: List[Operation] = Nil
  private var inTrySection = false
  private var withTry: List[Operation] = Nil

  def setName(name: String) = javaClass.name = name

  def addFunction(function: JavaFunction) {
    javaClass.addFunction(function)
  }

  def addFunctions(functions: List[JavaFunction]) {
    for (function <- functions)
      javaClass.addFunction(function)
  }

  def addField(field: JavaField) {
    javaClass.addField(field)
  }

  def addAnnotation(annotation: JavaAnnotation) {
    javaClass.addAnnotation(annotation)
  }

  def addPackage(javaPackage: JavaPackage) {
    javaClass.javaPackage = javaPackage
  }

  def addImplements(interface: JavaInterface) {
    javaClass.addImplements(interface)
  }

  def addBlock(block: StaticBlock) {
    javaClass.addBlock(block)
  }

  def addExtend(clazz: JavaClass) {
    javaClass.addExtend(clazz)
  }

  def removeImplements(interfaceName: JavaInterface): Option[JavaInterface] = {
    javaClass.removeImplements(interfaceName)
  }

  def removeExtend(): Option[JavaClass] = {
    javaClass.removeExtend()
  }

  def addGeneric(generic: DataType) {
    javaClass.addGenericType(generic)
  }

  /**
   * Can be used only in static block
   * @param operation
   */
  def addOperation(operation: Operation) {
    if (inStaticSection && !inTrySection)
      withStatic = withStatic ::: operation :: Nil
    if (inTrySection)
      withTry = withTry ::: operation :: Nil
  }

  def static(f: => Unit) = {
    inStaticSection = true
    f
    inStaticSection = false
    val block = new StaticBlock()
    for (o <- withStatic)
      block.addOperation(o)
    addBlock(block)
  }

  def tryable(f: => Unit): ClassTryable = {
    inTrySection = true
    f
    inTrySection = false
    new ClassTryable(this, withTry)
  }

  def getJavaClass: JavaClass = javaClass
}

object ClassBuilder {
  def apply(): ClassBuilder = new ClassBuilder()

  def apply(name: String): ClassBuilder = {
    val b = new ClassBuilder()
    b.setName(name)
    b
  }
}