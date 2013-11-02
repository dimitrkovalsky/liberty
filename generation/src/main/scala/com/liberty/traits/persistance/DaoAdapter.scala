package com.liberty.traits.persistance

import com.liberty.entities.{JavaClass, JavaField, JavaAnnotation}

/**
 * User: Dimitr
 * Date: 27.10.13
 * Time: 12:05
 */
trait DaoAdapter extends Annotator{
    var datastoreName:String

    def getDatastoreAnnotation: JavaAnnotation

    def getIdAnnotation: JavaAnnotation

    def getIdField: Option[JavaField]

    def markField(field: JavaField, annotation: JavaAnnotation)

    def getJavaClass: JavaClass
}
