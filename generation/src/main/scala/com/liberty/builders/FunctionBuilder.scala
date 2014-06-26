package com.liberty.builders

import com.liberty.model._
import com.liberty.operations._
import com.liberty.types

/**
 * User: Dimitr
 * Date: 05.09.13
 * Time: 10:52
 */
// TODO: In future use method getInstance for retrieving appropriate builder for some language
class FunctionBuilder {
  private val function = new JavaFunction
  private var inTrySection = false
  private var withTry: List[Operation] = Nil
  private var lastTry: Option[TryOperation] = None

  def setName(name: String) = function.signature.name = name

  def setOutputType(dataType: types.DataType) = function.signature.output = dataType

  def addParam(param: FunctionParameter) = function.signature.addParameter(param)

  def addParams(params: FunctionParameter*) = {
    for (param <- params)
      function.signature.addParameter(param)
  }

  def addThrow(thr: JavaException) {
    function.signature.addThrow(thr)
  }

  def addAnnotation(annotation: JavaAnnotation) {
    function.addAnnotation(annotation)
  }

  def addModifier(modifier: Modifier) {
    function.signature.modifier = modifier
  }

  def getFunction = function

  def addOperation(operation: Operation) {
    if (inTrySection) {
      withTry = withTry ::: operation :: Nil
    }
    else
      function.body.addOperation(operation)
  }

  def addSuperMethodInvoke(name: String, params: List[Expression], result: Option[Variable]) {
    val function = new SelfFunctionInvokeOperation(FunctionType.SUPER_FUNCTION, name, params, result)
    addOperation(function)
  }

  def addSuperMethodInvoke(name: String, result: Option[Variable], params: Expression*) {
    val function = new SelfFunctionInvokeOperation(FunctionType.SUPER_FUNCTION, name, params.toList, result)
    addOperation(function)
  }

  def tryable(f: => Unit): Tryable = {
    inTrySection = true
    f
    inTrySection = false
    new Tryable(this, withTry)
  }

  def wrapable(wrapper: JavaException)(f: => Unit) = {
    tryable(f).throwWrapped(wrapper)
  }
}

object FunctionBuilder {
  def apply = new FunctionBuilder()

  def apply(modifier: Modifier, functionName: String, params: FunctionParameter*) = {
    val builder = new FunctionBuilder()
    builder.setName(functionName)
    builder.addParams(params: _*)
    builder.addModifier(modifier)
    builder
  }

  def apply(functionName: String, params: FunctionParameter*) = {
    val builder = new FunctionBuilder()
    builder.setName(functionName)
    builder.addParams(params: _*)
    builder
  }
}
