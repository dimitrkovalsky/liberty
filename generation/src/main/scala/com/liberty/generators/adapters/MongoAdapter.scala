package com.liberty.generators.adapters

import com.liberty.builders.{ClassBuilder, FunctionBuilder}
import com.liberty.common.DBConfig
import com.liberty.common.Implicits._
import com.liberty.exceptions.IdMissedException
import com.liberty.model._
import com.liberty.operations._
import com.liberty.traits.persistance.DaoAdapter
import com.liberty.traits.{JavaPackage, LocationPackage}
import com.liberty.types.{ObjectType, SimpleObjectType}

import scala.util.{Failure, Success, Try}

/**
 * User: Dimitr
 * Date: 01.11.13
 * Time: 9:45
 */
class MongoAdapter(var javaClass: JavaClass, basePackage: LocationPackage) extends DaoAdapter {
  private val morphiaPackage: String = "com.google.code.morphia.annotations"
  var datastoreName: String = javaClass.name.firstToLowerCase
  val datastore = ObjectType("Datastore", JavaPackage("com.google.code.morphia", "Datastore"))
  private val daoException = JavaException("DaoException", basePackage.nested("errors", "DaoException"))

  def getAccessible: JavaClass = getAccessible(javaClass)

  def addAccessors() = super.addAccessors(javaClass)

  def getDatastoreAnnotation: JavaAnnotation = {
    JavaAnnotation("Entity", JavaPackage(morphiaPackage, "Entity"))("value", datastoreName)("noClassnameStored", "true")
  }

  def getIdAnnotation: JavaAnnotation = {
    JavaAnnotation("Id", JavaPackage(morphiaPackage, "Id"))
  }

  def getIdField: Option[JavaField] = {
    javaClass.fields.find(field => field.id || field.name.startsWith("id") || field.name.contains("Id"))
  }

  def getIdFieldName = getIdField.fold("ERROR")(_.name)

  private def getIdMethodName = {
    // TODO : throws exception instead of ERROR
    getIdField.map(n => s"get${n.name.capitalize}").getOrElse("ERROR")
  }

  def markField(field: JavaField, annotation: JavaAnnotation): Unit = {
    field.addAnnotation(annotation)
  }

  def getEntityClass: JavaClass = javaClass

  override def createDaoClass(): Try[JavaClass] = {
    daoBuilder.setName(javaClass.name + "Dao")
    val extension = new JavaClass("BasicDAO", JavaPackage("com.google.code.morphia.dao", "BasicDAO"))
    getIdField.map {
      idField =>
        extension.addGenericType(javaClass, idField.dataType)
    }.getOrElse {
      return Failure(new IdMissedException("[MongoAdapter] Id field is not specified"))
    }
    daoBuilder.addExtend(extension)
    Success(daoBuilder.getJavaClass)
  }

  override def createDaoFields(): Unit = {}

  private def createEntityParameter = FunctionParameter("entity", ObjectType(javaClass.getTypeName, javaClass.javaPackage))

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
      val removeParam = ChainedOperations(CreationOperation(basicDBObject), FunctionInvokeOperation("append",
        List(getIdFieldName, GetValueOperation(param.paramName.name, getIdMethodName))))
      builder.addOperation(ChainedOperations(FunctionInvokeOperation("getCollection"), FunctionInvokeOperation("remove", List(removeParam))))
    }.throwWrapped(daoException)
    Some(builder.getFunction)
  }

  override def createFindAll(): Option[JavaFunction] = {
    val builder = FunctionBuilder(PublicModifier, "findAll")
    builder.tryable {
      val chain = ChainedOperations(FunctionInvokeOperation("getCollection"), FunctionInvokeOperation("find",
        List(javaClass.asClassParam)), FunctionInvokeOperation("asList"))
      builder.addOperation(chain)
    }.throwWrapped(daoException)
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
        Some(builder.getFunction)
    }.getOrElse(None)
  }

  override def createDaoFactory(config: DBConfig): JavaClass = {
    import com.liberty.types.primitives._
    val builder = ClassBuilder(DAO_FACTORY_NAME)
    builder.addField(JavaField(DB_URL, StringType, PrivateStaticModifier, config.url))
    builder.addField(JavaField(DB_PORT, IntegerType, PrivateStaticModifier, config.port.toString))
    builder.addField(JavaField(DB_NAME, StringType, PrivateStaticModifier, config.database))
    builder.addField(JavaField("datastore", datastore, PrivateStaticModifier))
    builder.static {
      val mongoType = ObjectType("Mongo", JavaPackage("com.mongodb", "Mongo"))
      val mongo = Variable(mongoType)
      builder addOperation CreationOperation(mongoType, mongo, List(DB_URL.asValue, DB_PORT.asValue))
      val morphiaType = ObjectType("Morphia", JavaPackage("com.google.code.morphia", "Morphia"))
      val morphia = Variable(morphiaType)
      builder addOperation CreationOperation(morphiaType, morphia)
    }
    builder.getJavaClass
  }
}
