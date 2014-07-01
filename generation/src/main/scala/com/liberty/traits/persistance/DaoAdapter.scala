package com.liberty.traits.persistance

import com.liberty.builders.ClassBuilder
import com.liberty.common.DBConfig
import com.liberty.model.{JavaAnnotation, JavaClass, JavaField}
import com.liberty.traits.Accessible

import scala.util.{Success, Try}

/**
 * User: Dimitr
 * Date: 27.10.13
 * Time: 12:05
 */
trait DaoAdapter extends Annotator with CRUDable with Accessible {
  this: {var javaClass: JavaClass} =>
  protected val DAO_FACTORY_NAME = "DaoFactory"
  protected val DB_URL = "DATABASE_URL"
  protected val DB_PORT = "DATABASE_PORT"
  protected val DB_NAME = "DATABASE_NAME"
  protected var daoBuilder: ClassBuilder = new ClassBuilder()

  var datastoreName: String

  def getDatastoreAnnotation: JavaAnnotation

  def getIdAnnotation: JavaAnnotation

  def getIdField: Option[JavaField]

  def markField(field: JavaField, annotation: JavaAnnotation)

  def getEntityClass: JavaClass

  def createDaoFields()

  def createDaoClass(): Try[JavaClass]

  def createDaoConstructor()

  def getDaoClass: JavaClass = daoBuilder.getJavaClass

  def createDaoFactory(config: DBConfig): JavaClass

  def createDAO: Try[JavaClass] = {
    createDaoClass().flatMap {
      dao => {
        createDaoFields()
        createDaoConstructor()
        daoBuilder.addFunctions(createMethods())
        Success(daoBuilder.getJavaClass)
      }
    }
  }
}
