package com.liberty.model

import com.liberty.patterns
import com.liberty.traits._
import com.liberty.types.{ConstructedType, DataType, primitives}

import scala.collection.SortedSet

/**
 * User: Dimitr
 * Date: 15.09.13
 * Time: 10:55
 */
// TODO: Add support of function and field changing and removing
// TODO: Add constructor support
// TODO: Add name validation
case class JavaClass(var name: String = "", jPackage: JavaPackage = new NoPackage)
  extends DataType(name) with Annotatable with Importable with Cloneable with Generalizable with ConstructedType {
  def this(jPackage: JavaPackage) = this("", jPackage)

  this.javaPackage = jPackage

  override def clone(): AnyRef = super.clone()

  var extendClass: Option[JavaClass] = None
  var functions: List[JavaFunction] = Nil
  var fields: List[JavaField] = Nil
  var implementList: List[JavaInterface] = Nil
  var blocks: List[StaticBlock] = Nil

  override def getTypeName = name

  def addField(field: JavaField) {
    fields = fields ::: List(field)
  }

  def addBlock(block: StaticBlock) {
    blocks = blocks ::: List(block)
  }

  def addFunction(function: JavaFunction) {
    functions = functions ::: List(function)
  }

  def addImplements(interface: JavaInterface) {
    implementList = implementList ::: List(interface)
  }

  def addExtend(clazz: JavaClass) {
    extendClass = Some(clazz)
  }

  def removeImplements(interfaceName: JavaInterface): Option[JavaInterface] = {
    val parts = implementList.partition(ji => !ji.name.equals(interfaceName.name))
    implementList = parts._1
    parts._2 match {
      case Nil => None
      case x :: xs => Some(x)
    }
  }

  def removeExtend(): Option[JavaClass] = {
    val extend = extendClass
    extendClass = None
    extend
  }

  private def getAllImports: String = {
    var set: Set[JavaPackage] = getPackages
    fields.foreach(f => f.getPackages.foreach(p => set += p))
    functions.foreach(f => set = set ++ f.getPackages)
    blocks.foreach(b => set = set ++ b.getPackages)
    annotations.map(ann => ann.javaPackage).filter(p => !p.isInstanceOf[NoPackage]).foreach(p => set += p)
    val imports = set.filter(p => !p.isEmpty).filterNot(_.packagePath == javaPackage.packagePath).map(jp => jp.getImport)
    (SortedSet[String]() ++ imports).mkString("\n")
  }

  private def getPackages: Set[JavaPackage] = {
    var set: Set[JavaPackage] = Set()
    implementList.foreach(i => set += i.javaPackage)
    extendClass.map(c => set = set ++ c.getImportablePackages)
    set
  }

  /**
   * Returns packages for import to outer classes
   */
  def getImportablePackages: Set[JavaPackage] = {
    var set = Set(javaPackage)
    generics.foreach(set += _.javaPackage)
    set
  }

  private def getInheritanceString: String = {
    val extend = extendClass match {
      case None => ""
      case Some(clazz: JavaClass) => " extends " + clazz.name + clazz.getGenericString
    }

    val impl = implementList match {
      case Nil => ""
      case x :: xs => " implements " + implementList.map(interface => interface.name + interface.getGenericString)
        .mkString(", ")
    }

    extend + impl
  }


  def functionExist(functionName: String) = {
    functions.contains(JavaFunction(functionName))
  }

  override def toString: String = {
    val fieldsString = fields.map(_.toClassPart()) match {
      case Nil => ""
      case list: List[String] => list.mkString(";\n\t") + ";"
    }
    val staticBlocks = blocks.map(_.toClassPart()) match {
      case Nil => ""
      case list: List[String] => list.mkString("\n\t")
    }

    val functionsString = functions.map(_.toClassPart()) match {
      case Nil => ""
      case list: List[String] => list.mkString("\n\n")
    }

    patterns.JavaClassPattern(getPackageString, getAllImports, annotationToString(inline = false), name,
      getGenericString, getInheritanceString, fieldsString, staticBlocks, functionsString)
  }

  override def getConstructor(): String = name
}


trait ClassPart {
  def toClassPart(shift: String = "\t"): String
}
 // TODO : change  value: String = "" to Option[String]
case class JavaField(name: String, dataType: DataType, var modifier: Modifier = DefaultModifier, var value: String = "",
                     var id: Boolean = false)
  extends ClassPart with Annotatable {

  import com.liberty.common.Implicits._

  def apply(annotation: JavaAnnotation): JavaField = {
    addAnnotation(annotation)
    this
  }

  if (value.isEmpty)
    value = dataType.getDefaultValue
  else if (dataType == primitives.StringType)
    value = value.quotes

  override def toString: String = {
    s"${
      annotationToPostShiftedString("\t")
    }${
      if (modifier.toString.isEmpty) "" else modifier.toString + " "
    }${dataType.toString} $name = $value"
  }

   /**
     * @return Name with this. prefix
     */
  def getInternalName = "this." + name

  override def toClassPart(shift: String = "\t"): String = toString

  def getPackages: List[JavaPackage] = dataType.javaPackage :: annotations.map(ann => ann.javaPackage)
    .filter(p => !p.isInstanceOf[NoPackage])
}

class StaticBlock extends FunctionBody with ClassPart {
  override def toClassPart(shift: String = "\t"): String = toString.split("\n").map(shift + _).mkString("\n")

  override def toString: String = {
    s"static {\n\t${
      super.toString
    }\n}"
  }
}

object StaticBlock {
  def apply = new StaticBlock()
}