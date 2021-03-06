package com.liberty.operations

import com.liberty.common.Implicits._
import com.liberty.model._
import com.liberty.traits.Importable
import com.liberty.types.ObjectType

/**
 * User: dkovalskyi
 * Date: 06.09.13
 * Time: 16:51
 */
trait Expression

/**
 * Class that represents all types of opearation
 */
trait Operation extends Expression with Importable {
  /**
   * Executes operation
   * @return string representation of operation invoke
   */
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

  def apply(obj: ObjectType) = new Variable(obj.getTypeName.firstToLowerCase)
}

case class Value(value: String) extends Expression {
  override def toString: String = value

}

class StringValue(value: String) extends Expression {
  def asValue = new Value(value)

  override def toString: String = "\"" + value + "\""
}

case class NoneExpression() extends Expression

case class UnaryOperation(operator: String, arg: Expression) extends Expression

case class BinaryOp(operator: String, left: Expression, right: Expression) extends Expression


case class TryOperation(c: CatchOperation) extends Operation {
  override def execute(): Option[String] = {
    c.execute().flatMap(r => Some("try {" + "\n\t\t" + r))
  }
}

case class CatchOperation(e: JavaException, ops: List[Operation], result: CatchResult) extends Operation {
  override def execute(): Option[String] = {
    val body = result match {
      case Throws => ""
      case ThrowWrapped(ex) => s"throw new ${ex.name}(e);"
      case PrintError => s"System.err.println(e.getMessage());"
      case CatchOperations(operations: List[Operation]) => operations.mkString(";\n\t\t") + ";"
      case _ => ""
    }
    Some(s"${ops.flatMap(o => o.execute()).map(_ + ";").mkString("\n\t\t")}\n\t} catch(${e.name} e){\n\t\t$body\n\t}")
  }
}