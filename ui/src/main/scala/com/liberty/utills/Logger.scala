package com.liberty.utills

import com.liberty.processing.TPModel

/**
 * User: Dimitr
 * Date: 16.03.14
 * Time: 10:50
 */
object Logger {
  def log(msg: String) {
    println(msg)
    TPModel.logHistory(msg)
  }

  def log(msg: String, zoneNumber: Int) {
    val message = s"[Zone # $zoneNumber] $msg => ${System.currentTimeMillis()}"
    println(message)
    TPModel.logHistory(message)
  }

  def logWithTime(msg: String)(implicit zoneNumber: Int) {
    val message = s"[Zone # $zoneNumber] $msg => ${System.currentTimeMillis()}"
    println(message)
    TPModel.logHistory(message)
  }
}
