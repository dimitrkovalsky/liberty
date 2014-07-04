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


  def getDatastoreAnnotation: JavaAnnotation = ???

  def getIdAnnotation: JavaAnnotation = ???

  def getIdField: Option[JavaField] = ???

  def markField(field: JavaField, annotation: JavaAnnotation): Unit = ???

  def getEntityClass: JavaClass = ???

  def createDaoFields(): Unit = ???

  def createDaoClass(): Try[JavaClass] = ???

  def createDaoConstructor(): Unit = ???

  def createDelete() = ???

  def createFind() = ???

  def createUpdate() = ???

  def createInsert() = ???

  override def createFindAll(): Option[JavaFunction] = ???

  override def createFindById(): Option[JavaFunction] = ???

  override def getFactoryCreator: FactoryCreator = ???

  override def getDaoName: String = ???

  override def getDaoCreationFunction: Option[JavaFunction] = ???

  /**
   * Returns marked entity for appropriate database
   * @return
   */
  override def createEntity: JavaClass = ???
}

