package com.liberty.model

import com.liberty.builders.{ClassBuilder, FunctionBuilder}
import com.liberty.operations.{CatchOperation, Operation, TryOperation}
import com.liberty.traits.{JavaPackage, NoPackage}

/**
 * Created by Dmytro_Kovalskyi on 19.06.2014.
 */
class JavaException(name: String, jPackage: JavaPackage = new NoPackage) extends JavaClass(name, jPackage) {}

object StandardExceptions {

  object Exception extends JavaException("Exception", JavaPackage("java.lang", "Exception"))

  object UnknownHostException extends JavaException("UnknownHostException", JavaPackage("java.net", "UnknownHostException"))

}

object JavaException {
  def apply(name: String, jPackage: JavaPackage = new NoPackage) = new JavaException(name, jPackage)
}

trait TryResult

case class Throwable(e: JavaException) extends TryResult

case class Catchable(e: JavaException) extends TryResult {

}

class Tryable(builder: FunctionBuilder, withTry: List[Operation]) {
  def throwWrapped(wrapper: JavaException, base: JavaException = StandardExceptions.Exception) {
    builder.addOperation(TryOperation(CatchOperation(base, withTry, ThrowWrapped(wrapper))))
    builder.addThrow(wrapper)
  }

  /**
   * Realize operation in catch block
   */
  def catchOperation(operations: List[Operation], base: JavaException = StandardExceptions.Exception) {
    builder.addOperation(TryOperation(CatchOperation(base, withTry, CatchOperations(operations))))
  }
}

/**
 * Can be used only with ClassBuilder
 * Returns operations wrapped into TryOperation
 */
class ClassTryable(builder: ClassBuilder, withTry: List[Operation]) {
  def printError(forCatch: JavaException = StandardExceptions.Exception) {
    val op = TryOperation(CatchOperation(forCatch, withTry, PrintError))
    builder addOperation op
  }
}

sealed trait CatchResult

case object Ignore extends CatchResult

case object PrintError extends CatchResult

case class CatchOperations(operations: List[Operation]) extends CatchResult

case object Throws extends CatchResult

case class ThrowWrapped(wrapper: JavaException) extends CatchResult
