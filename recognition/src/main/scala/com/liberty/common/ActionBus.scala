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
    println(s"[ActionBus] publish to topic : $topic action >> $action ")
    topics.get(topic).foreach(_.foreach(_.onAction(action)))
  }

  def main(args: Array[String]) {
    val c = new DaoController

    ActionBus.publish(Topics.GENERATION, CreateDaoAction(null))
    ActionBus.publish(Topics.GENERATION, CreateBeanAction(null))
  }
}

trait Subscriber {
  type Receive = PartialFunction[Action, Unit]

  init()

  private def init(): Unit = {
    getSubscriptionTopics.foreach(ActionBus.subscribe(_, this))
  }

  protected def onActionReceived: Receive

  def onAction(action: Action) {
    println(s"[${this.getClass.getSimpleName}] received $action}")
    if (onActionReceived.isDefinedAt(action))
      onActionReceived(action)
  }

  def notify(topic: String, action: Action): Unit = {
    ActionBus.publish(topic, action)
  }

  //def notifyUser()

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
}
