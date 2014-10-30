package com.liberty.processing

import com.liberty.common.Zone

/**
 * User: Dimitr
 * Date: 16.03.14
 * Time: 11:49
 */
case class FormingBricks(zone: Int) extends Operation("Forming bricks", zone) {
  private val FORMING_TIME = 700

  override def execute() = {
    TPModel.changeStageStatus(zone, Zone.PROCESSING)
    TPModel.setDryingStatus(Zone.PREPARING)
    Thread.sleep(FORMING_TIME)
    TPModel.changeStageStatus(zone, Zone.COMPLETED)
    TPModel.setDryingStatus(Zone.PROCESSING)
    true
  }
}
