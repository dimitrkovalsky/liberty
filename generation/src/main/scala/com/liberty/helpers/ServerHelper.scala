package com.liberty.helpers

import com.liberty.common.ProjectConfig
import com.liberty.executor.{CommandExecutor, MavenExecutor}

/**
 * User: Maxxis
 * Date: 05.11.2014
 * Time: 1:32
 */
class ServerHelper {


  def startServer(): Unit = {
    MavenExecutor.deploy
    CommandExecutor.execute("copy *.jar " + ProjectConfig.serverDeploymentPath, ProjectConfig.targetPath)
    CommandExecutor.execute(ProjectConfig.serverStartPath)
    CommandExecutor.execute("start " + ProjectConfig.browser + " new " + ProjectConfig.startPage)
  }

  //TODO:realize stop server
  def stopServer(): Unit = {

  }
}
