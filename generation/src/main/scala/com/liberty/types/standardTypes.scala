package com.liberty.types

import com.liberty.traits.JavaPackage

/**
 * Created by Dmytro_Kovalskyi on 04.07.2014.
 */
object standardTypes {

  case object DateType extends DataType("Date") with ConstructedType{
    javaPackage = JavaPackage("java.util", "Date")

    override def getDefaultValue: String = "null"

    override def getConstructor(): String = "Date"
  }

}
