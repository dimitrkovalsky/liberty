package com.liberty.processing

import com.liberty.common.{TemperatureComponent, TemperatureBounds, Zone, ZoneStatus}
import com.liberty.common.Zone._
import com.liberty.controllers.TPController

/**
 * User: Dimitr
 * Date: 16.03.14
 * Time: 13:03
 */
object TPModel {
  private var dryingStageStatus = Zone.NOT_STARTED
  var zones: Map[Int, ZoneStatus] = Map.empty
  var temperatures: Map[Int, TemperatureComponent] = Map.empty
  var controller: TPController = _

  def initialize(list: List[Zone], tp: TPController) {
    controller = tp
    list.foreach(z => zones += z.zoneId -> ZoneStatus(z.zoneId))

    list.foreach(zone => {
      zone.operation match {
        case op: DryingBricks =>
          temperatures += zone.zoneId -> TemperatureComponent(op.bounds.average(), op.bounds)
          controller.setBounds(zone.zoneId, op.bounds)
        case _ =>
      }
    })
    println(temperatures)
  }

  def checkTemperature(zoneId: Int): Int = {
    temperatures.get(zoneId).map {
      component => component.simulateTemperatureChange()
        component.current
    }.getOrElse(0)
  }

  def setZoneStatus(zoneId: Int, status: Zone.Value) {
    zones.get(zoneId).foreach {
      st => zones += zoneId -> ZoneStatus(zoneId, status)
    }
  }

  def increaseTemperature(zoneId: Int, delta: Int) {
    temperatures.get(zoneId).foreach {
      c => c.current = c.current + delta
        controller.changeCurrentTemperature(zoneId, c.current, delta)
    }
  }

  def decreaseTemperature(zoneId: Int, delta: Int) {
    temperatures.get(zoneId).foreach {
      c => c.current = c.current - delta
        controller.changeCurrentTemperature(zoneId, c.current, -delta)
    }
  }

  def updateTemperature(zoneId: Int, current: Int) {
    temperatures.get(zoneId).foreach {
      c => c.current = current
        controller.changeCurrentTemperature(zoneId, c.current, 0)
    }
  }

  def getCurrentTemperature(zoneId: Int) = {
    temperatures.get(zoneId).map {
      component => component.current
    }.getOrElse(0)
  }

  def changeStageStatus(stageId: Int, status: Zone.Value) {
    controller.changeStageStatus(stageId, status)
  }

  def setDryingStatus(status: Zone.Value) {
    import Zone._
    if (dryingStageStatus == COMPLETED)
      return
    if (dryingStageStatus != status)
      status match {
        case PREPARING if dryingStageStatus == NOT_STARTED =>
          dryingStageStatus = PREPARING
          controller.changeStageStatus(3, dryingStageStatus)
        case PROCESSING if dryingStageStatus == PREPARING =>
          dryingStageStatus = PROCESSING
          controller.changeStageStatus(3, dryingStageStatus)
        case COMPLETED =>
          dryingStageStatus = COMPLETED
          controller.changeStageStatus(3, dryingStageStatus)
        case _ =>
      }
  }

  def logHistory(msg: String) {
    controller.logHistory(msg)
  }

  def changeZoneStatus(zoneId: Int, status: Zone.Value) {
    controller.changeZoneStatus(zoneId, status)
  }

}

