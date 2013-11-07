package com.liberty.generators.adapters

import com.liberty.traits.persistance.DaoAdapter
import com.liberty.entities.{JavaFunction, JavaAnnotation, JavaField, JavaClass}

/**
 * User: Dimitr
 * Date: 04.11.13
 * Time: 9:27
 */
case class StubAdapter() extends DaoAdapter{
    var javaClass: JavaClass = _
    var datastoreName: String = _

    def getDatastoreAnnotation: JavaAnnotation = ???

    def getIdAnnotation: JavaAnnotation = ???

    def getIdField: Option[JavaField] = ???

    def markField(field: JavaField, annotation: JavaAnnotation): Unit = ???

    def getEntityClass: JavaClass = ???

    def createDaoFields(): Unit = ???

    def createDaoClass(): Either[String, JavaClass] = ???

    def createDaoConstructor(): Unit = ???

    def createDelete(): JavaFunction = ???

    def createFind(): JavaFunction = ???

    def createUpdate(): JavaFunction = ???

    def createInsert(): JavaFunction = ???
}

