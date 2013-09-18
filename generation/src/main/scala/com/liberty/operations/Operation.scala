package com.liberty.operations

import com.liberty.types.{primitives, ConstructedType, DataType}

/**
 * User: dkovalskyi
 * Date: 06.09.13
 * Time: 16:51
 */
trait Expression

// TODO : Provide the possibility to pass function into another function
trait Operation extends Expression {
    def execute(): Option[String]

    override def toString: String = execute() match {
        case Some(res: String) => res
        case _ => "// Empty operation"
    }
}

case class Variable(name: String) extends Expression {
    override def toString: String = name
}

case class NoneExpressiion extends Expression

case class UnaryOperation(operator: String, arg: Expression) extends Expression

case class BinaryOp(operator: String, left: Expression, right: Expression) extends Expression

// TODO: creation variables without expressions like :<<< List<Integer> list; >>>
case class FunctionInvokeOperation(functionName: String, var params: List[Expression] = Nil,
                                   result: Variable = Variable("")) extends Operation {
    override def execute(): Option[String] = {
        val invokeParams = params match {
            case x :: xs => params.mkString(", ")
            case _ => ""
        }
        result match {
            case Variable("") => Some(s"$functionName($invokeParams)")
            case v: Variable => Some(s"${v.name} = $functionName($invokeParams)")
            case _ => None
        }
    }
}

case class VariableDeclarationOperation(v: Variable, dataType: DataType, expr: Expression = new NoneExpressiion)
    extends Operation {
    override def execute(): Option[String] = {
        val rightPart = expr match {
            case e: NoneExpressiion => ""
            case variable: Variable => " = " + variable.toString
            case op: Operation => op.execute() match {
                case Some(s: String) => " = " + s
                case None => return None
            }
        }
        Some(s"${dataType.toString} ${v.name}$rightPart")
    }
}

class ConstructorInvokeOperation(typeToConstruct: ConstructedType, parameters: List[Expression] = Nil)
    extends FunctionInvokeOperation(typeToConstruct.getConstructor(), parameters)

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

case class ReturnOperation(expression: Expression) extends Operation {
    override def execute(): Option[String] = {
        val result = expression match {
            case v: Variable => Some(v.toString)
            case op: Operation => op.execute()
        }

        result match {
            case Some(res: String) => Some(s"return $res;")
            case _ => None
        }
    }
}


