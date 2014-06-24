package com.liberty.operations

import com.liberty.model.JavaField

/**
 * User: Dimitr
 * Date: 07.11.13
 * Time: 10:26
 */
case class SetValueOperation(field: JavaField, expression: Expression) extends Operation {
  def execute(): Option[String] = {
    val result = expression match {
      case v: Variable => Some(v.toString)
      case v: Value => Some(v.toString)
      case op: Operation => op.execute()
    }

    result.map {
      expressionResult => Some(s"${field.getInternalName} = $expressionResult;")
    }.getOrElse(None)
  }
}
