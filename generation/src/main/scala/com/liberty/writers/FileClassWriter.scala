package com.liberty.writers

import java.io.FileWriter
import java.nio.file.{Files, Paths}

import com.liberty.model.{XmlFile, JavaClass, JavaInterface}
import com.liberty.traits.{JavaPackage, NoPackage, Writer}
import scala.xml.{XML, Elem}

/**
 * Created by Dmytro_Kovalskyi on 07.07.2014.
 */
/**
 * Class for file writing
 * @param basePath base part of path (C:/project_name/[another path])
 */
case class FileClassWriter(basePath: String) extends Writer {

  import FileClassWriter._

  def write(clazz: JavaClass) {
    clazz.javaPackage match {
      case p: NoPackage => Console.err.println("There are No package for : " + clazz.name + " class")
      case p: JavaPackage =>
        val path = s"$basePath/$SOURCE_PATH/${p.getPackagePath}/${clazz.name}.java"
        writeToFile(path, clazz.toString)
        println(s"Wrote ${clazz.name} => $path")
    }
  }

  override def write(interface: JavaInterface): Unit = {
    interface.javaPackage match {
      case p: NoPackage => Console.err.println("There are No package for : " + interface.name + " interface")
      case p: JavaPackage =>
        val path = s"$basePath/$SOURCE_PATH/${p.getPackagePath}/${interface.name}.java"
        writeToFile(path, interface.toString)
        println(s"Wrote ${interface.name} => $path")
    }
  }

  /**
   * Removes old file if exist and creates new file with data
   * @param path
   * @param data
   */
  private def writeToFile(path: String, data: String) {
    val pathToFile = Paths.get(path)
    Files.createDirectories(pathToFile.getParent)
    Files.deleteIfExists(pathToFile)
    Files.createFile(pathToFile)
    val writer = new FileWriter(path)
    try {
      writer.write(data)
    } finally {
      if (writer != null) {
        writer.flush()
        writer.close()
      }
    }
  }

  override def writeToMetaInf(file: XmlFile): Unit = {
    val path = s"$basePath/$META_INF_PATH/${file.filename}"
    writeXml(path, file.xml)
    println(s"Wrote ${file.filename} => $path")
  }

  private def writeXml(path: String, xml: Elem) {
    val prettyPrinter = new scala.xml.PrettyPrinter(140, 4)
    val prettyXml = prettyPrinter.format(xml)

    val pathToFile = Paths.get(path)
    Files.createDirectories(pathToFile.getParent)
    Files.deleteIfExists(pathToFile)
    Files.createFile(pathToFile)
    XML.save(path, XML.loadString(prettyXml) , "UTF-8", xmlDecl = true, null)
  }
}

object FileClassWriter {
  val SOURCE_PATH = "src\\main\\java\\"
  val META_INF_PATH = "src\\main\\resources\\META-INF\\"
}
