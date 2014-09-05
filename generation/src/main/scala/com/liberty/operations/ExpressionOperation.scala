package com.liberty.operations

/**
 * Created by Dmytro_Kovalskyi on 05.09.2014.
 */
case class ExpressionOperation(expression: String) extends Operation {
  /**
   * Executes operation
   * @return string representation of operation invoke
   */
  override def execute(): Option[String] = {
    Some(expression)
  }
}
