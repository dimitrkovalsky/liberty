package com.liberty.generators.adapters

import com.liberty.common.DBConfig
import com.liberty.model.{JavaFunction, JavaAnnotation, JavaClass, JavaField}
import com.liberty.traits.{LocationNoPackage, LocationPackage, NoPackage, JavaPackage}
import com.liberty.traits.persistance.DaoAdapter

import scala.util.Try

/**
 * User: Dimitr
 * Date: 04.11.13
 * Time: 9:27
 */
case class StubAdapter() extends DaoAdapter {
  var javaClass: JavaClass = _
  var datastoreName: String = _
  val basePackage : LocationPackage = new LocationNoPackage
  /**
   * Returns marked entity for appropriate database
   * @return
   */
  override def createEntity: JavaClass = ???

  override def getDatastoreAnnotation: JavaAnnotation = ???

  override def getIdAnnotation: JavaAnnotation = ???

  override def createDaoConstructor(): Unit = ???

  override def createDaoFields(): Unit = ???

  override def getEntityClass: JavaClass = ???

  override def getDaoCreationFunction: Option[JavaFunction] = ???

  override protected def createDaoClassBase(): Try[JavaClass] = ???

  override def getFactoryCreator: FactoryCreator = ???

  override def createUpdate(): Option[JavaFunction] = ???

  override def createInsert(): Option[JavaFunction] = ???

  override def createDelete(): Option[JavaFunction] = ???

  override def createFindAll(): Option[JavaFunction] = ???

  override def createFind(): Option[JavaFunction] = ???

  override def createFindById(): Option[JavaFunction] = ???
}

