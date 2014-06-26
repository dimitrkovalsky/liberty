package com.liberty


import com.liberty.traits.{Importable, JavaPackage, NoPackage}
import com.liberty.types.DataType


/**
 * User: Dimitr
 * Date: 02.09.13
 * Time: 10:24
 */
package object types {

  abstract class DataType(typeName: String) extends Importable {
    def getDefaultValue: String = "null"

    override def toString: String = typeName

    override def equals(obj: scala.Any): Boolean = {
      if (!obj.isInstanceOf[com.liberty.types.DataType])
        return false
      val dt = obj.asInstanceOf[com.liberty.types.DataType]
      this.typeName.equals(dt.toString)
    }

    def getTypeName = typeName
  }


  trait ConstructedType {
    def getConstructor(): String
  }

  case class UndefinedType() extends DataType("")

  case class VoidType() extends DataType("void")

  case class ObjectType(classType: String, jPackage: JavaPackage = new NoPackage) extends DataType(classType) with ConstructedType with Importable {
    this.javaPackage = jPackage

    def getConstructor(): String = classType
  }

  case class SimpleObjectType(className: String, packagePath: String)

}

case class StubType(stubType: String) extends DataType(stubType)



