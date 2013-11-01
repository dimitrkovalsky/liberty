package com.liberty.entities

import com.liberty.operations.Operation
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

    // TODO: do not add ';' if in operation loop or if-else expression
    private def operationsToString(): String = operations.mkString(";\n\t")

    def addOperation(operation: Operation) {
        operations = operations ::: operation :: Nil
    }

    // TODO: check if the function returns result and if result has appropriate type
    def isFunctionValid: Boolean = true

    // TODO: realize body getPackages
    def getPackages:Set[JavaPackage] = Set()
}
