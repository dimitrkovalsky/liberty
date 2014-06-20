package com.liberty.traits.persistance

import com.liberty.model.{JavaFunction, JavaClass, JavaField, JavaAnnotation}
import com.liberty.traits.Accessible
import com.liberty.builders.ClassBuilder

import scala.util.{Success, Try}

/**
 * User: Dimitr
 * Date: 27.10.13
 * Time: 12:05
 */
trait DaoAdapter extends Annotator with CRUDable with Accessible {
  this: {var javaClass: JavaClass} =>
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
