package com.liberty.entities

import com.liberty.types.{UndefinedType, DataType}
import com.liberty.{patterns, types}
import com.liberty.operations.Variable
import com.liberty.traits.{JavaPackage, Annotatable}

/**
 * User: Dimitr
 * Date: 01.09.13
 * Time: 12:42
 */
// TODO: add access modifiers support
class JavaFunction extends ClassPart with Annotatable {
    val signature: FunctionSignature = new FunctionSignature

    var body: FunctionBody = new FunctionBody()


    override def toString: String = {
        patterns.JavaFunctionPattern(signature.toString, body.toString)
    }

    override def toClassPart(shift: String = "\t"): String = {
        annotationToShiftedString(shift) + toString.split("\n").map(shift + _).mkString("\n")
    }

    def getPackages: Set[JavaPackage] = {
        val set: Set[JavaPackage] = signature.getPackages
        set ++ body.getPackages
    }
}

case class FunctionParameter(paramName: Variable, var paramType: DataType) {
    override def toString: String = paramType.toString + " " + paramName.toString
}

// TODO: Add normal throws support add Java exception type
// TODO: Add exception import  support
case class FunctionSignature(var name: String, var output: DataType, var modifier: Modifier = DefaultModifier,
                             var input: List[FunctionParameter] = Nil, var functionThrows: List[String] = Nil) {
    def this(name: String, output: DataType, input: List[FunctionParameter], functionThrows: List[String]) = this(name,
        output, DefaultModifier, input, functionThrows)

    def this(name: String, output: DataType, input: List[FunctionParameter]) = this(name, output, DefaultModifier,
        input)

    def this(name: String, output: DataType) = this(name, output, Nil, Nil)

    def this(name: String) = this(name, new types.UndefinedType)

    def this() = this("")


    def addParameter(in: FunctionParameter) {
        input = input ::: List(in)
    }

    def changeParameter(newParameter: FunctionParameter, index: Int): Either[Error, Boolean] = input.lift(index) match {
        case Some(FunctionParameter(_, _)) => input = input.updated(index, newParameter)
            Right(true)
        case _ => Left(new Error("Incorrect index"))
    }

    def getParameter(index: Int): Option[FunctionParameter] = input.lift(index)

    def addThrow(thr: String) {
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
        set
    }

    override def toString: String = {
        patterns.JavaFunctionSignaturePattern(modifier.toString, output, name, input.mkString(", "), functionThrows)
    }
}

