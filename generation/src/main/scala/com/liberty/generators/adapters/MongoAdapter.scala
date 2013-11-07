package com.liberty.generators.adapters

import com.liberty.traits.{JavaPackage, Accessible}
import com.liberty.entities._
import com.liberty.traits.persistance.{Annotator, DaoAdapter}
import com.liberty.builders.{FunctionBuilder, ClassBuilder}
import com.liberty.entities.JavaAnnotation
import com.liberty.traits.JavaPackage
import com.liberty.entities.JavaField
import com.liberty.operations.{Variable, FunctionType, SelfFunctionInvokeOperation}

/**
 * User: Dimitr
 * Date: 01.11.13
 * Time: 9:45
 */
class MongoAdapter(var javaClass: JavaClass) extends DaoAdapter {
    private val morphiaPackage: JavaPackage = JavaPackage("com.google.code.morphia.annotations")
    var datastoreName: String = javaClass.name.toLowerCase

    def getAccessible: JavaClass = getAccessible(javaClass)

    def addAccessors() = super.addAccessors(javaClass)

    def getDatastoreAnnotation: JavaAnnotation = {
        JavaAnnotation("Entity", morphiaPackage)("value", datastoreName)("noClassnameStored", "true")
    }

    def getIdAnnotation: JavaAnnotation = {
        JavaAnnotation("Id", morphiaPackage)
    }

    // TODO : Realize good id field lookup
    def getIdField: Option[JavaField] = {
        javaClass.fields.find(field => field.id)
    }

    def markField(field: JavaField, annotation: JavaAnnotation): Unit = {
        field.addAnnotation(annotation)
    }

    def getEntityClass: JavaClass = javaClass

    override def createDaoClass(): Either[String, JavaClass] = {
        daoBuilder.setName(javaClass.name + "DAO")
        val extension = new JavaClass("BasicDAO", JavaPackage("com.google.code.morphia.dao"))
        getIdField.map {
            idField => extension.addGenericType(idField.dataType, javaClass)
        }.getOrElse {
            return Left("[MongoAdapter] Id field is not specified")
        }
        daoBuilder.addExtend(extension)
        Right(daoBuilder.getJavaClass)
    }

    override def createDaoFields(): Unit = {}

    override def createDaoConstructor() {
        val builder = new FunctionBuilder
        builder.setName(daoBuilder.javaClass.name)
        builder.addModifier(PublicModifier)
        val variable = new Variable("datastore")
        builder
            .addParam(FunctionParameter(variable, new JavaClass("Datastore", JavaPackage("com.google.code.morphia"))))
        builder
            .addOperation(new SelfFunctionInvokeOperation(FunctionType.SUPER_CONSTRUCTOR, parameters = List(variable)))
        daoBuilder.addFunction(builder.getFunction)
    }


    override def createInsert(): JavaFunction = {
        null
    }

    override def createFind(): JavaFunction = ???

    override def createUpdate(): JavaFunction = ???

    override def createDelete(): JavaFunction = ???

}
