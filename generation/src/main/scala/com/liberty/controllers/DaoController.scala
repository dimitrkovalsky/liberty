package com.liberty.controllers

import com.liberty.common.ProjectConfig
import com.liberty.generators.DaoGenerator
import com.liberty.model.JavaClass
import com.liberty.traits.Writer
import com.liberty.writers.FileClassWriter

import scala.util.{Failure, Success, Try}

/**
 * Created by Dmytro_Kovalskyi on 07.07.2014.
 */
class DaoController {
  private val writer: Writer = new FileClassWriter(ProjectConfig.projectPath)
  private val generator = new DaoGenerator(ProjectConfig.dbStandardMongo, ProjectConfig.basePackage)

  def changeDatabase() {}

  def modelChanged(model: JavaClass) {
    // TODO: regenerate DAO
  }

  def createDao(model: JavaClass): Try[String] = {
    generator.addEntity(model)
    for (e <- generator.createEntities)
      writer.write(e)

    for (interface <- generator.createInterfaces) {
      interface match {
        case Failure(t) => return Failure(t)
        case Success(i) => writer.write(i)
      }
    }

    for (dao <- generator.createDaos) {
      dao match {
        case Failure(t) => return Failure(t)
        case Success(d) => writer.write(d)
      }
    }

    generator.createFactory match {
      case None => return Failure(new Exception("Can't create dao factory"))
      case Some(factory) => writer.write(factory)
    }

    Success("Dao created")
  }
}
