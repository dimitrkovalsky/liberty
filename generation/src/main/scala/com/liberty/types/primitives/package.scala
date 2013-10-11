package com.liberty.types

import com.liberty.traits.JavaPackage


/**
 * User: Dimitr
 * Date: 02.09.13
 * Time: 11:15
 */
package object primitives {

    abstract class PrimitiveType(dataType: String) extends DataType(dataType) {
        javaPackage = JavaPackage("java.util.lang", dataType)
    }

    case object ByteType extends PrimitiveType("Byte") {
        override def getDefaultValue: String = "0"
    }

    case object BooleanType extends PrimitiveType("Boolean") {
        override def getDefaultValue: String = "false"
    }

    case object CharType extends PrimitiveType("Char") {
        override def getDefaultValue: String = "\'\'"
    }

    case object StringType extends PrimitiveType("String") {
        override def getDefaultValue: String = "\"\""
    }

    case object IntegerType extends PrimitiveType("Integer") {
        override def getDefaultValue: String = "0"
    }

    case object LongType extends PrimitiveType("Long") {
        override def getDefaultValue: String = "0"
    }

    case object FloatType extends PrimitiveType("Float") {
        override def getDefaultValue: String = "0f"
    }

    case object DoubleType extends PrimitiveType("Double") {
        override def getDefaultValue: String = "0.0"
    }

}
