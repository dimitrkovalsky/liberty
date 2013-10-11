package com.liberty.builders

import com.liberty.entities.{JavaAnnotation, JavaField, JavaClass, JavaFunction}
import com.liberty.traits.JavaPackage

/**
 * User: Dimitr
 * Date: 15.09.13
 * Time: 10:47
 */
// TODO : Add abstract class support
// TODO: Add import support
class ClassBuilder {
    private val javaClass: JavaClass = new JavaClass

    def setName(name: String) = javaClass.name = name

    def addFunction(function: JavaFunction) {
        javaClass.addFunction(function)
    }

    def addField(field: JavaField) {
        javaClass.addField(field)
    }

    def addAnnotation(annotation:JavaAnnotation){
        javaClass.addAnnotation(annotation)
    }

    def addPackage(javaPackage:JavaPackage){
        javaClass.javaPackage = javaPackage
    }

    def getJavaClass: JavaClass = javaClass
}
