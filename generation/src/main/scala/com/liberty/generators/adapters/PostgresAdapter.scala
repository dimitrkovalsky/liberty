package com.liberty.generators.adapters

import com.liberty.builders.FunctionBuilder
import com.liberty.common.Implicits._
import com.liberty.model._
import com.liberty.operations._
import com.liberty.traits.persistance.DaoAdapter
import com.liberty.traits.{JavaPackage, LocationPackage}
import com.liberty.types.ObjectType
import com.liberty.types.collections.ListType

import scala.util.{Success, Try}

/**
 * Created by Dmytro_Kovalskyi on 24.07.2014.
 */
class PostgresAdapter(var javaClass: JavaClass, bPackage: LocationPackage) extends DaoAdapter {
  val basePackage = bPackage
  val persistencePackage = LocationPackage("javax.persistence")
  var dao: Option[JavaClass] = None
  private val daoException = JavaException("DaoException", basePackage.nested("errors", "DaoException"))
  val entityManager = ObjectType("EntityManager", JavaPackage("javax.persistence", "EntityManager"))
  val entityManagerField = JavaField("entityManager", entityManager, PrivateModifier)

  def addAccessors() = super.addAccessors(javaClass)

  /**
   * Returns marked entity for appropriate database
   * @return
   */
  override def createEntity: JavaClass = {
    addAccessors()
    annotateClass()
    javaClass
  }


  def getDatastoreAnnotation: JavaAnnotation = {
    SimpleAnnotation("Entity", persistencePackage)("value", datastoreName)
  }

  def getIdAnnotation: JavaAnnotation = {
    SimpleAnnotation("Id", persistencePackage)
  }

  override var datastoreName: String = javaClass.name.firstToLowerCase

  override def getEntityClass: JavaClass = javaClass

  override protected def createDaoClassBase(): Try[JavaClass] = {
    daoBuilder.setName(getDaoName)
    daoBuilder.addPackage(basePackage.nested("dao", getDaoName))
    val daoClazz = daoBuilder.getJavaClass
    dao = Some(daoClazz)
    Success(daoClazz)
  }

  override def createDaoConstructor(): Unit = {
    val builder = new FunctionBuilder
    builder.setName(daoBuilder.javaClass.name)
    builder.addModifier(PublicModifier)
    val variable = new Variable("em")
    builder.addParam(FunctionParameter(variable, entityManager))
    builder.addOperation(SetValueOperation(entityManagerField, variable))
    daoBuilder.addFunction(builder.getFunction)
  }

  override def createDaoFields(): Unit = {
    daoBuilder.addField(entityManagerField)
  }

  private def createEntityParameter = FunctionParameter("entity", ObjectType(javaClass.getTypeName, javaClass.javaPackage))

  private val getEntityType = ObjectType(javaClass.getTypeName, javaClass.javaPackage)

  override def getDaoCreationFunction: Option[JavaFunction] = ???

  override def getFactoryCreator: FactoryCreator = ???

  override def createUpdate(): Option[JavaFunction] = {
    val param = createEntityParameter
    val builder = FunctionBuilder(PublicModifier, "update", param)
    builder.tryable {
      builder.addOperation(startTransaction)
      builder.addOperation(ObjectFunctionInvokeOperation(Variable(entityManagerField), "merge", List(param)))
      builder.addOperation(commitTransaction)
    }.throwWrapped(daoException)
    Some(builder.getFunction)
  }

  override def createInsert(): Option[JavaFunction] = {
    val param = createEntityParameter
    val builder = FunctionBuilder(PublicModifier, "insert", param)
    builder.tryable {
      builder.addOperation(startTransaction)
      builder.addOperation(ObjectFunctionInvokeOperation(Variable(entityManagerField), "persist", List(param)))
      builder.addOperation(commitTransaction)
    }.throwWrapped(daoException)
    Some(builder.getFunction)
  }

  private def startTransaction = {
    ChainedOperations(GetValueOperation(entityManagerField, "getTransaction"), FunctionInvokeOperation("begin"))
  }

  private def commitTransaction = {
    ChainedOperations(GetValueOperation(entityManagerField, "getTransaction"), FunctionInvokeOperation("commit"))
  }

  override def createDelete(): Option[JavaFunction] = {
    val param = createEntityParameter
    val builder = FunctionBuilder(PublicModifier, "delete", param)
    builder.tryable {
      builder.addOperation(startTransaction)
      builder.addOperation(ObjectFunctionInvokeOperation(Variable(entityManagerField), "remove", List(param)))
      builder.addOperation(commitTransaction)
    }.throwWrapped(daoException)
    Some(builder.getFunction)
  }

  override def createFindAll(): Option[JavaFunction] = {
    val builder = FunctionBuilder(PublicModifier, "findAll")
    builder.tryable {
      val pattern = s"CriteriaQuery<${getEntityClass.getTypeName}> criteria = entityManager.getCriteriaBuilder().createQuery(${getEntityClass.getTypeName}.class);\n\t\t" +
        s"criteria.select(criteria.from(${getEntityClass.getTypeName}.class));\n\t\t" +
        s"return entityManager.createQuery(criteria).getResultList()"
      val packages = JavaPackage("javax.persistence.criteria", "CriteriaQuery") :: Nil
      builder addOperation PatternOperation(pattern, packages)
    }.throwWrapped(daoException)
    builder.addReturn(ListType(getEntityType))
    Some(builder.getFunction)
  }

  override def createFind(): Option[JavaFunction] = {
    val param = createEntityParameter
    val builder = FunctionBuilder(PublicModifier, "find", param)
    builder.wrapable(daoException) {
      val ret = ReturnOperation(ObjectFunctionInvokeOperation(Variable(entityManagerField), "find", List(Value(getEntityClass.getTypeName + ".class"),
        GetValueOperation(param.paramName.name, getIdMethodName))))
      builder.addOperation(ret)
    }
    builder.addReturn(param.paramType)
    Some(builder.getFunction)
  }

  override def createFindById(): Option[JavaFunction] = {
    getIdField.map {
      id =>
        val param = FunctionParameter(id)
        val builder = FunctionBuilder(PublicModifier, s"findBy${id.name.capitalize}", param)
        builder.tryable {
          builder.addOperation(ReturnOperation(
            ObjectFunctionInvokeOperation(Variable(entityManagerField), "find", List(Value(getEntityClass.getTypeName + ".class"), param))))
        }.throwWrapped(daoException)
        builder.addReturn(getEntityType)
        Some(builder.getFunction)
    }.getOrElse(None)
  }
}
