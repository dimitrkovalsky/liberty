package com.liberty.generators

import com.liberty.builders.ClassBuilder
import com.liberty.common.ProjectConfig
import com.liberty.model.{JavaClass, JavaField, PrivateModifier}
import com.liberty.types.primitives.{IntegerType, StringType}
import org.junit.Test

/**
 * Created by Dmytro_Kovalskyi on 25.09.2014.
 */
class BeanGeneratorTest {
  private def getModel: JavaClass = {
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

  @Test def generateBean() {
    val generator = new BeanGenerator

  }
}
