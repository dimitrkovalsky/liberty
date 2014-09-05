package com.liberty.model

import com.liberty.common.Implicits._
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
case class JavaClass(var name: String = "", jPackage: JavaPackage = new NoPackage, modifier: Modifier = PublicModifier)
  extends DataType(name) with Annotatable with Importable with Cloneable with Generalizable with ConstructedType with Serializable {
  def this(jPackage: JavaPackage) = this("", jPackage)

  this.javaPackage = jPackage

  override def clone(): AnyRef = super.clone()

  def deepCopy: JavaClass = {
    val result = new JavaClass(name, jPackage)
    result.javaPackage = this.javaPackage
    result.blocks = this.blocks
    result.extendClass = this.extendClass
    result.fields = this.fields.map(f => f.deepCopy())
    result.functions = this.functions
    result.implementList = this.implementList
    result
  }

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
    val mod = modifier.toString
    patterns.JavaClassPattern(getPackageString, getAllImports, annotationsToString(inline = false), mod, name,
      getGenericString, getInheritanceString, fieldsString, staticBlocks, functionsString)
  }

  override def getConstructor(): String = name
}


trait ClassPart {
  def toClassPart(shift: String = "\t"): String
}

// TODO : change  value: String = "" to Option[String]
case class JavaField(name: String, dataType: DataType, modifier: Modifier = DefaultModifier, value: Option[String] = None)
  extends ClassPart with Annotatable {
  def deepCopy(): JavaField = {
    val field = new JavaField(name, dataType, modifier, value)
    field.annotations = this.annotations.map(a => a)
    field
  }

  def apply(annotation: JavaAnnotation): JavaField = {
    addAnnotation(annotation)
    this
  }


  override def toString: String = {
    val defVal =
      if (value.isDefined && dataType == primitives.StringType)
        Some(value.get.quotes)
      else if (value.isDefined)
        value
//      else if (!value.isDefined)
//        Some(dataType.getDefaultValue)
      else None

    s"${
      annotationToPostShiftedString("\t")
    }${
      if (modifier.toString.isEmpty) "" else modifier.toString + " "
    }${dataType.toString} $name${
      if (defVal.isDefined) {
        " = " + defVal.get
      } else ""
    }"
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