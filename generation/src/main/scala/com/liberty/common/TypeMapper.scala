package com.liberty.common

import com.liberty.traits.JavaPackage
import com.liberty.types.collections._
import com.liberty.types.primitives._
import com.liberty.types.standardTypes.DateType
import com.liberty.types.{DataType, ObjectType, VoidType}


/**
 * Uses for type mapping
 * Created by Dmytro_Kovalskyi on 03.09.2014.
 */
object TypeMapper {
  /**
   * Changes complex dataTypes
   * <i>IDepartmentBean [model : Department] => I[newModel]Bean</i>
   * @param oldModel  Department from previous line
   */
  def changeComplexType(oldType: DataType, oldModel: String, newModel: String): DataType = {
    if (oldType.toString.contains(oldModel)) {
      val name = oldType.getTypeName.replace(oldModel, newModel)
      val newPackage = JavaPackage(oldType.javaPackage.packagePath, oldType.javaPackage.packageClass.replace(oldModel, newModel))
      ObjectType(name, newPackage)
    }
    else
      oldType
  }


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

  private def changeOneContainedCollectionType(collection: CollectionType, newType: DataType): Option[CollectionType] = {
    collection match {
      case ListType(_) => Some(ListType(newType))
      case ArrayListType(_) => Some(ArrayListType(newType))
      case LinkedListType(_) => Some(LinkedListType(newType))
      case SetType(_) => Some(SetType(newType))
      case TreeSetType(_) => Some(TreeSetType(newType))
      case HashSetType(_) => Some(HashSetType(newType))
      case SortedSetType(_) => Some(SortedSetType(newType))
      case _ => None
    }
  }

  private def changeTwoContainedCollectionType(collection: CollectionType, oldType: DataType, newType: DataType): Option[CollectionType] = {
    collection match {
      case MapType(key, value) if oldType.equals(key) => Some(MapType(newType, value))
      case MapType(key, value) if oldType.equals(value) => Some(MapType(key, newType))
      case MapType(key, value) if oldType.equals(key) & oldType.equals(value) => Some(MapType(newType, newType))
      case HashMapType(key, value) if oldType.equals(key) => Some(HashMapType(newType, value))
      case HashMapType(key, value) if oldType.equals(value) => Some(HashMapType(key, newType))
      case HashMapType(key, value) if oldType.equals(key) & oldType.equals(value) => Some(HashMapType(newType, newType))
      case _ => None
    }
  }

  def isCollection(dataType: DataType): Boolean = {
    dataType.isInstanceOf[CollectionType]
  }

  def changeType(collection: CollectionType, oldType: DataType, newType: DataType): Option[CollectionType] = {
    if (isTwoContainedCollection(collection))
      changeTwoContainedCollectionType(collection, oldType, newType)
    else
      changeOneContainedCollectionType(collection, newType)
  }

  def getStandardTwoContainedCollectionTypes(collectionName: String, key: DataType, value: DataType): Option[DataType] = {
    collectionName match {
      case "Map" => Some(MapType(key, value))
      case "HashMap" => Some(HashMapType(key, value))
      case _ => None
    }
  }

  private def isTwoContainedCollection(collection: CollectionType) = {
    collection match {
      case MapType(_, _) | HashMapType(_, _) => true
      case _ => false
    }
  }
}
