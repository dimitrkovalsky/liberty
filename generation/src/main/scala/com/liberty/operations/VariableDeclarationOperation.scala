package com.liberty.operations

import com.liberty.types.DataType

/**
 * User: Dimitr
 * Date: 07.11.13
 * Time: 10:26
 */
case class VariableDeclarationOperation(v: Variable, dataType: DataType, expr: Expression = new NoneExpression)
  extends Operation {
  override def execute(): Option[String] = {
    val rightPart = expr match {
      case e: NoneExpression => ""
      case variable: Variable => " = " + variable.toString
      case op: Operation => op.execute() match {
        case Some(s: String) => " = " + s
        case None => return None
      }
    }
    Some(s"${dataType.toString} ${
      v.name
    }$rightPart")
  }

  override def getPackageString: String = super.getPackageString
}
