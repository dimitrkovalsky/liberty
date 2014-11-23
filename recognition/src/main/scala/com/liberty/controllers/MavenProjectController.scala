package com.liberty.controllers

import java.io.File

import com.liberty.common._
import com.liberty.executor.{CommandExecutor, MavenExecutor}
import com.liberty.helpers.{StringHelper, SynthesizeHelper}
import com.liberty.transmission.TransmissionManager


/**
 * User: Maxxis
 * Date: 22.10.2014
 * Time: 22:26
 */
class MavenProjectController extends Controller {

  import com.liberty.common.Implicits._

  def addPackage(packageName: String) {
    val path = ProjectConfig.projectPath + "/src/main/java/" + StringHelper.getPathByPackageName(ProjectConfig.basePackageString) + "/" + StringHelper.getPathByPackageName(packageName)
    val file: File = new File(path)
    file.mkdirs()
  }

  def startProjectCreation(): Unit = {
    //TODO: Enter project name dialog
    println("Enter name")
    SynthesizeHelper.synthesize("Give the name for a new project, please")
    GrammarController.changeGrammarGroup(GrammarGroups.PROJECT_NAMES)
  }

  def setProjectName(name: String): Unit = {
    ProjectConfig.projectName = name.removeSpaces()
  }

  def createProject(): Unit = {
    val mvnExec: MavenExecutor = new MavenExecutor
    if (mvnExec.create()) {
      SynthesizeHelper.synthesize("Project created")
      //TODO: Think about place of grammar group activation
      GrammarController.changeGrammarGroup(GrammarGroups.COMPONENT_CREATION)
      ActionBus.publish(Topics.USER_NOTIFICATION, UserNotificationAction(NotificationType.PROJECT_CREATED, Right("Project created")))
    } else {
      SynthesizeHelper.synthesize("Project creation failed")
      ActionBus.publish(Topics.USER_NOTIFICATION, UserNotificationAction(NotificationType.PROJECT_CREATION_FAILED, Right("Project not created")))
    }
  }

  def testAppsPackages(): Unit = {
    addPackage("beans")
    addPackage("commons")
    addPackage("dao")
    addPackage("errors")
    addPackage("models")
    addPackage("rest")
    addPackage("security")
  }

  def build(): Unit = {
    val mvn = new MavenExecutor
    mvn.clean()
    mvn.build()
    SynthesizeHelper.synthesize("Project " + ProjectConfig.projectName + " built")
  }

  def deploy(): Unit = {
    new Thread(new Runnable {
      override def run(): Unit = {
        CommandExecutor.execute("copy *.war " + ProjectConfig.serverDeploymentPath, ProjectConfig.targetPath)
        CommandExecutor.execute(ProjectConfig.serverStartPath)
        SynthesizeHelper.synthesize("Project " + ProjectConfig.projectName + " deployed")
      }
    }).start()

  }

  def buildAndDeploy(): Unit = {
    build()
    deploy()
  }

  def openBrowser(): Unit = {
    new Thread(new Runnable {
      override def run(): Unit = {
        CommandExecutor.execute("start " + ProjectConfig.browser + " " + ProjectConfig.startPage)
      }
    })
  }

  def runMongo(): Unit = {
    new Thread(new Runnable {
      override def run(): Unit = {
        CommandExecutor.execute(ProjectConfig.mongoPath, redirectOutput = false)
        ActionBus.publish(Topics.USER_NOTIFICATION, UserNotificationAction(NotificationType.PROCESS_STARTED, Right("Mongo started")))
        SynthesizeHelper.synthesize("Mongo server started")
      }
    })
  }
}