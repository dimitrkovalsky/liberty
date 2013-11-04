package com.liberty.builders

import com.liberty.entities._
import com.liberty.traits.JavaPackage
import com.liberty.entities.JavaAnnotation
import com.liberty.traits.JavaPackage
import com.liberty.entities.JavaField
import scala.Some
import com.liberty.types.DataType

/**
 * User: Dimitr
 * Date: 15.09.13
 * Time: 10:47
 */
// TODO : Add abstract class support
class ClassBuilder (var javaClass: JavaClass = new JavaClass) {

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

    def addImplements(interface: JavaInterface) {
        javaClass.addImplements(interface)
    }

    def addExtend(clazz: JavaClass) {
       javaClass.addExtend(clazz)
    }

    def removeImplements(interfaceName: JavaInterface): Option[JavaInterface] = {
        javaClass.removeImplements(interfaceName)
    }

    def removeExtend(): Option[JavaClass] = {
        javaClass.removeExtend()
    }

    def addGeneric(generic: DataType) {
        javaClass.addGenericType(generic)
    }

    def getJavaClass: JavaClass = javaClass
}

object ClassBuilder {
    def apply() : ClassBuilder = new ClassBuilder()
}