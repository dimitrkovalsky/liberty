package com.liberty.controllers

import java.io.File

import com.liberty.common.ProjectConfig
import com.liberty.executor.MavenExecutor
import com.liberty.helpers.{StringHelper, SynthesizeHelper}
import com.liberty.transmission.{GrammarGroups, TransmissionManager}


/**
 * User: Maxxis
 * Date: 22.10.2014
 * Time: 22:26
 */
class MavenProjectController {

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
    TransmissionManager.activateGrammar(GrammarGroups.PROJECT_NAMES)
  }

  def setProjectName(name: String): Unit = {
    ProjectConfig.projectName = name.removeSpaces()
  }

  def createProject(): Unit = {
    val mvnExec: MavenExecutor = new MavenExecutor
    //TODO: catch exception
    if (mvnExec.create())
      SynthesizeHelper.synthesize("Project created")
    else
      SynthesizeHelper.synthesize("Project creation failed")
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
}