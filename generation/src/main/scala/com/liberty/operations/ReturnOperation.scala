package com.liberty.operations

/**
 * User: Dimitr
 * Date: 07.11.13
 * Time: 10:26
 */
case class ReturnOperation(expression: Expression) extends Operation {
  override def execute(): Option[String] = {
    val result = expression match {
      case v: Variable => Some(v.toString)
      case v: Value => Some(v.toString)
      case op: Operation => op.execute()
    }

    result match {
      case Some(res: String) => Some(s"return $res")
      case _ => None
    }
  }
}
