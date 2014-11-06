package com.liberty.generators

import com.liberty.builders.ClassBuilder
import com.liberty.common.{ProjectConfig, ClassMapper}
import com.liberty.model.JavaClass
import com.liberty.traits.JavaPackage

/**
 * User: Dmytro_Kovalskyi
 * Date: 05.11.2014
 * Time: 17:33
 */
class AdditionalClassGenerator extends HybridGenerator {
  def createException(name: String, baseException: String = "Exception",
                      pack: JavaPackage = ProjectConfig.basePackage.nested("errors")): JavaClass = {
    val templatePath = baseException match {
      case "Exception" => "errors.base_exception_template"
      case _ => "errors.base_another_template"
    }

    val template = loadClass(templatePath, "errors.base_package")
    val clazz = ClassMapper.changeSimpleModel(template, name, pack, baseException)
    clazz
  }
}

