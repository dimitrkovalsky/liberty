package com.liberty.traits

import com.liberty.builders.FunctionBuilder
import com.liberty.model.{JavaField, _}
import com.liberty.operations.{ReturnOperation, SetValueOperation, Variable}

/**
 * User: Dimitr
 * Date: 31.10.13
 * Time: 20:11
 */
trait Accessible {

  def addGetters(clazz: JavaClass) {
    for (field: JavaField <- clazz.fields) {
      addGetter(clazz, field)
    }
  }

  def addSetters(clazz: JavaClass) {
    for (field: JavaField <- clazz.fields) {
      addSetter(clazz, field)
    }
  }


  def addGetter(clazz: JavaClass, field: JavaField) {
    if (!clazz.functionExist(getGetterName(field)))
      clazz.addFunction(createGetter(field))
  }

  def addSetter(clazz: JavaClass, field: JavaField) {
    if (!clazz.functionExist(getSetterName(field)))
      clazz.addFunction(createSetter(field))
  }

  private def getGetterName(field: JavaField): String = "get" + field.name.capitalize

  private def getSetterName(field: JavaField): String = "set" + field.name.capitalize

  private def createGetter(field: JavaField): JavaFunction = {
    val builder = new FunctionBuilder
    builder.setName(getGetterName(field))
    builder.addModifier(PublicModifier)
    builder.setOutputType(field.dataType)
    builder.addOperation(ReturnOperation(Variable(field)))
    builder.getFunction
  }

  private def createSetter(field: JavaField): JavaFunction = {
    val builder = new FunctionBuilder
    builder.setName(getSetterName(field))
    builder.addModifier(PublicModifier)
    builder.addParam(FunctionParameter(field))
    builder.addOperation(SetValueOperation(field, Variable(field)))
    builder.getFunction
  }

  def addAccessors(clazz: JavaClass) {
    addGetters(clazz)
    addSetters(clazz)
  }

  def getAccessible(clazz: JavaClass): JavaClass = {
    val result: JavaClass = clazz.clone().asInstanceOf[JavaClass]
    addGetters(result)
    addSetters(result)
    result
  }
}
