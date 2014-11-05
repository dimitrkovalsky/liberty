package com.liberty.controllers

import akka.actor.{ActorRef, Actor}
import akka.actor.Actor.Receive
import com.liberty.common.{UIAction, UserNotificationAction, Topics, Subscriber}

/**
 * User: Dmytro_Kovalskyi
 * Date: 05.11.2014
 * Time: 16:17
 */
class NotificationController(uiActor: ActorRef) extends Controller with Actor with Subscriber {
  override def receive: Actor.Receive = {
    case action: UIAction => println("[NotificationController] received action from UI")
  }

  override protected def onActionReceived = {
    case notification: UserNotificationAction => uiActor ! notification
      Right("UserNotification processed")
  }

  override protected def getSubscriptionTopics: List[String] = List(Topics.USER_NOTIFICATION)
}
