package com.liberty.types


/**
 * User: Dimitr
 * Date: 02.09.13
 * Time: 11:15
 */
package object primitives {

    abstract class PrimitiveType(dataType: String) extends DataType(dataType) {
        def getDefaultValue: String
    }

    case object ByteType extends PrimitiveType("Byte"){
        def getDefaultValue: String = "0"
    }

    case object BooleanType extends PrimitiveType("Boolean"){
        def getDefaultValue: String = "false"
    }

    case object CharType extends PrimitiveType("Char"){
        def getDefaultValue: String = "\'\'"
    }

    case object StringType extends PrimitiveType("String"){
        def getDefaultValue: String = "\"\""
    }

    case object IntegerType extends PrimitiveType("Integer"){
        def getDefaultValue: String = "0"
    }

    case object LongType extends PrimitiveType("Long"){
        def getDefaultValue: String = "0"
    }

    case object FloatType extends PrimitiveType("Float"){
        def getDefaultValue: String = "0f"
    }

    case object DoubleType extends PrimitiveType("Double"){
        def getDefaultValue: String = "0.0"
    }

}
