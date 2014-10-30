package com.liberty.common

import com.liberty.processing.Operation

/**
 * User: Dimitr
 * Date: 16.03.14
 * Time: 10:47
 */


case class Zone(zoneId: Int, operation: Operation) {
  def run(): Boolean = {
    operation.executeOperation()
  }
}

object Zone extends Enumeration {
  val NOT_STARTED = Value("NOT_STARTED")
  val PREPARING = Value("PREPARING")
  val PROCESSING = Value("PROCESSING")
  val COMPLETED = Value("COMPLETED")
  val FAILED = Value("FAILED")
}

case class ZoneStatus(zoneId: Int, status: Zone.Value = Zone.NOT_STARTED)
