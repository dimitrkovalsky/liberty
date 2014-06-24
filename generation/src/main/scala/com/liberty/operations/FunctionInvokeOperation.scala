package com.liberty.operations

/**
 * User: Dimitr
 * Date: 07.11.13
 * Time: 10:24
 */
// TODO: creation variables without expressions like :<<< List<Integer> list; >>>
case class FunctionInvokeOperation(functionName: String, var params: List[Expression] = Nil,
                                   result: Option[Variable] = None) extends Operation {
  override def execute(): Option[String] = {
    executeNamed(functionName)
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
