package com.liberty.builders

import com.liberty.model.{JavaClass, TemplateClass}

/**
 * Created by Dmytro_Kovalskyi on 10.09.2014.
 */
class TemplateClassBuilder(javaClass: TemplateClass = new TemplateClass) extends ClassBuilder(javaClass) {
  def addExtendTemplate(extend: String) = {
    javaClass.addExtendTemplate(extend)
  }

  def addImplementTemplate(impl: String) = {
    javaClass.addImplementTemplate(impl)
  }

  override def getJavaClass: JavaClass = javaClass

  def getTemplateJavaClass: TemplateClass = javaClass
}