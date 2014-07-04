package com.liberty.traits.persistance

import com.liberty.builders.{ClassBuilder, InterfaceBuilder}
import com.liberty.common.DBConfig
import com.liberty.model._
import com.liberty.traits.{Accessible, LocationPackage}

import scala.util.{Success, Try}

/**
 * User: Dimitr
 * Date: 27.10.13
 * Time: 12:05
 */
trait DaoAdapter extends Annotator with CRUDable with Accessible {
  this: {var javaClass: JavaClass
    val basePackage: LocationPackage} =>

  trait FactoryCreator {
    def createDaoFactory(config: DBConfig, daos: List[JavaFunction]): JavaClass

    /**
     * Returns function for creation appropriate Dao class in DaoFactory
     * @return None if can't create function (Dao class wasn't created or some else errors)
     */
    def getDaoCreationFunction: Option[JavaFunction]
  }

  protected val DAO_FACTORY_NAME = "DaoFactory"
  protected val DB_URL = "DATABASE_URL"
  protected val DB_PORT = "DATABASE_PORT"
  protected val DB_NAME = "DATABASE_NAME"
  protected var daoBuilder: ClassBuilder = new ClassBuilder()

  var datastoreName: String
  private var daoInterface: Option[JavaInterface] = None

  def getDatastoreAnnotation: JavaAnnotation

  def getIdAnnotation: JavaAnnotation

  def getIdField: Option[JavaField]

  def markField(field: JavaField, annotation: JavaAnnotation)

  def getEntityClass: JavaClass

  def createDaoFields()

  protected def createDaoClass(): Try[JavaClass]

  def createDaoConstructor()

  def getFactoryCreator: FactoryCreator

  def getDaoName: String

  def getDaoCreationFunction: Option[JavaFunction]

  protected def getDaoInterface: Option[JavaInterface] = daoInterface

  /**
   * Creates dao class as instance of JavaClass
   * @return  instance of JavaClass
   */
  def createDao: Try[JavaClass] = {
    createDaoClass().flatMap {
      dao => {
        createDaoFields()
        createDaoConstructor()
        daoBuilder.addFunctions(createMethods())
        createDaoInterface.foreach(daoBuilder.addImplements)
        Success(daoBuilder.getJavaClass)
      }
    }
  }

  def createDaoInterface: Try[JavaInterface] = {
    val builder = InterfaceBuilder("I" + getDaoName, createMethods())
    builder.addPackage(basePackage.nested("dao", builder.getName))
    val interface = builder.getInterface
    daoInterface = Some(interface)
    Success(interface)
  }
}
