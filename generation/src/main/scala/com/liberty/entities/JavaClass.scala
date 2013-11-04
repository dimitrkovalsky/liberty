package com.liberty.entities

import com.liberty.types.DataType
import com.liberty.patterns
import com.liberty.traits._
import com.liberty.entities.JavaAnnotation
import com.liberty.entities.JavaField
import scala.Some
import com.liberty.traits.JavaPackage

/**
 * User: Dimitr
 * Date: 15.09.13
 * Time: 10:55
 */
// TODO: Add support of function and field changing and removing
// TODO: Add constructor support
// TODO: Add name validation
class JavaClass(jPackage: JavaPackage = new NoPackage)
    extends Annotatable with Importable with Cloneable with Generalizable {

    this.javaPackage = jPackage

    override def clone(): AnyRef = super.clone()

    var name: String = ""
    var functions: List[JavaFunction] = Nil
    var fields: List[JavaField] = Nil
    var implementList: List[JavaInterface] = Nil
    var extendClass: Option[JavaClass] = None

    def addField(field: JavaField) {
        fields = fields ::: List(field)
    }

    def addFunction(function: JavaFunction) {
        functions = functions ::: List(function)
    }

    def addImplements(interface: JavaInterface) {
        implementList = implementList ::: List(interface)
    }

    def addExtend(clazz: JavaClass) {
        extendClass = Some(clazz)
    }

    def removeImplements(interfaceName: JavaInterface): Option[JavaInterface] = {
        val parts = implementList.partition(ji => !ji.name.equals(interfaceName))
        implementList = parts._1
        parts._2 match {
            case Nil => None
            case x :: xs => Some(x)
        }
    }

    def removeExtend(): Option[JavaClass] = {
        val extend = extendClass
        extendClass = None
        extend
    }

    private def getAllImports: String = {
        var set: Set[JavaPackage] = Set()
        fields.foreach(f => f.getPackages.foreach(p => set += p))
        functions.foreach(f => set = set ++ f.getPackages)
        annotations.map(ann => ann.javaPackage).filter(p => !p.isInstanceOf[NoPackage]).foreach(p => set += p)
        set.map(jp => jp.getImport).mkString("\n")
    }

    private def getInheritanceString: String = {
        val extend = extendClass match {
            case None => ""
            case Some(clazz: JavaClass) => " extends " + clazz.name + clazz.getInheritanceString
        }

        val impl = implementList match {
            case Nil => ""
            case x :: xs => " implements " + implementList.map(interface => interface.name + interface.getGenericString)
                .mkString(", ")
        }

        extend + impl
    }

    def functionExist(functionName: String) = {
        functions.contains(JavaFunction(functionName))
    }

    override def toString: String = {
        val fieldsString = fields.map(_.toClassPart()) match {
            case Nil => ""
            case list: List[String] => list.mkString(";\n\t") + ";"
        }
        val functionsString = functions.map(_.toClassPart()) match {
            case Nil => ""
            case list: List[String] => list.mkString("\n\n")
        }

        patterns.JavaClassPattern(getPackageString, getAllImports, annotationToString(inline = false), name,
            getGenericString, getInheritanceString, fieldsString, functionsString)
    }
}


trait ClassPart {
    def toClassPart(shift: String = "\t"): String
}

case class JavaField(name: String, dataType: DataType, var modifier: Modifier = DefaultModifier, var value: String = "",
                     var id: Boolean = false)
    extends ClassPart with Annotatable {


    def apply(annotation: JavaAnnotation): JavaField = {
        addAnnotation(annotation)
        this
    }

    if (value.isEmpty)
        value = dataType.getDefaultValue


    override def toString: String = {
        s"${
            annotationToPostShiftedString("\t")
        }${
            if (modifier.toString.isEmpty) "" else modifier.toString + " "
        }${dataType.toString} $name = $value"
    }

    def getInternalName = "this." + name

    override def toClassPart(shift: String = "\t"): String = toString

    def getPackages: List[JavaPackage] = dataType.javaPackage :: annotations.map(ann => ann.javaPackage)
        .filter(p => !p.isInstanceOf[NoPackage])
}