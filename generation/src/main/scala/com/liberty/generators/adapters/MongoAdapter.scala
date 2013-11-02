package com.liberty.generators.adapters

import com.liberty.traits.{JavaPackage, Accessible}
import com.liberty.entities.{JavaAnnotation, JavaField, JavaClass}
import com.liberty.traits.persistance.{Annotator, DaoAdapter}

/**
 * User: Dimitr
 * Date: 01.11.13
 * Time: 9:45
 */
class MongoAdapter(var javaClass: JavaClass) extends Accessible with DaoAdapter {
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

    def getJavaClass: JavaClass = javaClass

}
