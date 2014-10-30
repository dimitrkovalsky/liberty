package com.liberty.common

import scala.util.Random

/**
 * User: Dimitr
 * Date: 16.03.14
 * Time: 13:10
 */
case class TemperatureBounds(min: Int, max: Int) {

  import TemperatureBounds._

  def checkBounds(temperature: Int) = {
    if (temperature < min)
      LOWER
    else if (temperature > max)
      HIGHER
    else
      NORMAL
  }

  def calculateDelta(temperature: Int): Int = {
    checkBounds(temperature) match {
      case LOWER => (max + min) / 2 - temperature
      case HIGHER => temperature - (max + min) / 2
      case _ => 0
    }
  }

  def average(): Int = {
    min + (max - min) / 2
  }
}

object TemperatureBounds extends Enumeration {
  val LOWER = Value("LOVER")
  val NORMAL = Value("NORMAL")
  val HIGHER = Value("HIGHER")
}

case class TemperatureComponent(var current: Int, bounds: TemperatureBounds) {
  def simulateTemperatureChange() {
    val delta = bounds.max - bounds.min
    current = current + Random.nextInt(delta) - delta / 2
  }
}