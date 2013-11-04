package com.liberty.traits

import com.liberty.types.DataType

/**
 * User: Dimitr
 * Date: 04.11.13
 * Time: 10:14
 */
trait Generalizable {
    var generics: List[DataType] = Nil

    def addGenericType(dataType: DataType) {
        if (!generics.contains(dataType))
            generics = generics ::: List(dataType)
    }

    def getGenericString: String = {
        generics match {
            case Nil => ""
            case _ => s"<${generics.map(_.toString).mkString(", ")}>"
        }
    }
}
