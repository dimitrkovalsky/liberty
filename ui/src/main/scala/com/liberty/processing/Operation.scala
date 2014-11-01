package com.liberty.processing

import com.liberty.utills.Logger
import com.liberty.common.TemperatureBounds

/**
 * User: Dimitr
 * Date: 16.03.14
 * Time: 11:22
 */
abstract class Operation(operationName: String, zoneId: Int) {
  /**
   * Encapsulates operation in TP
   * @return Operation completed ?
   */
  protected def execute(): Boolean

  def executeOperation(): Boolean = {
    val start = System.currentTimeMillis()
    Logger.log(s"$operationName started ", zoneId)
    val result = execute()
    val msg = result match {
      case true => s"$operationName finished succesfull"
      case false => s"$operationName failed"
    }
    Logger.log(s"$msg => in ${System.currentTimeMillis() - start} millis")
    result
  }

  def complete(){}
}






