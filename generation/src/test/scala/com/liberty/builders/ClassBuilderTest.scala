package com.liberty.builders

import org.junit.{Assert, Test}
import com.liberty.entities._
import com.liberty.types.primitives._
import com.liberty.traits.JavaPackage
import com.liberty.entities.JavaAnnotation
import com.liberty.traits.JavaPackage
import com.liberty.entities.JavaField
import com.liberty.StubType

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

        val expected = "class Empty {}"
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

        val expected = "class SomeClass {\n\tprivate String name = \"\";\n\tprivate Integer age = 0;\n\tprotected String position = \"\";\n}"
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

        val expected = "class SomeClass<GenericClass> {\n\tprivate String name = \"\";\n\tprivate Integer age = 0;\n\tprotected String position = \"\";\n}"
        val available = clazz.toString
        //println(available)
        Assert.assertEquals(expected, available)
    }

    @Test def genericWithInheritanceClass() {
        val clazz = createGenericInheritClass

        val expected = "package my.test.package;\n\nimport javax.persistence.Entity;\nimport java.lang.String;\nimport javax.persistence.Id;\nimport javax.persistence.Column;\nimport java.util.List;\nimport javax.persistence.NamedQuery;\nimport java.lang.Integer;\n\n@Entity(table = \"simple_table\")\n@NamedQuery(name = \"findAll\", query = \"SELECT * FROM simple_table\")\nclass FullClass extends AnotherClass implements GenericInterface<SomeType> {\n\t@Id\n\tprivate String id = \"\";\n\t@Column(name = \"fc_name\")\n\tprivate String name = \"\";\n\tprivate Integer age = 0;\n\tprotected String position = \"\";\n\n\tList<String> filter(List<String> list){\n\t\tArrayList<String> result = new ArrayList();\n\t\tvalidate(result);\n\t\treturn split(list, result);\n\t}\n\n\tString invokeAnotherFunction(List<String> list){\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n\n\tString invoke(List<String> list) throws Exception {\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n\n\t@Override\n\tpublic String toString(){\n\t\treturn \"toString invoked\";\n\t}\n}"
        val available = clazz.toString
        //println(available)
        Assert.assertEquals(expected, available)
    }


    @Test def functionsClass() {
        val clazz = createAnotherClass
        val expected = "class AnotherClass {\n\tList<String> filter(List<String> list){\n\t\tArrayList<String> result = new ArrayList();\n\t\tvalidate(result);\n\t\treturn split(list, result);\n\t}\n\n\tString invokeAnotherFunction(List<String> list){\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n\n\tString invoke(List<String> list) throws Exception {\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n}"
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

        val expected = "class AnotherSomeClass {\n\t@Override\n\tpublic String toString(){\n\t\treturn \"toString invoked\";\n\t}\n}"
        val available = clazz.toString
        //println(available)
        Assert.assertEquals(expected, available)
    }

    @Test def fullClass() {
        val clazz = createFullClass

        val expected = "package com.guide.city.entities;\n\nimport java.lang.String;\nimport java.lang.Integer;\nimport java.util.List;\n\nclass FullClass {\n\tprivate String name = \"\";\n\tprivate Integer age = 0;\n\tprotected String position = \"\";\n\n\tList<String> filter(List<String> list){\n\t\tArrayList<String> result = new ArrayList();\n\t\tvalidate(result);\n\t\treturn split(list, result);\n\t}\n\n\tString invokeAnotherFunction(List<String> list){\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n\n\tString invoke(List<String> list) throws Exception {\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n}"
        val available = clazz.toString
        //  println(available)
        Assert.assertEquals(expected, available)
    }

    @Test def withAnnotationClass() {
        val clazz = createWithAnnotationClass

        val expected = "package my.test.package;\n\nimport javax.persistence.Entity;\nimport java.lang.String;\nimport javax.persistence.Id;\nimport javax.persistence.Column;\nimport java.util.List;\nimport javax.persistence.NamedQuery;\nimport java.lang.Integer;\n\n@Entity(table = \"simple_table\")\n@NamedQuery(name = \"findAll\", query = \"SELECT * FROM simple_table\")\nclass FullClass {\n\t@Id\n\tprivate String id = \"\";\n\t@Column(name = \"fc_name\")\n\tprivate String name = \"\";\n\tprivate Integer age = 0;\n\tprotected String position = \"\";\n\n\tList<String> filter(List<String> list){\n\t\tArrayList<String> result = new ArrayList();\n\t\tvalidate(result);\n\t\treturn split(list, result);\n\t}\n\n\tString invokeAnotherFunction(List<String> list){\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n\n\tString invoke(List<String> list) throws Exception {\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n\n\t@Override\n\tpublic String toString(){\n\t\treturn \"toString invoked\";\n\t}\n}"
        val available = clazz.toString
        // println(available)
        Assert.assertEquals(expected, available)
    }

    @Test def withInheritanceClass() {
        val clazz = createInheritClass
        val expected = "package my.test.package;\n\nimport javax.persistence.Entity;\nimport java.lang.String;\nimport javax.persistence.Id;\nimport javax.persistence.Column;\nimport java.util.List;\nimport javax.persistence.NamedQuery;\nimport java.lang.Integer;\n\n@Entity(table = \"simple_table\")\n@NamedQuery(name = \"findAll\", query = \"SELECT * FROM simple_table\")\nclass FullClass extends AnotherClass implements Marker, TestInterface {\n\t@Id\n\tprivate String id = \"\";\n\t@Column(name = \"fc_name\")\n\tprivate String name = \"\";\n\tprivate Integer age = 0;\n\tprotected String position = \"\";\n\n\tList<String> filter(List<String> list){\n\t\tArrayList<String> result = new ArrayList();\n\t\tvalidate(result);\n\t\treturn split(list, result);\n\t}\n\n\tString invokeAnotherFunction(List<String> list){\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n\n\tString invoke(List<String> list) throws Exception {\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n\n\t@Override\n\tpublic String toString(){\n\t\treturn \"toString invoked\";\n\t}\n}"
        val available = clazz.toString
        //  println(available)
        Assert.assertEquals(expected, available)
    }

    def createFullClass: JavaClass = {
        val builder = new ClassBuilder
        builder.setName("FullClass")
        val functionTest = new FunctionBuilderTest
        builder.addPackage(JavaPackage("com.guide.city.entities"))
        builder.addField(JavaField("name", StringType, PrivateModifier, id = true))
        builder.addField(JavaField("age", IntegerType, PrivateModifier))
        builder.addField(JavaField("position", StringType, ProtectedModifier))
        builder.addFunction(functionTest.createFilterFunction)
        builder.addFunction(functionTest.createInvokeAnotherFunction)
        builder.addFunction(functionTest.createInvokeFunction)

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
        builder.addField(JavaField("id", StringType, PrivateModifier)(JavaAnnotation("Id",JavaPackage("javax.persistence"))))
        builder.addField(JavaField("name", StringType, PrivateModifier)(
            JavaAnnotation("Column", JavaPackage("javax.persistence"))("name", "fc_name")))
        builder.addField(JavaField("age", IntegerType, PrivateModifier))
        builder.addField(JavaField("position", StringType, ProtectedModifier))
        builder.addFunction(functionTest.createFilterFunction)
        builder.addFunction(functionTest.createInvokeAnotherFunction)
        builder.addFunction(functionTest.createInvokeFunction)
        builder.addFunction(functionTest.createToStringFunction)
        builder.addPackage(JavaPackage("my.test.package", "FullClass"))
        builder.addAnnotation(JavaAnnotation("Entity", JavaPackage("javax.persistence"))("table", "simple_table"))
        val annotation: JavaAnnotation = new JavaAnnotation("NamedQuery", JavaPackage("javax.persistence"))
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
