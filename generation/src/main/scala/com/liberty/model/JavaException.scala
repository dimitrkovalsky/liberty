package com.liberty.model

import com.liberty.builders.FunctionBuilder
import com.liberty.operations.{CatchOperation, Operation, TryOperation}
import com.liberty.traits.{JavaPackage, NoPackage}

/**
 * Created by Dmytro_Kovalskyi on 19.06.2014.
 */
class JavaException(name: String, jPackage: JavaPackage = new NoPackage) extends JavaClass(name, jPackage) {}

object StandardExceptions {

  object Exception extends JavaException("Exception", JavaPackage("java.lang", "Exception"))

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
}

sealed trait CatchResult

case object Ignore extends CatchResult

case object Throws extends CatchResult

case class ThrowWrapped(wrapper: JavaException) extends CatchResult
