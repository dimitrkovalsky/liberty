package com.liberty.operations

import com.liberty.operations.FunctionType.FunctionType

/**
 * User: Dimitr
 * Date: 07.11.13
 * Time: 10:28
 */
class SelfFunctionInvokeOperation(functionType: FunctionType, name: String = "",
                                  var parameters: List[Expression] = Nil, functionResult: Option[Variable] = None)
  extends FunctionInvokeOperation(name, parameters, functionResult) {
  override def execute(): Option[String] = {
    functionType match {
      case FunctionType.USUAL_FUNCTION => super.execute()
      case FunctionType.SELF_FUNCTION => super.executeNamed("this." + name)
      case FunctionType.SUPER_FUNCTION => super.executeNamed("super." + name)
      case FunctionType.SELF_CONSTRUCTOR => super.executeNamed("this")
      case FunctionType.SUPER_CONSTRUCTOR => super.executeNamed("super")
    }
  }
}

object FunctionType extends Enumeration {
  type FunctionType = Value
  val SELF_FUNCTION, SELF_CONSTRUCTOR, SUPER_FUNCTION, SUPER_CONSTRUCTOR, USUAL_FUNCTION = Value
}

object SelfFunctionInvokeOperation {
  def apply(functionType: FunctionType, name: String = "", parameters: List[Expression] = Nil,
            functionResult: Option[Variable] = None) = new SelfFunctionInvokeOperation(functionType, name, parameters, functionResult)
}
