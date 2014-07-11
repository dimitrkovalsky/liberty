package com.liberty.controllers

import com.liberty.common.ProjectConfig
import com.liberty.generators.DaoGenerator
import com.liberty.model.JavaClass
import com.liberty.traits.{Changeable, Writer}
import com.liberty.writers.FileClassWriter

import scala.util.{Failure, Success, Try}

/**
 * Created by Dmytro_Kovalskyi on 07.07.2014.
 */
class DaoController extends Changeable {
  private val writer: Writer = new FileClassWriter(ProjectConfig.projectPath)
  private val generator = new DaoGenerator(ProjectConfig.dbStandardMongo, ProjectConfig.basePackage)
  private val entities = scala.collection.mutable.Map[String, JavaClass]()

  def changeDatabase() {}

  def createDao(model: JavaClass): Try[String] = {
    entities += model.name -> model
    generator.addModel(model)
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


  private def regenerate(model: JavaClass): Try[String] = {
    generator.update(model).flatMap {
      packet =>
        writer.write(packet.entity)
        writer.write(packet.daoInterface)
        writer.write(packet.dao)
        writer.write(packet.factory)
        Success(s"${model.name} was updated")
    }
  }

  // TODO : if renamed remove old files

  override def changed(clazz: JavaClass): Try[String] = {
    regenerate(clazz)
  }
}
