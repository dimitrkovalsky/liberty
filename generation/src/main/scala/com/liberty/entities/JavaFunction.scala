package com.liberty.entities

import com.liberty.types.DataType
import com.liberty.{patterns, types}

/**
 * User: Dimitr
 * Date: 01.09.13
 * Time: 12:42
 */
// TODO: add throws support
class JavaFunction {
    var name = ""
    var output: DataType = new types.Undefined
    var input: List[FunctionParameter] = Nil
    var body: FunctionBody = new FunctionBody()

    def addParameter(in: FunctionParameter) {
        input = input ::: List(in)
    }

    def changeParameter(newParameter: FunctionParameter, index: Int): Either[Error, Boolean] = input.lift(index) match {
        case Some(FunctionParameter(_, _)) => input = input.updated(index, newParameter)
            Right(true)
        case _ => Left(new Error("Incorrect index"))
    }

    def getParameter(index: Int): Option[FunctionParameter] = input.lift(index)

    override def toString: String = {
        patterns.JavaFunctionPattern(output, name, input.mkString(", "), body.toString)
    }

    private implicit def dataTypeToString(dataType: DataType): String = dataType.toString

}

case class FunctionParameter(var paramName: String = "", var paramType: DataType) {
    override def toString: String = paramType.toString + " " + paramName
}
