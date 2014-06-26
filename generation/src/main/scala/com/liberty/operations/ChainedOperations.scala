package com.liberty.operations

/**
 * Created by Dmytro_Kovalskyi on 26.06.2014.
 */
case class ChainedOperations(operations: Operation*) extends Operation {
  override def execute(): Option[String] = {
    Some(operations.flatMap(_.execute()).mkString("."))
  }
}
