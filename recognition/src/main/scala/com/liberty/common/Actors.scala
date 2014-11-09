package com.liberty.common

import akka.actor.{Props, ActorSystem}

/**
 * User: Dmytro_Kovalskyi
 * Date: 06.11.2014
 * Time: 16:45
 */
object Actors {
  val actorSystem = ActorSystem("LibertyActors")

  def createActor(props: Props) = actorSystem.actorOf(props)

  def createActor[T](clazz: Class[T]) = actorSystem.actorOf(Props(clazz))
}
