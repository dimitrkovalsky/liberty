package com.liberty.model

import com.liberty.patterns
import com.liberty.traits.{Generalizable, Importable, JavaPackage, NoPackage}

/**
 * User: Dimitr
 * Date: 14.09.13
 * Time: 10:24
 */
// TODO: Prohibit the creation of two identical interfaces
// TODO: Add possibility constand addition
class JavaInterface(jPackage: JavaPackage = new NoPackage, modifier: Modifier = PublicModifier) extends Importable with Generalizable {
  javaPackage = javaPackage
  var name = ""
  var signatures: List[FunctionSignature] = Nil

  def addSignature(signature: FunctionSignature) {
    signatures = signatures ::: List(signature)
  }

  private def getAllImports: String = {
    var set: Set[JavaPackage] = Set()
    signatures.foreach(f => set = set ++ f.getPackages)
    set.filter(p => !p.isEmpty).filterNot(_.packagePath == javaPackage.packagePath).map(jp => jp.getImport).mkString("\n")
  }

  override def toString: String = {
    val mod = modifier.toString
    signatures match {
      case Nil => patterns.JavaMarkerInterfacePattern(getPackageString, mod, name, getGenericString)
      case _ => val signs = signatures.map(s => patterns.JavaFunctionInterfacePattern(s.toString)).mkString("\n\n\t")
        patterns.JavaInterfacePattern(getPackageString, getAllImports, mod, name, getGenericString, signs)
    }
  }

  override def equals(obj: scala.Any): Boolean = {
    if (!obj.isInstanceOf[JavaInterface])
      return false
    val interface: JavaInterface = obj.asInstanceOf[JavaInterface]
    this.name.equals(interface.name)
  }
}

