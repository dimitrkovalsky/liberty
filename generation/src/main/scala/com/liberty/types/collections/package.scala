package com.liberty.types

import com.liberty.traits.JavaPackage


/**
 * User: Dimitr
 * Date: 02.09.13
 * Time: 11:15
 */
package object collections {

    abstract class CollectionType(collectionName: String) extends DataType("") {
        javaPackage = JavaPackage("java.util")
        override def getDefaultValue: String = "null"
    }

    abstract class ListCollection(collectionName: String, collectionType: DataType)
        extends CollectionType(collectionName) {
        override def toString: String = String.format("%s<%s>", collectionName, collectionType.toString)
    }

    abstract class SetCollection(collectionName: String, collectionType: DataType)
        extends CollectionType(collectionName) {
        override def toString: String = String.format("%s<%s>", collectionName, collectionType.toString)
    }

    abstract class MapCollection(collectionName: String, key: DataType, value: DataType)
        extends CollectionType(collectionName) {
        override def toString: String = String.format("%s<%s, %s>", collectionName, key.toString, value.toString)
    }

    case class ListType(collectionType: DataType) extends ListCollection("List", collectionType)

    case class ArrayListType(collectionType: DataType) extends ListCollection("ArrayList", collectionType) with ConstructedType{
        def getConstructor(): String = "ArrayList"
    }

    case class SetType(collectionType: DataType) extends SetCollection("Set", collectionType)

    case class MapType(key: DataType, value: DataType) extends MapCollection("Map", key, value)

}
