package com.liberty.builders

import com.liberty.StubType
import com.liberty.common.Implicits._
import com.liberty.model._
import com.liberty.traits.JavaPackage
import com.liberty.types.primitives._
import com.liberty.types.standardTypes.DateType
import org.junit.{Assert, Test}

/**
 * User: Dimitr
 * Date: 15.09.13
 * Time: 12:37
 */
class ClassBuilderTest {
  @Test def emptyClass() {
    val builder = new ClassBuilder
    builder.setName("Empty")
    val clazz = builder.getJavaClass

    val expected = "public class Empty {\n\n}"
    val available = clazz.toString
    //println(available)
    Assert.assertEquals(expected, available)
  }

  @Test def fieldsClass() {
    val builder = new ClassBuilder
    builder.setName("SomeClass")
    builder.addField(JavaField("name", StringType, PrivateModifier))
    builder.addField(JavaField("age", IntegerType, PrivateModifier))
    builder.addField(JavaField("position", StringType, ProtectedModifier))
    val clazz = builder.getJavaClass

    val expected = "import java.lang.Integer;\nimport java.lang.String;\n\npublic class SomeClass {\n\tprivate String name;\n\tprivate Integer age;\n\tprotected String position;\n}"
    val available = clazz.toString
    //println(available)
    Assert.assertEquals(expected, available)
  }

  @Test def genericClass() {
    val builder = new ClassBuilder
    builder.setName("SomeClass")
    builder.addField(JavaField("name", StringType, PrivateModifier))
    builder.addField(JavaField("age", IntegerType, PrivateModifier))
    builder.addField(JavaField("position", StringType, ProtectedModifier))
    builder.addGeneric(StubType("GenericClass"))
    val clazz = builder.getJavaClass

    val expected = "import java.lang.Integer;\nimport java.lang.String;\n\npublic class SomeClass<GenericClass> {\n\tprivate String name;\n\tprivate Integer age;\n\tprotected String position;\n}"
    val available = clazz.toString
    //println(available)
    Assert.assertEquals(expected, available)
  }

  @Test def genericWithInheritanceClass() {
    val clazz = createGenericInheritClass

    val expected = "package my.test.package;\n\nimport java.lang.Integer;\nimport java.lang.String;\nimport java.util.ArrayList;\nimport java.util.List;\nimport javax.persistence.Column;\nimport javax.persistence.Entity;\nimport javax.persistence.Id;\nimport javax.persistence.NamedQuery;\n\n@Entity(table = \"simple_table\")\n@NamedQuery(name = \"findAll\", query = \"SELECT * FROM simple_table\")\npublic class FullClass extends AnotherClass implements GenericInterface<SomeType> {\n\t@Id\n\tprivate String id;\n\t@Column(name = \"fc_name\")\n\tprivate String name;\n\tprivate Integer age;\n\tprotected String position;\n\n\tList<String> filter(List<String> list){\n\t\tArrayList<String> result = new ArrayList();\n\t\tvalidate(result);\n\t\treturn split(list, result);\n\t}\n\n\tString invokeAnotherFunction(List<String> list){\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n\n\tString invoke(List<String> list) throws Exception {\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n\n\t@Override\n\tpublic String toString(){\n\t\treturn \"toString invoked\";\n\t}\n}"
    val available = clazz.toString
    //println(available)
    Assert.assertEquals(expected, available)
  }


  @Test def functionsClass() {
    val clazz = createAnotherClass
    val expected = "import java.lang.String;\nimport java.util.ArrayList;\nimport java.util.List;\n\npublic class AnotherClass {\n\tList<String> filter(List<String> list){\n\t\tArrayList<String> result = new ArrayList();\n\t\tvalidate(result);\n\t\treturn split(list, result);\n\t}\n\n\tString invokeAnotherFunction(List<String> list){\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n\n\tString invoke(List<String> list) throws Exception {\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n}"
    val available = clazz.toString
    //println(available)
    Assert.assertEquals(expected, available)
  }

