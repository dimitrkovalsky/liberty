package com.liberty.operations

import com.liberty.types.{ConstructedType, DataType, primitives}

/**
 * User: Dimitr
 * Date: 07.11.13
 * Time: 10:24
 */
// TODO : Validate variable name
case class CreationOperation(dataType: DataType, variableName: Option[Variable] = None,
                             var params: List[Expression] = Nil)
  extends Operation {
  // TODO: realize creation of primitives with params
  def createFromDataType(dataType: DataType): String = dataType match {
    case t: ConstructedType => s"new ${new ConstructorInvokeOperation(t, params).execute().get}"
    case prim: primitives.PrimitiveType => prim.getDefaultValue
  }

  override def execute(): Option[String] = {
    val construct = createFromDataType(dataType)
    variableName match {
      case None => Some(construct)
      case Some(n) => Some(s"${dataType.toString} $n = $construct")
      case _ => None
    }
  }
}
