package com.liberty.operations

/**
 * Created by Dmytro_Kovalskyi on 26.06.2014.
 */
case class ChainedOperations(result: Option[Variable], operations: Operation*) extends Operation {
  override def execute(): Option[String] = {
    val res = operations.flatMap(_.execute()).mkString(".")
    result match {
      case None => Some(res)
      case Some(v) => Some(s"${v.name} = $res")
      case _ => None
    }
  }
}
