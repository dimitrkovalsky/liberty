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

    def execute(operation: Operation): Option[String]

    protected[operations] def returnResult(): Option[String]

    override def toString: String = execute() match {
        case Some(res: String) => res
        case _ => "// Empty operation"
    }
}

case class Variable(name: String) extends Expression

case class Number(num: Double) extends Expression

case class UnaryOperation(operator: String, arg: Expression) extends Expression

case class BinaryOp(operator: String, left: Expression, right: Expression) extends Expression

case class FunctionInvokeOperation(functionName: String, var params: List[String] = Nil) extends Operation {
    def execute(): Option[String] = {
        val invokeParams = params match {
            case x :: xs => params.mkString(", ")
            case _ => ""
        }
        Some(s"$functionName($invokeParams)")
    }

    protected[operations] override def returnResult(): Option[String] = execute()

    def execute(operation: Operation): Option[String] = {
        operation.execute() match {
            case Some(res: String) => this.params = List(res); execute()
            case _ => None
        }
    }
}

class ConstructorInvokeOperation(typeToConstruct: ConstructedType, parameters: List[String] = Nil)
    extends FunctionInvokeOperation(typeToConstruct.getConstructor(), parameters)

// TODO : Validate variable name
case class CreationOperation(dataType: DataType, variableName: String = "", var params: List[String] = Nil)
    extends Operation {
    // TODO: realize creation of primitives with params
    def createFromDataType(dataType: DataType): String = dataType match {
        case t: ConstructedType => s"new ${new ConstructorInvokeOperation(t, params).execute().get}"
        case prim: primitives.PrimitiveType => prim.getDefaultValue
    }

    def execute(): Option[String] = {
        val construct = createFromDataType(dataType)
        variableName match {
            case "" => Some(construct)
            case _ => Some(s"${dataType.toString} $variableName = $construct;")
        }

    }

    protected[operations] override def returnResult(): Option[String] = execute()

    def execute(operation: Operation): Option[String] = {
        operation.execute() match {
            case Some(res: String) => this.params = List(res); execute()
            case _ => None
        }
    }
}

case class ReturnOperation(operation: Operation) extends Operation {
    def execute(): Option[String] = operation.returnResult() match {
        case Some(invokeResult: String) => Some(s"return $invokeResult;")
        case _ => None
    }

    protected[operations] override def returnResult(): Option[String] = execute()

    def execute(operation: Operation): Option[String] = ???
}


