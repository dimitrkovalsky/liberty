package com.liberty.generators.adapters

import com.liberty.builders.{ClassBuilder, FunctionBuilder}
import com.liberty.common.Implicits._
import com.liberty.common.{ProjectConfig, NoSQLConfig}
import com.liberty.exceptions.IdMissedException
import com.liberty.model._
import com.liberty.operations._
import com.liberty.traits.persistance.DaoAdapter
import com.liberty.traits.{JavaPackage, LocationPackage}
import com.liberty.types.collections.ListType
import com.liberty.types.primitives.{IntegerType, StringType}
import com.liberty.types.{ObjectType, SimpleObjectType}

import scala.util.{Failure, Success, Try}
import scala.xml.Elem

/**
 * User: Dimitr
 * Date: 01.11.13
 * Time: 9:45
 */
class MongoAdapter(var javaClass: JavaClass, bPackage: LocationPackage) extends DaoAdapter {
  ProjectConfig.addEntity(javaClass.name)
  val basePackage = bPackage
  private val morphiaPackage: String = "com.google.code.morphia.annotations"
  var datastoreName: String = javaClass.name.firstToLowerCase
  val datastore = ObjectType("Datastore", JavaPackage("com.google.code.morphia", "Datastore"))
  var dao: Option[JavaClass] = None
  private val daoException = JavaException("DaoException", basePackage.nested("errors", "DaoException"))

  def addAccessors() = super.addAccessors(javaClass)

  def createEntity: JavaClass = {
    addAccessors()
    annotateClass()
    javaClass
  }

  def getDatastoreAnnotation: JavaAnnotation = {
    JavaAnnotation("Entity", JavaPackage(morphiaPackage, "Entity"))("value", datastoreName)("noClassnameStored", "true")
  }

  def getIdAnnotation: JavaAnnotation = {
    JavaAnnotation("Id", JavaPackage(morphiaPackage, "Id"))
  }

  def getEntityClass: JavaClass = javaClass


  /**
   * Creates Dao class in  basePackage + .dao package
   * @return
   */
  override protected def createDaoClassBase(): Try[JavaClass] = {
    daoBuilder.setName(getDaoName)
    daoBuilder.addPackage(basePackage.nested("dao", getDaoName))
    val extension = new JavaClass("BasicDAO", JavaPackage("com.google.code.morphia.dao", "BasicDAO"))
    getIdField.map {
      idField =>
        extension.addGenericType(javaClass, idField.dataType)
    }.getOrElse {
      return Failure(new IdMissedException("[MongoAdapter] Id field is not specified"))
    }
    daoBuilder.addExtend(extension)
    val daoClazz = daoBuilder.getJavaClass
    dao = Some(daoClazz)
    Success(daoClazz)
  }

  override def createDaoFields(): Unit = {}

  private def createEntityParameter = FunctionParameter("entity", ObjectType(javaClass.getTypeName, javaClass.javaPackage))

  private val getEntityType = ObjectType(javaClass.getTypeName, javaClass.javaPackage)

  override def createDaoConstructor() {
    val builder = new FunctionBuilder
    builder.setName(daoBuilder.javaClass.name)
    builder.addModifier(PublicModifier)
    val variable = new Variable("datastore")
    builder.addParam(FunctionParameter(variable, datastore))
    builder.addOperation(new SelfFunctionInvokeOperation(FunctionType.SUPER_CONSTRUCTOR, parameters = List(variable)))
    daoBuilder.addFunction(builder.getFunction)
  }

  override def createInsert() = {
    val param = createEntityParameter
    val builder = FunctionBuilder(PublicModifier, "insert", param)
    builder.tryable {
      builder.addSuperMethodInvoke("save", None, param.paramName)
    }.throwWrapped(daoException)
    Some(builder.getFunction)
  }

  override def createFind() = {
    val param = createEntityParameter
    val builder = FunctionBuilder(PublicModifier, "find", param)
    builder.wrapable(daoException) {
      val ret = ReturnOperation(SuperFunctionInvokeOperation("findOne", List("_id", GetValueOperation(param.paramName.name, getIdMethodName))))
      builder.addOperation(ret)
    }
    builder.addReturn(param.paramType)
    Some(builder.getFunction)
  }

