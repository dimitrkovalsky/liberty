package com.liberty

import com.liberty.traits.JavaPackage

/**
 * User: Dimitr
 * Date: 05.09.13
 * Time: 11:52
 */
// TODO : add formatter support
package object patterns {
    def JavaFunctionPattern(signature: String, body: String): String = {
        s"$signature{\n\t$body\n}"
    }

    def JavaFunctionSignaturePattern(modifier: String, output: String, functionName: String, parameters: String, functionThrows: List[String]): String = {
        val thr = functionThrows match {
            case Nil => ""
            case x :: xs => " throws " + functionThrows.mkString(", ") + " "
        }
        val mod = if (modifier.isEmpty) modifier else modifier + " "
        s"$mod$output $functionName($parameters)$thr"
    }

    def JavaFunctionInterfacePattern(signature: String): String = { signature.trim + ";" }

    def JavaInterfacePattern(jPackage: String, name: String, signatures: String): String = {
        s"${if (jPackage.isEmpty) "" else jPackage + "\n\n"}interface $name {\n\t$signatures\n}"
    }

    def JavaMarkerInterfacePattern(jPackage: String, name: String): String = {

        s"${if (jPackage.isEmpty) "" else jPackage + "\n\n"}interface $name {}"
    }

    def JavaClassPattern(annotations: String, name: String, fields: String, functions: String): String = {
        if (fields.isEmpty && functions.isEmpty)
            return s"class $name {}"
        if (fields.isEmpty)
            return s"class $name {\n$functions\n}"
        if (functions.isEmpty)
            return s"class $name {\n\t$fields\n}"

        s"${annotations}class $name {\n\t$fields\n\n$functions\n}"
    }
}
