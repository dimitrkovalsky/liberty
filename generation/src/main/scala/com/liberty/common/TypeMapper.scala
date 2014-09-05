package com.liberty.common

import com.liberty.types.{VoidType, DataType}
import com.liberty.types.primitives._
import com.liberty.types.standardTypes.DateType

/**
 * Created by Dmytro_Kovalskyi on 03.09.2014.
 */
object TypeMapper {

  def getStandardType(typeName: String): Option[DataType] = {
    typeName match {
      case "String" => Some(StringType)
      case "Char" | "char" => Some(CharType)
      case "Byte" | "byte" => Some(ByteType)
      case "Integer" | "int" => Some(IntegerType)
      case "Long" | "long" => Some(LongType)
      case "Boolean" | "boolean" => Some(BooleanType)
      case "Float" | "float" => Some(FloatType)
      case "Double" | "double" => Some(DateType)
      case "Date" => Some(DateType)
      case "void" => Some(new VoidType)
      case _ => None
    }
  }

  def isString(typeName: String): Boolean = {
    typeName match {
      case "String" => true
      case _ => false
    }
  }
}