  override def createUpdate() = {
    val param = createEntityParameter
    val builder = FunctionBuilder(PublicModifier, "update", param)
    builder.tryable {
      builder.addSuperMethodInvoke("save", None, param.paramName)
    }.throwWrapped(daoException)
    Some(builder.getFunction)
  }

  override def createDelete() = {
    val param = createEntityParameter
    val builder = FunctionBuilder(PublicModifier, "delete", param)
    builder.tryable {
      val basicDBObject = SimpleObjectType("BasicDBObject", "com.mongodb")
      val removeParam = ChainedOperations(None, CreationOperation(basicDBObject), FunctionInvokeOperation("append",
        List(getIdFieldName, GetValueOperation(param.paramName.name, getIdMethodName))))
      builder.addOperation(ChainedOperations(None, FunctionInvokeOperation("getCollection"), FunctionInvokeOperation("remove", List(removeParam))))
    }.throwWrapped(daoException)
    Some(builder.getFunction)
  }

  override def createFindAll(): Option[JavaFunction] = {
    val builder = FunctionBuilder(PublicModifier, "findAll")
    builder.tryable {
      val chain = ChainedOperations(None, FunctionInvokeOperation("getCollection"), FunctionInvokeOperation("find",
        List(javaClass.asClassParam)), FunctionInvokeOperation("asList"))
      builder.addOperation(ReturnOperation(chain))
    }.throwWrapped(daoException)
    builder.addReturn(ListType(getEntityType))
    Some(builder.getFunction)
  }

  override def createFindById(): Option[JavaFunction] = {
    getIdField.map {
      id =>
        val param = FunctionParameter(id)
        val builder = FunctionBuilder(PublicModifier, s"findBy${id.name.capitalize}", param)
        builder.tryable {
          builder.addOperation(ReturnOperation(SuperFunctionInvokeOperation("findOne", List("_id", param.paramName.name.asValue))))
        }.throwWrapped(daoException)
        builder.addReturn(getEntityType)
        Some(builder.getFunction)
    }.getOrElse(None)
  }

  override def getDaoCreationFunction: Option[JavaFunction] = getFactoryCreator.getDaoCreationFunction

  override def getFactoryCreator: FactoryCreator = new NoSQLFactoryCreator {
    private val datastoreVariable = JavaField("datastore", datastore, PrivateStaticModifier)

    override def getDaoCreationFunction: Option[JavaFunction] = {
      dao.flatMap {
        dao =>
          val builder = FunctionBuilder(PublicStaticModifier, "get" + getDaoName)
          builder.setOutputType(getDaoInterface.get)
          builder.addOperation(ReturnOperation(CreationOperation(dao, None, List(Variable(datastoreVariable)))))
          Some(builder.getFunction)
      }
    }

    /**
     * Creates factory class in  basePackage + .common package
     * @param config
     * @param creationFunctions
     * @return
     */
    override def createDaoFactory(config: NoSQLConfig, creationFunctions: List[JavaFunction]): JavaClass = {
      val builder = ClassBuilder(DAO_FACTORY_NAME)
      builder.addPackage(basePackage.nested("common"))
      builder.addField(JavaField(DB_URL, StringType, PrivateStaticModifier, config.url))
      builder.addField(JavaField(DB_PORT, IntegerType, PrivateStaticModifier, config.port.toString))
      builder.addField(JavaField(DB_NAME, StringType, PrivateStaticModifier, config.database))
      builder.addField(datastoreVariable)
      builder.static {
        builder.tryable {
          val mongoType = ObjectType("Mongo", JavaPackage("com.mongodb", "Mongo"))
          val mongo = Variable(mongoType)
          builder addOperation CreationOperation(mongoType, mongo, List(DB_URL.asValue, DB_PORT.asValue))
          val morphiaType = ObjectType("Morphia", JavaPackage("com.google.code.morphia", "Morphia"))
          val morphia = Variable(morphiaType)
          builder addOperation CreationOperation(morphiaType, morphia)
          builder addOperation ObjectFunctionInvokeOperation(morphia, "createDatastore", List(mongo, DB_NAME.asValue), datastoreVariable)
        }.printError(StandardExceptions.UnknownHostException)
      }

      builder.addFunctions(creationFunctions)
      builder.getJavaClass
    }

    override def createWebInfFiles(config: NoSQLConfig): List[XmlFile] = Nil
  }

  override def getDatabaseName: String = "Mongo"

}
