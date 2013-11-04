package com.liberty.traits.persistance

import com.liberty.entities.{JavaFunction, JavaClass, JavaField, JavaAnnotation}
import com.liberty.traits.Accessible
import com.liberty.builders.ClassBuilder

/**
 * User: Dimitr
 * Date: 27.10.13
 * Time: 12:05
 */
trait DaoAdapter extends Annotator with CRUDable with Accessible {
    this: {var javaClass: JavaClass} =>
    var builder: ClassBuilder = new ClassBuilder()

    var datastoreName: String

    def getDatastoreAnnotation: JavaAnnotation

    def getIdAnnotation: JavaAnnotation

    def getIdField: Option[JavaField]

    def markField(field: JavaField, annotation: JavaAnnotation)

    def getEntityClass: JavaClass

    def createDaoFields()

    def createDaoClass()
    
    def getDaoClass: JavaClass = builder.getJavaClass
}
