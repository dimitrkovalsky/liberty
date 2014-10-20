package com.liberty.generators

import com.liberty.common.{ClassMapper, ProjectConfig}
import com.liberty.model.xml.{BeansXml, XmlFile}
import com.liberty.model.{JavaClass, JavaInterface}
import com.liberty.traits.LocationPackage

/**
 * Created by Dmytro_Kovalskyi on 25.09.2014.
 */
class BeanGenerator(basePackage: LocationPackage = ProjectConfig.basePackage.nested("beans")) extends HybridGenerator {
  private val template = loadClass("bean.template")
  private val baseInterface = loadInterface("bean.interface")
  private val baseModel = loadModel("bean.model")

  def createBean(model: JavaClass): Option[BeanPacket] = {
    val mapper = ClassMapper(baseModel)
    val bean = mapper.changeModel(template, model, model.name + "Bean", basePackage)

    val interface = mapper.changeModel(baseInterface, model, "I" + model.name + "Bean", basePackage)
    bean.addImplements(interface)

    Some(BeanPacket(bean, interface))
  }

  def createBeansXml: XmlFile = {
    XmlFile("beans.xml", new BeansXml().createXml())
  }
}

// TODO: generate beans.xml
/**
 * Uses for transferring data after dao updates
 */
case class BeanPacket(bean: JavaClass, beanInterface: JavaInterface)
