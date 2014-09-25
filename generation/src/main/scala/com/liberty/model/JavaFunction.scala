package com.liberty.model

import com.liberty.operations.Variable
import com.liberty.traits.{Annotatable, JavaPackage, NoPackage}
import com.liberty.types.{DataType, UndefinedType}
import com.liberty.{patterns, types}

import scala.Predef._

/**
 * User: Dimitr
 * Date: 01.09.13
 * Time: 12:42
 */
class JavaFunction extends ClassPart with Annotatable {

  var signature: FunctionSignature = new FunctionSignature

  var body: FunctionBody = new FunctionBody()


  def this(functionName: String) = {
    this()
    signature.name = functionName
  }


  override def toString: String = {
    patterns.JavaFunctionPattern(signature.toString, body.toString)
  }

  override def toClassPart(shift: String = "\t"): String = {
    annotationToShiftedString(shift) + toString.split("\n").map(shift + _).mkString("\n")
  }

  def getPackages: Set[JavaPackage] = {
    var set: Set[JavaPackage] = signature.getPackages
    set = set ++ body.getPackages
    set.filter(p => !p.isInstanceOf[NoPackage])
  }

  override def equals(obj: Any): Boolean = {
    if (!obj.isInstanceOf[JavaFunction])
      return false
    this.signature.equals(obj.asInstanceOf[JavaFunction].signature)
  }
}

case class FunctionParameter(paramName: Variable, var paramType: DataType) extends Annotatable {
  def this(paramName: String, paramType: DataType) = this(Variable(paramName), paramType)

  override def toString: String = {
    val annotations = annotationsToString(inline = true)
    val annString = if (!annotations.isEmpty) annotations + " " else ""
    annString + paramType.toString + " " + paramName.toString
  }
}

object FunctionParameter {
  def apply(field: JavaField) = new FunctionParameter(Variable(field), field.dataType)

  def apply(paramName: String, paramType: DataType) = new FunctionParameter(Variable(paramName), paramType)
}

case class FunctionSignature(var name: String, var output: DataType, var modifier: Modifier = DefaultModifier,
                             var input: List[FunctionParameter] = Nil, var functionThrows: List[JavaException] = Nil) {
  def this(name: String, output: DataType, input: List[FunctionParameter], functionThrows: List[JavaException]) = this(name,
    output, DefaultModifier, input, functionThrows)

  def this(name: String, output: DataType, input: List[FunctionParameter]) = this(name, output, DefaultModifier,
    input)

  def this(name: String, output: DataType) = this(name, output, Nil, Nil)

  def this(name: String) = this(name, new types.UndefinedType)

  def this() = this("")

  override def equals(obj: Any): Boolean = {
    if (!obj.isInstanceOf[FunctionSignature])
      return false
    val sign: FunctionSignature = obj.asInstanceOf[FunctionSignature]
    this.name.equals(sign.name)
  }

  def isModifierPresent(modifier: Modifier) = this.modifier == modifier

  def addParameter(in: FunctionParameter) {
    input = input ::: List(in)
  }

  def changeParameter(newParameter: FunctionParameter, index: Int): Either[Error, Boolean] = input.lift(index) match {
    case Some(FunctionParameter(_, _)) => input = input.updated(index, newParameter)
      Right(true)
    case _ => Left(new Error("Incorrect index"))
  }

  def getParameter(index: Int): Option[FunctionParameter] = input.lift(index)

  def addThrow(thr: JavaException) {
    functionThrows = functionThrows ::: List(thr)
  }

  private implicit def dataTypeToString(dataType: DataType): String = {
    dataType match {
      case t: UndefinedType => "void"
      case _ => dataType.toString
    }
  }

  def getPackages: Set[JavaPackage] = {
    var set: Set[JavaPackage] = Set()
    set += output.javaPackage
    input.foreach(param => set += param.paramType.javaPackage)
    functionThrows.foreach(set += _.javaPackage)
    set
  }

  def toConstructorString: String = {
    patterns.JavaConstructorSignaturePattern(modifier.toString, name, input.mkString(", "), functionThrows.map(_.name))
  }

  override def toString: String = {

    patterns.JavaFunctionSignaturePattern(modifier.toString, output, name, input.mkString(", "), functionThrows.map(_.name))
  }
}


class ConstructorFunction extends JavaFunction {
  override def toString: String = {
    patterns.JavaFunctionPattern(signature.toConstructorString, body.toString)
  }
}

object ConstructorFunction {
  def apply(f: JavaFunction): ConstructorFunction = {
    val c = new ConstructorFunction
    c.body = f.body
    c.signature = f.signature
    c.annotations = f.annotations
    c
  }
}

object JavaFunction {
  def apply(functionName: String) = new JavaFunction(functionName)

  def apply(signature: FunctionSignature, body: FunctionBody) = {
    val f = new JavaFunction
    f.signature = signature
    f.body = body
    f
  }
}

