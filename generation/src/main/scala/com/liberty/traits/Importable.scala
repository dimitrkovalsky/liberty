package com.liberty.traits

/**
 * User: dkovalskyi
 * Date: 10.10.13
 * Time: 18:00
 */
/**
 * Uses for import support in code generation
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

/**
 * Represents Java package for imports
 * @param packagePath path for package something like : javax.persistence
 * @param packageClass class name : Entity
 */
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

object JavaPackage {
  def parse(importString: String): JavaPackage = {
    val lastIndex = importString.lastIndexOf(".")
    val dataType = importString.substring(lastIndex + 1)
    val path = importString.substring(0, lastIndex)
    JavaPackage(path, dataType)
  }
}

class AsteriskPackage(packagePath: String) extends JavaPackage(packagePath, "*")

/**
 * Represent base package for another packages
 * The mail target of this class is to simplify code
 * @param packagePath path for package something like : javax.persistence
 */
class LocationPackage(packagePath: String) extends JavaPackage(packagePath, "") {
  /**
   * Creates new nested LocationPackage instance
   * @param nestedPackage nested path for package
   * @return  LocationPackage instance
   */
  def nested(nestedPackage: String) = {
    if (nestedPackage.isEmpty) this
    else new LocationPackage(packagePath + "." + nestedPackage)
  }

  /**
   * Creates package for appropriate class
   * @param nestedPackage nested path for class
   * @param className  name of target class
   * @return JavaPackage instance
   */
  def nested(nestedPackage: String, className: String) = new JavaPackage(packagePath + "." + nestedPackage, className)

  /**
   * Creates package for appropriate class
   * @param className name of target class
   * @return JavaPackage instance
   */
  def nestedClass(className: String) = new JavaPackage(packagePath, className)
}

object LocationPackage {
  def apply(packagePath: String) = new LocationPackage(packagePath)
}

class NoPackage() extends JavaPackage("", "")

class LocationNoPackage() extends LocationPackage("")
