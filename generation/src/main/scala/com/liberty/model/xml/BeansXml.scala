package com.liberty.model.xml

/**
 * Created by Dmytro_Kovalskyi on 26.09.2014.
 */
class BeansXml {
  def createXml() = {
   val xml = <beans xmlns="http://java.sun.com/xml/ns/javaee"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/beans_1_0.xsd">
      </beans>
    xml
  }
}
