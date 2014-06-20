package com.liberty.generators.adapters

import com.liberty.traits.persistance.DaoAdapter
import com.liberty.model.{JavaFunction, JavaAnnotation, JavaField, JavaClass}

import scala.util.Try

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

    def createDaoClass(): Try[JavaClass] = ???

    def createDaoConstructor(): Unit = ???

    def createDelete() = ???

    def createFind() = ???

    def createUpdate() = ???

    def createInsert() = ???
}

