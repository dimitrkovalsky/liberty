package com.liberty.controllers

import com.liberty.common.{DatabaseType, ProjectConfig}
import com.liberty.generators.{DaoPacket, DaoGenerator}
import com.liberty.model.JavaClass
import com.liberty.traits.{Changeable, Writer}
import com.liberty.writers.FileClassWriter

import scala.util.{Failure, Success, Try}
import com.liberty.common.DatabaseType.DatabaseType
import com.liberty.common.DatabaseType.DatabaseType

/**
 * Created by Dmytro_Kovalskyi on 07.07.2014.
 */
class DaoController extends Changeable {
  private val writer: Writer = new FileClassWriter(ProjectConfig.projectPath)
  private var generator = createGenerator
  private val entities = scala.collection.mutable.Map[String, JavaClass]()

  def changeDatabase(db: DatabaseType): Either[String, String] = {
    if (generator.dbConfig.databaseType != db) {
      val generated = generator.getGenerated
      val models = generator.initialModels
      clean(generated)
      // For removing previous dao files ned remove files and recreate generator
      changeConfig(db)
      generator = createGenerator
      models.values.foreach(createDao)
      Right(s"Database was successfully changed. Changed ${generated.filesAmount} files")
    } else
      Left("Can't change database into the same")
  }

  private def createGenerator: DaoGenerator = new DaoGenerator(ProjectConfig.defaultDatabase, ProjectConfig.basePackage)

  private def changeConfig(db: DatabaseType) {
    db match {
      case DatabaseType.MONGO_DB => ProjectConfig.defaultDatabase = ProjectConfig.dbStandardMongo
      case DatabaseType.POSTGRES_DB => ProjectConfig.defaultDatabase = ProjectConfig.dbStandardPostgres
      case _ => println(s"${db.toString} doesn't support")
    }
  }

  /**
   * Removes generated files in DaoPacket instance
   * @param generated
   */
  private def clean(generated: DaoPacket) {

  }

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

    generator.createMetaInfFiles.foreach(writer.writeToMetaInf)


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
