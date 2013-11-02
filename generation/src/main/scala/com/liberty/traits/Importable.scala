package com.liberty.traits

/**
 * User: dkovalskyi
 * Date: 10.10.13
 * Time: 18:00
 */
trait Importable {
    var javaPackage: JavaPackage = new NoPackage()

    def getImports: String = javaPackage match {
        case p: NoPackage => ""
        case _ => javaPackage.toString
    }

    def getPackageString = javaPackage match {
        case p: NoPackage => ""
        case _ => s"package ${javaPackage.packagePath};"
    }


}


case class JavaPackage(packagePath: String, packageClass: String = "") {
    def getImport: String = {
        s"import $packagePath.$packageClass;"
    }

    override def equals(obj: Any): Boolean = {
        if (!obj.isInstanceOf[JavaPackage])
            return false
        val pack: JavaPackage = obj.asInstanceOf[JavaPackage]
        this.packagePath.equals(pack.packagePath) && this.packageClass.equals(pack.packageClass)
    }
}

class NoPackage() extends JavaPackage("", "") {}
