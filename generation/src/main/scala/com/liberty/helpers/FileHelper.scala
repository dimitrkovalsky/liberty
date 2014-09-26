package com.liberty.helpers

import java.io.File

import com.liberty.model.xml.XmlFile
import com.liberty.model.{JavaClass, JavaInterface}
import com.liberty.traits.{JavaPackage, NoPackage}


/**
 * Created by Dmytro_Kovalskyi on 04.08.2014.
 */
class FileHelper(basePath: String) extends ProjectPaths {
  def deleteFile(clazz: JavaClass) {
    clazz.javaPackage match {
      case p: NoPackage => Console.err.println("There are No package for : " + clazz.name + " class")
      case p: JavaPackage =>
        val path = getPath(clazz.name, p)
        deleteFile(path)
        println(s"Removed ${clazz.name} from $path")
    }
  }

  def deleteFile(interface: JavaInterface) {
    interface.javaPackage match {
      case p: NoPackage => Console.err.println("There are No package for : " + interface.name + " class")
      case p: JavaPackage =>
        val path = getPath(interface.name, p)
        deleteFile(path)
        println(s"Removed ${interface.name} from $path")
    }
  }

  def deleteFile(xml: XmlFile) {
    val path = s"$basePath/$META_INF_PATH/${xml.filename}"
    deleteFile(path)
    println(s"Removed ${xml.filename} from $path")
  }

  def getPath(fileName: String, p: JavaPackage): String = {
    s"$basePath/$SOURCE_PATH/${p.getPackagePath}/$fileName.java"
  }


  private def deleteFile(path: String) = {
    val file = new File(path)
    if (file.exists())
      file.delete()
    else
      false
  }
}
