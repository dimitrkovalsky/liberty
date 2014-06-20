package com.liberty.generators.adapters

import com.liberty.common.Implicits
import com.liberty.exceptions.IdMissedException
import com.liberty.traits.{JavaPackage, Accessible}
import com.liberty.model._
import com.liberty.traits.persistance.{Annotator, DaoAdapter}
import com.liberty.builders.{FunctionBuilder, ClassBuilder}
import com.liberty.model.JavaAnnotation
import com.liberty.traits.JavaPackage
import com.liberty.model.JavaField
import com.liberty.operations.{ReturnOperation, Variable, FunctionType, SelfFunctionInvokeOperation}
import com.liberty.types.ObjectType
import Implicits._

import scala.util.{Success, Failure, Try}

/**
 * User: Dimitr
 * Date: 01.11.13
 * Time: 9:45
 */
class MongoAdapter(var javaClass: JavaClass) extends DaoAdapter {
  private val morphiaPackage: String = "com.google.code.morphia.annotations"
  var datastoreName: String = javaClass.name.toLowerCase

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

  override def createDaoConstructor() {
    val builder = new FunctionBuilder
    builder.setName(daoBuilder.javaClass.name)
    builder.addModifier(PublicModifier)
    val variable = new Variable("datastore")
    builder.addParam(FunctionParameter(variable, ObjectType("Datastore", JavaPackage("com.google.code.morphia", "Datastore"))))
    builder.addOperation(new SelfFunctionInvokeOperation(FunctionType.SUPER_CONSTRUCTOR, parameters = List(variable)))
    daoBuilder.addFunction(builder.getFunction)
  }


  override def createInsert() = {
    val param = FunctionParameter("entity", ObjectType(javaClass.getTypeName, javaClass.javaPackage))
    val builder = FunctionBuilder(PublicModifier, "insert", param)
    builder.addThrow(StandardExceptions.Exception)
    builder.addOperation(SelfFunctionInvokeOperation(FunctionType.SUPER_FUNCTION, "save", List(param)))
    Some(builder.getFunction)
  }

  override def createFind() = None

  override def createUpdate() = None

  override def createDelete() = None

}
