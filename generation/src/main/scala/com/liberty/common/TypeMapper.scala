package com.liberty.common

import com.liberty.types.collections._
import com.liberty.types.primitives._
import com.liberty.types.standardTypes.DateType
import com.liberty.types.{DataType, VoidType}

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

  /**
   * Returns list of generic types. If it is not generic returns Nil
   */
  def getStandardOneContainedCollectionTypes(collectionName: String, entityType: DataType): Option[DataType] = {
    collectionName match {
      case "List" => Some(ListType(entityType))
      case "ArrayList" => Some(ArrayListType(entityType))
      case "LinkedList" => Some(LinkedListType(entityType))
      case "Set" => Some(SetType(entityType))
      case "TreeSet" => Some(TreeSetType(entityType))
      case "HashSet" => Some(HashSetType(entityType))
      case "SortedSet" => Some(SortedSetType(entityType))
      case _ => None
    }
  }

  def getStandardTwoContainedCollectionTypes(collectionName: String, key: DataType, value: DataType): Option[DataType] = {
    collectionName match {
      case "Map" => Some(MapType(key, value))
      case "HashMap" => Some(HashMapType(key, value))
      case _ => None
    }
  }
}
