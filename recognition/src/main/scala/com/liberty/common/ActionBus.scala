package com.liberty.common

import com.liberty.controllers.DaoController

/**
 * User: Dmytro_Kovalskyi
 * Date: 04.11.2014
 * Time: 15:19
 */
object ActionBus {
  protected val topics = scala.collection.mutable.Map[String, List[Subscriber]]()

  def subscribe(topic: String, subscriber: Subscriber) = {
    topics.get(topic).fold(topics += topic -> List(subscriber))(l => {
      topics += topic -> l.++(List(subscriber))
    })
    println(s"[ActionBus] ${subscriber.getClass.getSimpleName} subscribed for $topic}")
  }

  def publish(topic: String, action: Action) = {
    println(s"[ActionBus] publish to topic : $topic action >> ${action.getClass.getSimpleName} ")
    topics.get(topic).foreach(_.foreach(_.onAction(action)))
  }
}

trait Subscriber {
  type Received = PartialFunction[Action, Either[String, String]]

  init()

  private def init(): Unit = {
    getSubscriptionTopics.foreach(ActionBus.subscribe(_, this))
  }

  protected def onActionReceived: Received

  def onAction(action: Action) {
    println(s"[${this.getClass.getSimpleName}] received ${action.getClass.getSimpleName}")
    if (onActionReceived.isDefinedAt(action))
      onActionReceived(action)
  }

  def notify(topic: String, action: Action): Unit = {
    ActionBus.publish(topic, action)
  }


  protected def getSubscriptionTopics: List[String]
}

trait GeneratorSubscriber extends Subscriber {
  protected def getSubscriptionTopics = List(Topics.GENERATION)

  /**
   * Sent message into GENERATION topic
   * @param action
   */
  def notify(action: Action): Unit = {
    notify(Topics.GENERATION, action)
  }

  def notifyOperationCompleted(result: Either[String, String]) {
    notify(Topics.USER_NOTIFICATION, UserNotificationAction(NotificationType.GENERATION_COMPLETED, result))
  }

  override def onAction(action: Action) {
    println(s"[${this.getClass.getSimpleName}] received ${action.getClass.getSimpleName}")
    if (onActionReceived.isDefinedAt(action))
      notifyOperationCompleted(onActionReceived(action))
  }
}