  @Test def functionWithAnnotationClass() {
    val builder = new ClassBuilder
    builder.setName("AnotherSomeClass")
    val functionTest = new FunctionBuilderTest
    builder.addFunction(functionTest.createToStringFunction)
    val clazz = builder.getJavaClass

    val expected = "import java.lang.String;\n\npublic class AnotherSomeClass {\n\t@Override\n\tpublic String toString(){\n\t\treturn \"toString invoked\";\n\t}\n}"
    val available = clazz.toString
    //println(available)
    Assert.assertEquals(expected, available)
  }

  @Test def fullClass() {
    val clazz = createFullClass

    val expected = "package com.guide.city.model;\n\nimport java.lang.Integer;\nimport java.lang.String;\nimport java.util.ArrayList;\nimport java.util.List;\n\npublic class FullClass {\n\tprivate String name;\n\tprivate Integer age;\n\tprotected String position;\n\n\tList<String> filter(List<String> list){\n\t\tArrayList<String> result = new ArrayList();\n\t\tvalidate(result);\n\t\treturn split(list, result);\n\t}\n\n\tString invokeAnotherFunction(List<String> list){\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n\n\tString invoke(List<String> list) throws Exception {\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n}"
    val available = clazz.toString
    //  println(available)
    Assert.assertEquals(expected, available)
  }

  @Test def withAnnotationClass() {
    val clazz = createWithAnnotationClass

    val expected = "package my.test.package;\n\nimport java.lang.Integer;\nimport java.lang.String;\nimport java.util.ArrayList;\nimport java.util.List;\nimport javax.persistence.Column;\nimport javax.persistence.Entity;\nimport javax.persistence.Id;\nimport javax.persistence.NamedQuery;\n\n@Entity(table = \"simple_table\")\n@NamedQuery(name = \"findAll\", query = \"SELECT * FROM simple_table\")\npublic class FullClass {\n\t@Id\n\tprivate String id;\n\t@Column(name = \"fc_name\")\n\tprivate String name;\n\tprivate Integer age;\n\tprotected String position;\n\n\tList<String> filter(List<String> list){\n\t\tArrayList<String> result = new ArrayList();\n\t\tvalidate(result);\n\t\treturn split(list, result);\n\t}\n\n\tString invokeAnotherFunction(List<String> list){\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n\n\tString invoke(List<String> list) throws Exception {\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n\n\t@Override\n\tpublic String toString(){\n\t\treturn \"toString invoked\";\n\t}\n}"
    val available = clazz.toString
    //println(available)
    Assert.assertEquals(expected, available)
  }

  @Test def withInheritanceClass() {
    val clazz = createInheritClass
    val expected = "package my.test.package;\n\nimport java.lang.Integer;\nimport java.lang.String;\nimport java.util.ArrayList;\nimport java.util.List;\nimport javax.persistence.Column;\nimport javax.persistence.Entity;\nimport javax.persistence.Id;\nimport javax.persistence.NamedQuery;\nimport my.test.program.Marker;\n\n@Entity(table = \"simple_table\")\n@NamedQuery(name = \"findAll\", query = \"SELECT * FROM simple_table\")\npublic class FullClass extends AnotherClass implements Marker, TestInterface {\n\t@Id\n\tprivate String id;\n\t@Column(name = \"fc_name\")\n\tprivate String name;\n\tprivate Integer age;\n\tprotected String position;\n\n\tList<String> filter(List<String> list){\n\t\tArrayList<String> result = new ArrayList();\n\t\tvalidate(result);\n\t\treturn split(list, result);\n\t}\n\n\tString invokeAnotherFunction(List<String> list){\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n\n\tString invoke(List<String> list) throws Exception {\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n\n\t@Override\n\tpublic String toString(){\n\t\treturn \"toString invoked\";\n\t}\n}"
    val available = clazz.toString
    println(available)
    Assert.assertEquals(expected, available)
  }

  def createFullClass: JavaClass = {
    val builder = new ClassBuilder
    builder.setName("FullClass")
    val functionTest = new FunctionBuilderTest
    builder.addPackage(JavaPackage("com.guide.city.model", "BasicDAO"))
    builder.addField(JavaField("name", StringType, PrivateModifier))
    builder.addField(JavaField("age", IntegerType, PrivateModifier))
    builder.addField(JavaField("position", StringType, ProtectedModifier))
    builder.addFunction(functionTest.createFilterFunction)
    builder.addFunction(functionTest.createInvokeAnotherFunction)
    builder.addFunction(functionTest.createInvokeFunction)

    builder.getJavaClass
  }

