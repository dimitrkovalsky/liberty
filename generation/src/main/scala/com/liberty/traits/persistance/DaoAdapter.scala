package com.liberty.traits.persistance

import com.liberty.builders.{ClassBuilder, InterfaceBuilder}
import com.liberty.common.{DatabaseConfig, NoSQLConfig, RelationalConfig}
import com.liberty.model._
import com.liberty.traits.{Accessible, LocationPackage}

import scala.util.{Success, Try}
import scala.xml.Elem

/**
 * User: Dimitr
 * Date: 27.10.13
 * Time: 12:05
 */
/**
 * Represents adapter for appropriate database support
 */
trait DaoAdapter extends Annotator with CRUDable with Accessible {
  this: {var javaClass: JavaClass
    val basePackage: LocationPackage} =>

  /**
   * Uses for creation of factory class
   */
  trait FactoryCreator {
    /**
     * Creates factory dao class for appropriate database.
     * Different databases has different FactoryCreators
     * @return FactoryDao class represented as JavaClass instance
     */
    def createDaoFactory(config: DatabaseConfig, creationFunctions: List[JavaFunction]): Option[JavaClass]

    /**
     * Returns function for creation appropriate Dao class in DaoFactory
     * @return None if can't create function (Dao class wasn't created or some else errors)
     */
    def getDaoCreationFunction: Option[JavaFunction]

    /**
     * Creates files tha will be located in WEB-INF folder
     * @return Files in XML format
     */
    def createMetaInfFiles(config: DatabaseConfig): List[XmlFile]
  }

  abstract class RelationalFactoryCreator extends FactoryCreator {
    def createDaoFactory(config: RelationalConfig, creationFunctions: List[JavaFunction]): JavaClass

    def createWebInfFiles(config: RelationalConfig): List[XmlFile]

    override def createDaoFactory(config: DatabaseConfig, creationFunctions: List[JavaFunction]): Option[JavaClass] = {
      config match {
        case c: RelationalConfig => Some(createDaoFactory(c, creationFunctions))
        case _ => None
      }
    }

    def createMetaInfFiles(config: DatabaseConfig): List[XmlFile] = {
      config match {
        case c: RelationalConfig => createWebInfFiles(c)
        case _ => Nil
      }
    }
  }

  abstract class NoSQLFactoryCreator extends FactoryCreator {
    def createDaoFactory(config: NoSQLConfig, creationFunctions: List[JavaFunction]): JavaClass

    def createWebInfFiles(config: NoSQLConfig): List[XmlFile]

    override def createDaoFactory(config: DatabaseConfig, creationFunctions: List[JavaFunction]): Option[JavaClass] = {
      config match {
        case c: NoSQLConfig => Some(createDaoFactory(c, creationFunctions))
        case _ => None
      }
    }

    def createMetaInfFiles(config: DatabaseConfig): List[XmlFile] = {
      config match {
        case c: NoSQLConfig => createWebInfFiles(c)
        case _ => Nil
      }
    }
  }

  var datastoreName: String
  protected val DAO_FACTORY_NAME = "DaoFactory"
  protected val DB_URL = "DATABASE_URL"
  protected val DB_PORT = "DATABASE_PORT"
  protected val DB_NAME = "DATABASE_NAME"
  protected val DB_USER = "DATABASE_USER"
  protected val DB_PASSWORD = "DATABASE_PASSWORD"
  protected var daoBuilder: ClassBuilder = new ClassBuilder()

  /**
   * Returns marked entity for appropriate database
   * @return
   */
  def createEntity: JavaClass

  def getAccessible: JavaClass = getAccessible(javaClass)


  private var daoInterface: Option[JavaInterface] = None

  def getDatastoreAnnotation: JavaAnnotation

  def getIdAnnotation: JavaAnnotation

  def markField(field: JavaField, annotation: JavaAnnotation): Unit = {
    field.addAnnotation(annotation)
  }

  def getEntityClass: JavaClass

  def createDaoFields()

  /**
   * Creates dao class as instance of JavaClass without interface implementation
   */
  protected def createDaoClassBase(): Try[JavaClass]

  def createDaoConstructor()

  def getFactoryCreator: FactoryCreator

  def getDaoName: String = javaClass.name + "Dao"

  def getDaoCreationFunction: Option[JavaFunction]

  protected def getDaoInterface: Option[JavaInterface] = daoInterface

  def getIdFieldName = getIdField.fold("ERROR")(_.name)

  def getIdMethodName = {
    // TODO : throws exception instead of ERROR
    getIdField.map(n => s"get${n.name.capitalize}").getOrElse("ERROR")
  }

  /**
   * Returns first field with 'id' or 'Id' in name
   * @return None if id field is missing and Some[JavaField] if it is exists
   */
  def getIdField: Option[JavaField] = {
    javaClass.fields.find(field => field.id || field.name.startsWith("id") || field.name.contains("Id"))
  }

  /**
   * Creates dao class as instance of JavaClass with interface implementation
   * @return  instance of JavaClass
   */
  def createDao: Try[JavaClass] = {
    createDaoClassBase().flatMap {
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

  def getDatabaseName: String

}
