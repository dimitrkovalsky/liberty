package com.liberty.controllers

import akka.actor.{Actor, ActorRef}
import akka.actor.Actor.Receive
import com.liberty.common._

/**
 * User: Dmytro_Kovalskyi
 * Date: 05.11.2014
 * Time: 16:17
 */
class NotificationController(uiActor: ActorRef) extends Controller with Subscriber {

  override protected def onActionReceived = {
    case notification: UserNotificationAction =>
      uiActor ! notification
      Right("UserNotification processed")
    case a@ClassEditAction(clazz) =>
      println(clazz.toString)
      uiActor ! a
      Right("OK")
  }

  override protected def getSubscriptionTopics: List[String] = List(Topics.USER_NOTIFICATION)
}

class UiActor(onAction: UserNotificationAction => Unit) extends Actor {
  override def receive: Actor.Receive = {
    case action: UIAction => println("[NotificationController] received action from UI")
    case notification: UserNotificationAction => println("Received : " + notification)
      onAction(notification)
    case _ => println("[NotificationActor] received unhandled message")
  }
}


