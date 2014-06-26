package com.liberty.common

import com.liberty.model.{FunctionParameter, JavaAnnotation, SimpleAnnotation}
import com.liberty.operations.{Expression, Value, Variable}
import com.liberty.traits.JavaPackage
import com.liberty.types.{ObjectType, SimpleObjectType}

/**
 * Created by Dmytro_Kovalskyi on 20.06.2014.
 */
object Implicits {
  implicit def functionParameterToExpression(param: FunctionParameter): Expression = param.paramName

  implicit def simpleAnnotationToJavaAnnotation(annotation: SimpleAnnotation): JavaAnnotation =
    JavaAnnotation(annotation.name, JavaPackage(annotation.pack, annotation.name))

  implicit def variableToOption(v: Variable): Option[Variable] = Some(v)

  implicit def simpleObjectTypeToObjectType(t: SimpleObjectType): ObjectType = ObjectType(t.className, JavaPackage(t.packagePath, t.className))

  implicit def stringToValue(s: String): Value = Value(s)
}
