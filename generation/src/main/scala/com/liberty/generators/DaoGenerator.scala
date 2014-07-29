package com.liberty.generators

import com.liberty.common.{DBConfig, DatabaseType}
import com.liberty.exceptions.NotGeneratedException
import com.liberty.generators.adapters.{MongoAdapter, PostgresAdapter, StubAdapter}
import com.liberty.model.{JavaClass, JavaInterface}
import com.liberty.traits.LocationPackage
import com.liberty.traits.persistance.DaoAdapter

import scala.util.{Failure, Success, Try}
import scala.xml.Elem

/**
 * User: Dimitr
 * Date: 01.11.13
 * Time: 9:44
 */
/**
 * Generate daos for appropriate entities. Includes entity class marking, interface generation,
 * dao generation and factory class creation
 * Mutable class
 * getAdapter method should be updated for appropriate database
 */
class DaoGenerator(dbConfig: DBConfig, basePackage: LocationPackage) {
  /**
   * Models for daos
   */
  private val initialModels = scala.collection.mutable.Map[String, JavaClass]()
  // TODO: Optimize reuse some objects like factory instead of generation new instances
  /**
   * Factory class for dataCreation
   */
  private var factory: Option[JavaClass] = None
  /**
   * Marked initial entities for data storing using appropriate DaoAdapter
   */
  private var entities: List[JavaClass] = Nil

  private var adapters = scala.collection.mutable.Map[String, DaoAdapter]()

  private def getAdapters(entities: List[JavaClass]): List[DaoAdapter] = {
    entities.map(e => getAdapter(e))
  }

  /**
   * Creates adapter for appropriate DatabaseType.
   * Should be updated for appropriate database.
   */
  private def getAdapter(initialEntity: JavaClass) = {
    dbConfig.databaseType match {
      case DatabaseType.MONGO_DB => new MongoAdapter(initialEntity.clone().asInstanceOf[JavaClass], basePackage)
      case DatabaseType.POSTGRESQL_DB => new PostgresAdapter(initialEntity.clone().asInstanceOf[JavaClass], basePackage)
      case _ => new StubAdapter()
    }
  }

  private def addAdapter(initialEntity: JavaClass) = {
    val adapter = getAdapter(initialEntity)
    adapters += initialEntity.name -> adapter
    adapter
  }

  /**
   * Adds new model to initialModels and adds new adapter to adapters.
   * I model with tha same name was added before it removes old model and adapter
   * @param model JavaClass instance
   */
  def addModel(model: JavaClass) = {
    initialModels += model.name -> model
    addAdapter(model)
  }


  def createFactory: Option[JavaClass] = {
    adapters.lastOption.flatMap {
      case (name, adapter) =>
        val creator = adapter.getFactoryCreator
        val functions = adapters.values.map(_.getDaoCreationFunction).flatten
        factory = creator.createDaoFactory(dbConfig.databaseConfig, functions.toList)
        factory
    }
  }

  def createWebInfFiles: List[Elem] = {
    adapters.lastOption match {
      case Some((_, adapter)) =>
        val creator = adapter.getFactoryCreator
        creator.createWebInfFiles(dbConfig.databaseConfig)
      case _ => Nil
    }
  }

  private def getFactory: Try[JavaClass] = {
    createFactory match {
      case Some(f) => Success(f)
      case None => Failure(NotGeneratedException("Could not generate factory"))
    }
  }

  def createDaos: List[Try[JavaClass]] = {
    adapters.values.map(_.createDao).toList
  }

  def createInterfaces: List[Try[JavaInterface]] = {
    adapters.values.map(_.createDaoInterface).toList
  }

  def createEntities: List[JavaClass] = {
    adapters.map {
      case (name, adapter) => adapter.createEntity
    }.toList
  }

  def update(model: JavaClass): Try[EntityPacket] = {
    val adapter = addModel(model)
    for {
      entity <- Success(adapter.createEntity)
      interface <- adapter.createDaoInterface
      dao <- adapter.createDao
      factory <- getFactory
    } yield EntityPacket(entity, interface, dao, factory)
  }
}

/**
 * Uses for transferring data after dao updates
 */
case class EntityPacket(entity: JavaClass, daoInterface: JavaInterface, dao: JavaClass, factory: JavaClass)

object DaoGenerator {

}