  def createPojoClass: JavaClass = {
    val builder = new ClassBuilder
    builder.setName("PojoClass")
    builder.addPackage(JavaPackage("com.guide.city.model", "PojoClass"))
    builder.addField(JavaField("id", IntegerType, PrivateModifier))
    builder.addField(JavaField("name", StringType, ProtectedModifier))
    builder.addField(JavaField("age", IntegerType, PrivateModifier))
    builder.addField(JavaField("position", StringType, PrivateModifier))
    builder.getJavaClass
  }

  def createLocationClass: JavaClass = {
    val builder = new ClassBuilder
    builder.setName("Location")
    builder.addPackage(JavaPackage("com.test.model", "Location"))
    builder.addField(JavaField("accountId", IntegerType, PrivateModifier))
    builder.addField(JavaField("latitude", FloatType, PrivateModifier))
    builder.addField(JavaField("longitude", FloatType, PrivateModifier))
    builder.addField(JavaField("mark", StringType, PrivateModifier))
    builder.getJavaClass
  }

  def createAccountClass: JavaClass = {
    val builder = new ClassBuilder
    builder.setName("Account")
    builder.addPackage(JavaPackage("com.test.model", "Account"))
    builder.addField(JavaField("internalId", IntegerType, PrivateModifier))
    builder.addField(JavaField("androidId", StringType, PrivateModifier))
    builder.addField(JavaField("created", DateType, PrivateModifier))
    builder.getJavaClass
  }

  private def createAnotherClass: JavaClass = {
    val builder = new ClassBuilder
    builder.setName("AnotherClass")
    val functionTest = new FunctionBuilderTest
    builder.addFunction(functionTest.createFilterFunction)
    builder.addFunction(functionTest.createInvokeAnotherFunction)
    builder.addFunction(functionTest.createInvokeFunction)
    builder.getJavaClass
  }

  private def createWithAnnotationClass: JavaClass = {
    val builder = new ClassBuilder
    builder.setName("FullClass")
    val functionTest = new FunctionBuilderTest
    builder.addField(JavaField("id", StringType, PrivateModifier)(SimpleAnnotation("Id", "javax.persistence")))
    builder.addField(JavaField("name", StringType, PrivateModifier)(
      SimpleAnnotation("Column", "javax.persistence")("name", "fc_name")))
    builder.addField(JavaField("age", IntegerType, PrivateModifier))
    builder.addField(JavaField("position", StringType, ProtectedModifier))
    builder.addFunction(functionTest.createFilterFunction)
    builder.addFunction(functionTest.createInvokeAnotherFunction)
    builder.addFunction(functionTest.createInvokeFunction)
    builder.addFunction(functionTest.createToStringFunction)
    builder.addPackage(JavaPackage("my.test.package", "FullClass"))
    builder.addAnnotation(SimpleAnnotation("Entity", "javax.persistence")("table", "simple_table"))
    val annotation: JavaAnnotation = new JavaAnnotation("NamedQuery", JavaPackage("javax.persistence", "NamedQuery"))
    annotation.addParameter("name", "findAll")
    annotation.addParameter("query", "SELECT * FROM simple_table")
    builder.addAnnotation(annotation)
    builder.getJavaClass
  }

  private def createInheritClass: JavaClass = {
    val builder = new ClassBuilder(createWithAnnotationClass)
    builder.addExtend(createAnotherClass)
    val interfaceTest = new InterfaceBuilderTest
    builder.addImplements(interfaceTest.createWithPackageInterface())
    builder.addImplements(interfaceTest.createWithFunctionsInterface())
    builder.getJavaClass
  }

  private def createGenericInheritClass: JavaClass = {
    val builder = new ClassBuilder(createWithAnnotationClass)
    builder.addExtend(createAnotherClass)
    val interfaceTest = new InterfaceBuilderTest
    builder.addImplements(interfaceTest.createGenericInterface())
    builder.getJavaClass
  }

}
