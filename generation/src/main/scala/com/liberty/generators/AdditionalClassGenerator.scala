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
  def createException(name: String, pack: JavaPackage, baseException: String = "Exception"): JavaClass = {
    val templatePath = baseException match {
      case "Exception" => "errors.base_exception_template"
      case _ => "errors.DaoException"
    }

    val template = loadClass(templatePath, "errors.base_package")

    null
  }
}

object Run {
  def main(args: Array[String]) {
    val g = new AdditionalClassGenerator
    val r = g.createException("ApplicationException", ProjectConfig.basePackage.nested("errors"))
  }
}
