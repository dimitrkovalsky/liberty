package com.liberty


import com.liberty.traits.Importable
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
            if(!obj.isInstanceOf[com.liberty.types.DataType])
                return false
            val dt = obj.asInstanceOf[com.liberty.types.DataType]
            this.typeName.equals(dt.toString)
        }
    }


    trait ConstructedType {
        def getConstructor(): String
    }

    case class UndefinedType() extends DataType("")

    case class VoidType() extends DataType("void")

    case class ObjectType(classType: String) extends DataType(classType) with ConstructedType {
        def getConstructor(): String = classType
    }

}

case class StubType(stubType:String) extends DataType(stubType)


