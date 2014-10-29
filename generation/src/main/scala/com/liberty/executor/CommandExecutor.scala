package com.liberty.executor

import java.io.{BufferedReader, File, InputStreamReader}

import com.liberty.common.ProjectConfig

/**
 * User: Maxxis
 * Date: 28.10.13
 * Time: 9:16
 */
object CommandExecutor {

  val WORKING_DIRECTORY: String = "C:\\"

  def execute(command: String) {
    startProcess(createBuilder(command, WORKING_DIRECTORY))
  }

  def execute(command: String, workingDirectory: String) {
    startProcess(createBuilder(command, workingDirectory))
  }

  private def createBuilder(command: String, workingDirectory: String): ProcessBuilder = {
    val builder: ProcessBuilder = new ProcessBuilder()
    builder.directory(new File(workingDirectory))
    builder.command("cmd.exe", "/c", command)
    builder.redirectErrorStream(true)
    builder
  }

  //TODO: add start server and test copy jar
  def deploy(): Unit = {
    val mvn: MavenExecutor = new MavenExecutor
    mvn.clean()
    mvn.build()
    execute("copy *.jar " + ProjectConfig.serverPath, ProjectConfig.targetPath)
//    execute("start server")
//    execute("start " + ProjectConfig.browser + " new " + ProjectConfig.startPage)
  }

  private def startProcess(builder: ProcessBuilder) {
    val p: Process = builder.start()
    val r: BufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream))
    var line: String = ""

    while (line != null) {
      line = r.readLine()
      System.out.println(line)
    }
  }
}
