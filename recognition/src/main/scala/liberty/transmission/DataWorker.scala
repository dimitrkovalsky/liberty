package liberty.transmission

import java.io._
import java.net.{InetSocketAddress, ServerSocket, Socket}

import akka.actor.{ActorRef, Actor}
import akka.io.{IO, Tcp}
import akka.util.ByteString

class DataWorker(remote: InetSocketAddress, dataHandler: String => Unit, onError: String => Unit) extends Actor {

  import Tcp._
  import context.system

  IO(Tcp) ! Connect(remote)

  def receive = {
    case CommandFailed(_: Connect) =>
      dataHandler("connect failed")
      context stop self

    case c@Connected(remote, local) =>
      println("Connected to " + c.remoteAddress)
      val connection = sender()
      connection ! Register(self)
      context become {
        case data: ByteString => connection ! Write(data)
        case CommandFailed(w: Write) => onError("Write failed")
        case Received(data) => dataHandler(new String(data.toArray))
        case "close" => connection ! Close
        case _: ConnectionClosed =>
          onError("Connection closed")
          context stop self
      }
  }
}


