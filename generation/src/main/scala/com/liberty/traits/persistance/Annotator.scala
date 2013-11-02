package com.liberty.traits.persistance

/**
 * User: Dimitr
 * Date: 02.11.13
 * Time: 9:45
 */
trait Annotator {
    this: DaoAdapter =>

    def addIdAnnotation() {
        getIdField.map {
            field => field.addAnnotation(getIdAnnotation)
        }
    }

    def addDatastoreAnnotation() {
        getJavaClass.addAnnotation(getDatastoreAnnotation)
    }

    def annotateClass() {
        addDatastoreAnnotation()
        addIdAnnotation()
    }
}
