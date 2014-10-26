package com.liberty.entities

import com.liberty.types.{AccessModifier, RecognitionDataType}

/**
 * User: dkovalskyi
 * Date: 15.07.13
 * Time: 15:20
 */
class FunctionEntity {
  var accessModifier: Int = AccessModifier.NONE
  var name: String = ""
  var returnType: DataTypeEntity = new DataTypeEntity(RecognitionDataType.VOID)
  var input: Array[VariableEntity] = Array.empty[VariableEntity]

  override def toString: String = {
    AccessModifier.toString(accessModifier) + " " + returnType + " " + name + getParameterString + "{}"
  }

  private def getParameterString: String = {
    if (input.isEmpty)
      return "()"
    var result = "("
    for (variable <- input) {
      result += variable.toFunctionParam
    }
    result += ")"
    result
  }
}
