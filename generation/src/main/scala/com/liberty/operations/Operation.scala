package com.liberty.operations

import com.liberty.types.{primitives, ConstructedType, DataType}
import com.liberty.entities.JavaField
import com.liberty.traits.Importable

/**
 * User: dkovalskyi
 * Date: 06.09.13
 * Time: 16:51
 */
trait Expression

// TODO : Provide the possibility to get packages from operations
trait Operation extends Expression with Importable{
    def execute(): Option[String]

    override def toString: String = execute() match {
        case Some(res: String) => res
        case _ => "// Empty operation"
    }
}

case class Variable(name: String) extends Expression {
    override def toString: String = name
}

object Variable {
    def apply(field: JavaField) = new Variable(field.name)
}

case class Value(value: String) extends Expression {
    override def toString: String = value
}

case class NoneExpression() extends Expression

case class UnaryOperation(operator: String, arg: Expression) extends Expression

case class BinaryOp(operator: String, left: Expression, right: Expression) extends Expression

