package com.liberty


import com.liberty.types.primitives.PrimitiveType
import com.liberty.types.collections.CollectionType
import com.liberty.traits.Importable


/**
 * User: Dimitr
 * Date: 02.09.13
 * Time: 10:24
 */
package object types {

    abstract class DataType(dataType: String) extends Importable {
        def getDefaultValue: String = "null"

        override def toString: String = dataType
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


