package com.liberty.generators

import com.liberty.common.{DBConfig, DatabaseType}
import com.liberty.generators.adapters.{MongoAdapter, StubAdapter}
import com.liberty.model.{JavaClass, JavaInterface}
import com.liberty.traits.LocationPackage
import com.liberty.traits.persistance.DaoAdapter

import scala.util.Try

/**
 * User: Dimitr
 * Date: 01.11.13
 * Time: 9:44
 */
/**
 * Generate daos for appropriate entities. Includes entity class marking, interface generation,
 * dao generation and factory class creation
 * Mutable class
 */
class DaoGenerator(dbConfig: DBConfig, basePackage: LocationPackage) {
  /**
   * Models for daos
   */
  private var initialEntities: List[JavaClass] = Nil
  /**
   * Factory class for dataCreation
   */
  var factory: Option[JavaClass] = None
  /**
   * Marked initial entities for data storing using appropriate DaoAdapter
   */
  var entities: List[JavaClass] = Nil

  var daos: List[JavaClass] = Nil
  var daoInterfaces: List[JavaClass] = Nil
  var adapters: List[DaoAdapter] = Nil

  private def getAdapters(entities: List[JavaClass]): List[DaoAdapter] = {
    entities.map(e => getAdapter(e))
  }

  private def getAdapter(initialEntity: JavaClass) = {
    dbConfig.databaseType match {
      case DatabaseType.MONGO_DB => new MongoAdapter(initialEntity.clone().asInstanceOf[JavaClass], basePackage)
      case _ => new StubAdapter()
    }
  }

  private def addAdapter(initialEntity: JavaClass) {
    adapters = adapters :+ getAdapter(initialEntity)
  }

  def addEntity(entity: JavaClass) {
    initialEntities = initialEntities :+ entity
    addAdapter(entity)
  }

  def createFactory: Option[JavaClass] = {
    adapters.lastOption.flatMap {
      adapter =>
        val creator = adapter.getFactoryCreator
        val functions = adapters.map(_.getDaoCreationFunction).flatten
        factory = Some(creator.createDaoFactory(dbConfig.connectionConfig, functions))
        factory
    }
  }

  def createDaos: List[Try[JavaClass]] = {
    adapters.map(_.createDao)
  }

  def createInterfaces: List[Try[JavaInterface]] = {
    adapters.map(_.createDaoInterface)
  }

  def createEntities: List[JavaClass] = {
    adapters.map {
      adapter => adapter.createEntity
    }
  }
}


object DaoGenerator {

}
