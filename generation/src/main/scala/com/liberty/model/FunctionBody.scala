package com.liberty.model

import com.liberty.operations._
import com.liberty.traits.JavaPackage

/**
 * User: Dimitr
 * Date: 05.09.13
 * Time: 11:45
 */
class FunctionBody {
  private var operations: List[Operation] = Nil

  override def toString: String = operations match {
    case Nil => "// Empty body"
    case x :: xs => operationsToString()
  }

  private def operationsToString(): String = operations.map(s => if (!s.toString.endsWith("}")) s + ";" else s).mkString("\n\t")

  def addOperation(operation: Operation) {
    operations = operations ::: operation :: Nil
  }

  // TODO: check if the function returns result and if result has appropriate type
  def isFunctionValid: Boolean = true

  /**
   * Get packages
   * Search only class creation operation
   * @return
   */
  def getPackages: Set[JavaPackage] = {
    var set: Set[JavaPackage] = Set()
    operations.foreach {
      case o: CreationOperation => set += o.dataType.javaPackage
      case o: ChainedOperations => getPackages(o, Set()).foreach(set += _)
      case TryOperation(c: CatchOperation) => set += c.e.javaPackage; c.ops.foreach(o => set = set ++ getPackages(o, Set()))
      case _ =>
    }
    set
  }

  private def getPackages(op: Operation, set: Set[JavaPackage]): Set[JavaPackage] = {
    op match {
      case o: CreationOperation => set + o.dataType.javaPackage
      case o: ChainedOperations => set ++ (for (o <- o.operations) yield getPackages(o, set)).flatten
      case _ => set
    }
  }
}
