package com.liberty.entities

import com.liberty.patterns

/**
 * User: Dimitr
 * Date: 14.09.13
 * Time: 10:24
 */
class JavaInterface {
    var name = ""
    var signatures: List[FunctionSignature] = Nil

    def addSignature(signature: FunctionSignature) {
        signatures = signatures ::: List(signature)
    }

    override def toString: String = {
        signatures match {
            case Nil => patterns.JavaMarkerInterfacePattern(name)
            case _ => patterns.JavaInterfacePattern(name,
                signatures.map(s => patterns.JavaFunctionInterfacePattern(s.toString)).mkString("\n\t"))
        }
    }
}

