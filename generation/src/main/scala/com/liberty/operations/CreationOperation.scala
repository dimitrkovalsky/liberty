package com.liberty.operations

import com.liberty.types.{primitives, ConstructedType, DataType}

/**
 * User: Dimitr
 * Date: 07.11.13
 * Time: 10:24
 */
// TODO : Validate variable name
case class CreationOperation(dataType: DataType, variableName: Variable = Variable(""),
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
            case Variable("") => Some(construct)
            case _ => Some(s"${dataType.toString} $variableName = $construct")
        }
    }
}
