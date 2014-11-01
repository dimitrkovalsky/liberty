package com.liberty.processing

import com.liberty.common.{Zone, TemperatureBounds}
import com.liberty.utills.Logger
import scala.util.Random
import scala.util

/**
 * User: Dimitr
 * Date: 16.03.14
 * Time: 11:50
 */
case class DryingBricks(bounds: TemperatureBounds, zone: Int) extends Operation("Drying bricks", zone) {

  import TemperatureBounds._

  private implicit val zoneNumber = zone
  private val DRYING_TIME = 10000
  private val CONTROL_PERIOD = 500

  def getCurrentTemperature: Int = {
    TPModel.getCurrentTemperature(zone)
  }

  def increaseTemperature(delta: Int) {
    Logger.logWithTime(s"Increasing temperature on $delta t current value : $getCurrentTemperature")
    TPModel.increaseTemperature(zone, delta)
    Logger.logWithTime(s"Increasing completed current value : $getCurrentTemperature")
  }

  def decreaseTemperature(delta: Int) {
    Logger.logWithTime(s"Decreasing temperature on $delta current value : $getCurrentTemperature")
    TPModel.decreaseTemperature(zone, delta)
    Logger.logWithTime(s"Decreasing completed current value : $getCurrentTemperature")
  }

  def check() {
    val current = TPModel.checkTemperature(zone)
    Logger.logWithTime(s"Check temperature current value : $current")
    Thread.sleep(CONTROL_PERIOD)
    bounds.checkBounds(current) match {
      case LOWER => increaseTemperature(bounds.calculateDelta(current))
      case HIGHER => decreaseTemperature(bounds.calculateDelta(current))
      case NORMAL => TPModel.updateTemperature(zone, current)
    }
  }

  override def complete() {
    TPModel.changeZoneStatus(zone, Zone.COMPLETED)
  }

  override def execute() = {
    TPModel.changeZoneStatus(zone, Zone.PROCESSING)
    TPModel.changeZoneStatus(zone + 1, Zone.PREPARING)
    for (period <- 1 to DRYING_TIME / CONTROL_PERIOD)
      check()
    TPModel.changeZoneStatus(zone, Zone.COMPLETED)
    true
  }
}