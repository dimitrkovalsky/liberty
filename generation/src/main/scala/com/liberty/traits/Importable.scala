package com.liberty.traits

/**
 * User: dkovalskyi
 * Date: 10.10.13
 * Time: 18:00
 */
// TODO : Add import support
trait Importable {
    var javaPackage: JavaPackage = new NoPackage()

    def getImports: String = javaPackage match {
        case p:NoPackage => ""
        case _ => javaPackage.toString
    }

    def getPackageString =   javaPackage match {
        case p:NoPackage => ""
        case _ => s"package ${javaPackage.name};"
    }
}


case class JavaPackage(name: String) {
    override def toString: String = {
        s"import $name;"
    }
}

class NoPackage() extends JavaPackage("") {
}
