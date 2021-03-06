package com.liberty.executor

import com.liberty.common.ProjectConfig
import com.liberty.model.xml.{PomXml}

/**
 * User: Maxxis
 * Date: 28.10.13
 * Time: 9:28
 */
class MavenExecutor() {

  def create() : Boolean = {
    if (!ProjectConfig.path.isEmpty && !ProjectConfig.basePackageString.isEmpty && !ProjectConfig.projectName.isEmpty) {
      val command: String = "mvn archetype:generate -DgroupId=" + ProjectConfig.basePackageString + " -DartifactId=" + ProjectConfig.projectName + " -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false"

      //create project
      CommandExecutor.execute(command, ProjectConfig.path)

      //create pom.xml
      new PomXml().createXmlFile()

      //update dependencies
     // install
      true
    } else {
      false
    }
  }

  def clean() {
    val command: String = "mvn clean"
    CommandExecutor.execute(command, ProjectConfig.projectPath)
  }

  def build() {
    val command: String = "mvn package"
    CommandExecutor.execute(command, ProjectConfig.projectPath)
  }

  def install() {
    val command: String = "mvn install:install"
    CommandExecutor.execute(command, ProjectConfig.projectPath)
  }

  def runTest() {
    val command: String = "mvn test"
    CommandExecutor.execute(command, ProjectConfig.projectPath)
  }
}

object MavenExecutor {

  def deploy(): Unit = {
    val mvn = new MavenExecutor
    mvn.clean()
    mvn.build()
  }
}