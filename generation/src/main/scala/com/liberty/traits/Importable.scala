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


case class JavaPackage(packagePath: String, packageClass: String) {
  def isEmpty = packagePath.isEmpty || packageClass.isEmpty

  def getImport: String = {
    s"import $packagePath.$packageClass;"
  }

  /**
   * Uses for getting package path. Replace . from package into /
   */
  def getPackagePath = packagePath.replace(".", "/")

  override def equals(obj: Any): Boolean = {
    if (!obj.isInstanceOf[JavaPackage])
      return false
    val pack: JavaPackage = obj.asInstanceOf[JavaPackage]
    this.packagePath.equals(pack.packagePath) && this.packageClass.equals(pack.packageClass)
  }
}

class LocationPackage(packagePath: String) extends JavaPackage(packagePath, "") {
  def nested(nestedPackage: String) = new LocationPackage(packagePath + "." + nestedPackage)

  def nested(nestedPackage: String, className: String) = new JavaPackage(packagePath + "." + nestedPackage, className)
}

object LocationPackage {
  def apply(packagePath: String) = new LocationPackage(packagePath)
}

class NoPackage() extends JavaPackage("", "")

class LocationNoPackage() extends LocationPackage("")
