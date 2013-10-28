package com.liberty.entities

import com.liberty.patterns
import com.liberty.traits.{NoPackage, JavaPackage, Importable}

/**
 * User: Dimitr
 * Date: 14.09.13
 * Time: 10:24
 */
// TODO:Prohibit the creation of two identical interfaces
class JavaInterface(jPackage: JavaPackage = new NoPackage) extends Importable {
    javaPackage = javaPackage
    var name = ""
    var signatures: List[FunctionSignature] = Nil

    def addSignature(signature: FunctionSignature) {
        signatures = signatures ::: List(signature)
    }

    override def toString: String = {
        val jPackage = getPackageString
        signatures match {
            case Nil => patterns.JavaMarkerInterfacePattern(jPackage, name)
            case _ => patterns.JavaInterfacePattern(jPackage, name, signatures.map(s => patterns.JavaFunctionInterfacePattern(s.toString)).mkString("\n\t"))
        }
    }

    override def equals(obj: scala.Any): Boolean = {
        if(!obj.isInstanceOf[JavaInterface])
            return false
        val interface:JavaInterface = obj.asInstanceOf[JavaInterface]
        this.name.equals(interface.name)
    }
}
