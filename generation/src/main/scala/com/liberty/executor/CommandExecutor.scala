package com.liberty.executor

import java.io.{BufferedReader, File, InputStreamReader}

/**
 * User: Maxxis
 * Date: 28.10.13
 * Time: 9:16
 */
object CommandExecutor {

  val WORKING_DIRECTORY: String = "C:\\"

  def execute(command: String, redirectOutput: Boolean = true) {
    startProcess(createBuilder(command, WORKING_DIRECTORY), redirectOutput)
  }


  def execute(command: String, workingDirectory: String) {
    println("Execute command: " + command)
    startProcess(createBuilder(command, workingDirectory), true)
  }

  private def createBuilder(command: String, workingDirectory: String): ProcessBuilder = {
    val builder: ProcessBuilder = new ProcessBuilder()
    builder.directory(new File(workingDirectory))
    builder.command("cmd.exe", "/c", command)
    builder.redirectErrorStream(true)
    builder
  }

  private def startProcess(builder: ProcessBuilder, redirectOutput: Boolean) {
    var p: Process = null
    try {
      p = builder.start()
    } catch {
      case e: Exception => e.printStackTrace()
    }
    if (redirectOutput) {
      val r: BufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream))
      var line: String = ""

      while (line != null) {
        line = r.readLine()
        println(line)
      }
    }
  }
}
