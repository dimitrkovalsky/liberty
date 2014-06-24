package liberty.transmission

import java.io._
import java.net.{ServerSocket, Socket}

class DataWorker(dataHandler: String => Unit) extends Runnable {
  var client: ClientHandler = null

  def run() {
    try {
      val listener = new ServerSocket(TransmissionManager.DATA_PORT)
      println("[DataWorker] Listening on port " + TransmissionManager.DATA_PORT)
      while (true) {
        val socket = listener.accept()
        client = new ClientHandler(socket, dataHandler)
        client.start()
      }
      listener.close()
    } catch {
      case e: IOException =>
        System.err.println("[DataWorker] Could not listen on port: " + TransmissionManager.DATA_PORT + ".")
    }
  }

  def writeData(data: String) {
    client.writeData(data)
  }
}

class ClientHandler(socket: Socket, handleData: String => Unit) extends Actor {
  private var writer: WriteWorker = null

  private class ReadWorker(stream: InputStream) extends Runnable {
    def run() {
      val in = new BufferedReader(new InputStreamReader(stream))
      println("[ReadWorker] Client connected from " + socket.getInetAddress + ":" + socket.getPort)
      var inputLine = in.readLine()
      while (inputLine != null) {
        //   println("[ReadWorker] ClientHandler read : " + inputLine)
        handleData(inputLine)
        inputLine = in.readLine()
      }
    }
  }

  private class WriteWorker(stream: OutputStream) extends Actor {
    val outputStream = new PrintWriter(socket.getOutputStream, true)

    def act() {
      while (true) {
        receive {
          case data: String => outputStream.println(data)
            println("[WriteWorker] data transferred : " + data)
        }
      }
    }
  }

  def act() {
    try {
      writer = new WriteWorker(socket.getOutputStream)
      writer.start()
      new Thread(new ReadWorker(socket.getInputStream)).start()
    } catch {
      case e: Throwable => System.err.println("[ClientHandler] Error " + e)
    }
  }

  def writeData(data: String) {
    writer ! data
  }
}
