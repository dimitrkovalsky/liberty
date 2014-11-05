package com.liberty.model

/**
 * Created by Dmytro_Kovalskyi on 10.09.2014.
 */
class TemplateClass extends JavaClass {
  var extendTemplateList: Option[String] = None
  var implementTemplateList: List[String] = Nil
  var baseTemplatePackage: String = _

  def addExtendTemplate(ext: String): Unit = {
    extendTemplateList = Some(ext)
  }

  def addImplementTemplate(impl: String): Unit = {
    implementTemplateList = implementTemplateList ::: List(impl)
  }

  protected override def getInheritanceString: String = {
    val extend = extendTemplateList match {
      case None => ""
      case Some(clazz) => " extends " + clazz
    }

    val impl = implementTemplateList match {
      case Nil => ""
      case x :: xs => " implements " + implementTemplateList.mkString(", ")
    }

    extend + impl
  }
}
