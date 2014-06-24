package com.liberty.common

import com.liberty.model.{FunctionParameter, JavaAnnotation, SimpleAnnotation}
import com.liberty.operations.{Expression, Variable}
import com.liberty.traits.JavaPackage

/**
 * Created by Dmytro_Kovalskyi on 20.06.2014.
 */
object Implicits {
  implicit def functionParameterToExpression(param: FunctionParameter): Expression = param.paramName

  implicit def simpleAnnotationToJavaAnnotation(annotation: SimpleAnnotation): JavaAnnotation =
    JavaAnnotation(annotation.name, JavaPackage(annotation.pack, annotation.name))

  implicit def variableToOption(v: Variable): Option[Variable] = Some(v)
}
