package com.liberty.common

import com.liberty.builders.ClassBuilder
import com.liberty.model.{JavaClass, JavaField, PrivateModifier}
import com.liberty.types.primitives.{IntegerType, StringType}

/**
 * Created by Dmytro_Kovalskyi on 26.09.2014.
 */
object TestModels {
  def getSubject: JavaClass = {
    val basePackage = ProjectConfig.basePackage
    val builder = new ClassBuilder
    builder.setName("Subject")
    builder.addField(JavaField("id", IntegerType, PrivateModifier))
    builder.addField(JavaField("name", StringType, PrivateModifier))
    builder.addField(JavaField("lecturer", StringType, PrivateModifier))
    builder.addField(JavaField("departmentId", StringType, PrivateModifier))
    builder.addPackage(basePackage.nested("models", "Subject"))
    builder.getJavaClass
  }
}
