package com.liberty.operations

import com.liberty.types.ObjectType

/**
 * Created by Dmytro_Kovalskyi on 25.07.2014.
 */
case class StaticFunctionInvokeOperation(obj: ObjectType, functionName: String, params: List[Expression] = Nil,
                                         result: Option[Variable] = None)
  extends Operation {
  override def execute(): Option[String] = {
    executeNamed(obj.classType + "." + functionName)
  }


  protected def getInvokeParams: String = {
    params match {
      case x :: xs => params.mkString(", ")
      case _ => ""
    }
  }

  protected def executeNamed(named: String): Option[String] = {
    val invokeParams = getInvokeParams
    result match {
      case None => Some(s"$named($invokeParams)")
      case Some(v) => Some(s"${v.name} = $named($invokeParams)")
      case _ => None
    }
  }
}
