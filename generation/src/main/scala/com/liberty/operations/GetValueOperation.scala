package com.liberty.operations

/**
 * Created by Dmytro_Kovalskyi on 26.06.2014.
 */
case class GetValueOperation(obj: String, methodName: String) extends Operation {
  def execute(): Option[String] = {
    Some(s"${obj}.$methodName()")
  }
}
