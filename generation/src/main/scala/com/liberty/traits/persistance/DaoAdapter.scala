package com.liberty.traits.persistance

import com.liberty.model.{JavaFunction, JavaClass, JavaField, JavaAnnotation}
import com.liberty.traits.Accessible
import com.liberty.builders.ClassBuilder

/**
 * User: Dimitr
 * Date: 27.10.13
 * Time: 12:05
 */
trait DaoAdapter extends Annotator with CRUDable with Accessible {
    this: {var javaClass: JavaClass} =>
    protected var daoBuilder: ClassBuilder = new ClassBuilder()

    var datastoreName: String

    def getDatastoreAnnotation: JavaAnnotation

    def getIdAnnotation: JavaAnnotation

    def getIdField: Option[JavaField]

    def markField(field: JavaField, annotation: JavaAnnotation)

    def getEntityClass: JavaClass

    def createDaoFields()

    def createDaoClass(): Either[String, JavaClass]

    def createDaoConstructor()

    def getDaoClass: JavaClass = daoBuilder.getJavaClass

    def createDAO: Either[String, JavaClass] = {
        val result = createDaoClass()
        if (result.isLeft)
            return result

        createDaoFields()
        createDaoConstructor()
        daoBuilder.addFunctions(createMethods())
        Right(daoBuilder.getJavaClass)
    }
}
