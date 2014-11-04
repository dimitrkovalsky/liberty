package com.liberty.controllers

import com.liberty.common.DatabaseType.DatabaseType
import com.liberty.common._
import com.liberty.generators.{DaoGenerator, DaoPacket}
import com.liberty.helpers.FileHelper
import com.liberty.model.JavaClass
import com.liberty.traits.Changeable

import scala.util.{Failure, Success, Try}

/**
 * Created by Dmytro_Kovalskyi on 07.07.2014.
 */
class DaoController extends Changeable with Controller with GeneratorSubscriber {
  private var generator = createGenerator

  def changeDatabase(db: DatabaseType): Either[String, String] = {
    if (generator.dbConfig.databaseType != db) {
      val generated = generator.getGenerated
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
    val helper = new FileHelper(ProjectConfig.projectPath)
    generated.daos.foreach(helper.deleteFile)
    generated.entities.foreach(helper.deleteFile)
    generated.interfaces.foreach(helper.deleteFile)
    generated.factory.foreach(helper.deleteFile)
    generated.metaInf.foreach(helper.deleteFile)
  }

  def createDao(model: JavaClass): Try[String] = {
    val copy = model.deepCopy
    models += model.name -> copy

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
    Register.getModel(model.name).foreach(m => Register.changeModel(m.copy(daoExists = true)))

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

  override protected def onActionReceived: Receive = {
    case CreateDaoAction(model) => println("[DaoController] handling" )
  }
}
