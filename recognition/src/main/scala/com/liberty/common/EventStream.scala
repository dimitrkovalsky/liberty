package com.liberty.common

import akka.actor.{Props, ActorSystem, Actor}

/**
 * User: dimitr
 * Date: 04.11.2014
 * Time: 9:08
 */
class Subscriber2(f: (String, Any) => Unit) extends Actor {
  override def receive = {
    case (topic: String, payload: Any) => f(topic, payload)
  }
}

object EventStream {

  // ActorSystem is a heavy object: create only one per application
  // http://doc.example.io/docs/example/snapshot/scala/actors.html
  val system = ActorSystem("LibertySystem")


  def subscribe(topic:String, name: String) = {
    val props = Props(classOf[Subscriber])
    val subscriber = system.actorOf(props, name = name)
    system.eventStream.subscribe(subscriber, classOf[(String, Any)])
  }

  def publish(topic: String, payload: Any) {
    system.eventStream.publish(topic, payload)
  }
}

trait Subscriber {
  def subscribe() {
    val props = Props(this.getClass)
    val name = this.getClass.getSimpleName
  }
}

case class Subscription(prop: Props, actorName: String)
