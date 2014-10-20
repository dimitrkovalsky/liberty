package com.liberty.generators

import com.liberty.common.TestModels
import org.junit.{Assert, Test}

/**
 * Created by Dmytro_Kovalskyi on 25.09.2014.
 */
class BeanGeneratorTest {


  @Test def generateBean() {
    val generator = new BeanGenerator
    val result = generator.createBean(TestModels.getSubject)
    result match {
      case Some(packet) =>
        val actualBean = packet.bean.toString
        val actualInterface = packet.beanInterface.toString
//        println(actualBean)
        val beanExpected = "package standard.beans;\n\nimport java.lang.Integer;\nimport java.util.List;\nimport standard.commons.DaoFactory;\nimport standard.dao.ISubjectDao;\nimport standard.errors.ApplicationException;\nimport standard.models.Subject;\n\npublic class SubjectBean implements ISubjectBean {\n\tpublic Subject getSubject(Integer id) throws ApplicationException {\n\t\treturn DaoFactory.getSubjectDao().findById(id);\n\t}\n\n\tpublic List<Subject> getAllSubjects() throws ApplicationException {\n\t\treturn DaoFactory.getSubjectDao().findAll();\n\t}\n\n\tpublic void addSubject(Subject subject) throws ApplicationException {\n\t\tISubjectDao dao = DaoFactory.getSubjectDao();\n\t\tdao.insert(subject);\n\t}\n\n\tpublic void updateSubject(Subject subject) throws ApplicationException {\n\t\tISubjectDao dao = DaoFactory.getSubjectDao();\n\t\tdao.update(subject);\n\t}\n\n\tpublic void deleteSubject(Integer id) throws ApplicationException {\n\t\tISubjectDao dao = DaoFactory.getSubjectDao();\n\t\tSubject subject = new Subject();\n\t\tsubject.setId(id);\n\t\tdao.delete(subject);\n\t}\n}"
        val interfaceExpected = "package standard.beans;\n\nimport javax.ejb.Local;\nimport standard.models.Subject;\nimport standard.errors.ApplicationException;\nimport java.util.List;\nimport java.lang.Integer;\n\n@Local\npublic interface ISubjectBean {\n\n\tpublic Subject getSubject(Integer id) throws ApplicationException;\n\n\tpublic List<Subject> getAllSubjects() throws ApplicationException;\n\n\tpublic void addSubject(Subject subject) throws ApplicationException;\n\n\tpublic void updateSubject(Subject subject) throws ApplicationException;\n\n\tpublic void deleteSubject(Integer id) throws ApplicationException;\n}"
        Assert.assertEquals(beanExpected, actualBean)
        Assert.assertEquals(interfaceExpected, actualInterface)
      case _ => Assert.fail("Fail in" + result)
    }
  }

  @Test def generateBeansXml() {
    val generator = new BeanGenerator
    val result = generator.createBeansXml
    val expected = "<beans xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/beans_1_0.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://java.sun.com/xml/ns/javaee\">\n      </beans>"
    Assert.assertEquals(expected,result.xml.toString())
  }
}
