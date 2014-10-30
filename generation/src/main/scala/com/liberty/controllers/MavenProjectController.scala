package com.liberty.controllers

import java.io.File
import org.apache.commons.lang3.StringUtils
import com.liberty.common.ProjectConfig

/**
 * User: Maxxis
 * Date: 22.10.2014
 * Time: 22:26
 */
class MavenProjectController {

  def addPackage(packageName: String){
    val path = ProjectConfig.projectPath + "/src/main/java/" + getPathByPackageName(ProjectConfig.basePackageString)+ "/" + getPathByPackageName(packageName)
    val file: File = new File(path)
    file.mkdirs()
  }

  def getPathByPackageName(packageName: String) : String = {
    if (packageName.contains("."))
      StringUtils.replaceChars(packageName,".", "/")
    else
      packageName
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