package com.liberty.builders

import org.junit.{Assert, Test}
import com.liberty.entities.{ProtectedModifier, PrivateModifier, JavaField}
import com.liberty.types.primitives
import com.liberty.types.primitives._

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

    @Test def functionsClass() {
        val builder = new ClassBuilder
        builder.setName("AnotherSomeClass")
        val functionTest = new FunctionBuilderTest
        builder.addFunction(functionTest.createFilterFunction)
        builder.addFunction(functionTest.createInvokeAnotherFunction)
        builder.addFunction(functionTest.createInvokeFunction)
        val clazz = builder.getJavaClass

        val expected = "class AnotherSomeClass {\n\tList<String> filter(List<String> list){\n\t\tArrayList<String> result = new ArrayList();\n\t\tvalidate(result);\n\t\treturn split(list, result);\n\t}\n\n\tString invokeAnotherFunction(List<String> list){\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n\n\tString invoke(List<String> list) throws Exception {\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n}"
        val available = clazz.toString
        //println(available)
        Assert.assertEquals(expected, available)
    }

    @Test def fullClass() {
        val builder = new ClassBuilder
        builder.setName("FullClass")
        val functionTest = new FunctionBuilderTest
        builder.addField(JavaField("name", StringType, PrivateModifier))
        builder.addField(JavaField("age", IntegerType, PrivateModifier))
        builder.addField(JavaField("position", StringType, ProtectedModifier))
        builder.addFunction(functionTest.createFilterFunction)
        builder.addFunction(functionTest.createInvokeAnotherFunction)
        builder.addFunction(functionTest.createInvokeFunction)

        val clazz = builder.getJavaClass

        val expected = "class FullClass {\n\tprivate String name = \"\";\n\tprivate Integer age = 0;\n\tprotected String position = \"\";\n\n\tList<String> filter(List<String> list){\n\t\tArrayList<String> result = new ArrayList();\n\t\tvalidate(result);\n\t\treturn split(list, result);\n\t}\n\n\tString invokeAnotherFunction(List<String> list){\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n\n\tString invoke(List<String> list) throws Exception {\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n}"
        val available = clazz.toString
        //println(available)
        Assert.assertEquals(expected, available)
    }
}
