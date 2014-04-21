package com.liberty.generators.adapters

import org.junit.{Assert, Test}
import com.liberty.model.JavaClass
import com.liberty.builders.ClassBuilderTest

/**
 * User: Dimitr
 * Date: 01.11.13
 * Time: 10:12
 */
class MongoAdapterTest {
    @Test def createAccessors() {
        val initialClass = createClass()
        assertInitialClass(initialClass)
        val adapter = new MongoAdapter(initialClass)
        val accessible = adapter.getAccessible
     //   println(accessible)
        val expected = "package com.guide.city.model;\n\nimport java.lang.String;\nimport java.lang.Integer;\nimport java.util.List;\n\nclass FullClass {\n\tprivate String name = \"\";\n\tprivate Integer age = 0;\n\tprotected String position = \"\";\n\n\tList<String> filter(List<String> list){\n\t\tArrayList<String> result = new ArrayList();\n\t\tvalidate(result);\n\t\treturn split(list, result);\n\t}\n\n\tString invokeAnotherFunction(List<String> list){\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n\n\tString invoke(List<String> list) throws Exception {\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n\n\tpublic String getName(){\n\t\treturn name;\n\t}\n\n\tpublic Integer getAge(){\n\t\treturn age;\n\t}\n\n\tpublic String getPosition(){\n\t\treturn position;\n\t}\n\n\tpublic void setName(String name){\n\t\tthis.name = name;\n\t}\n\n\tpublic void setAge(Integer age){\n\t\tthis.age = age;\n\t}\n\n\tpublic void setPosition(String position){\n\t\tthis.position = position;\n\t}\n}"
        Assert.assertEquals(expected, accessible.toString)
        assertInitialClass(initialClass)

        // Test add accessors into initial class
        adapter.addAccessors()
      //  println(initialClass)
        Assert.assertEquals(expected, initialClass.toString)
    }

    @Test def createAccessorsAndAnnotations() {
        val clazz = createClass()
        assertInitialClass(clazz)
        val adapter = new MongoAdapter(clazz)
        adapter.addAccessors()
        adapter.annotateClass()
        val expected = "package com.guide.city.model;\n\nimport com.google.code.morphia.annotations.Entity;\nimport java.lang.String;\nimport com.google.code.morphia.annotations.Id;\nimport java.util.List;\nimport java.lang.Integer;\n\n@Entity(value = \"fullclass\", noClassnameStored = true)\nclass FullClass {\n\t@Id\n\tprivate String name = \"\";\n\tprivate Integer age = 0;\n\tprotected String position = \"\";\n\n\tList<String> filter(List<String> list){\n\t\tArrayList<String> result = new ArrayList();\n\t\tvalidate(result);\n\t\treturn split(list, result);\n\t}\n\n\tString invokeAnotherFunction(List<String> list){\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n\n\tString invoke(List<String> list) throws Exception {\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n\n\tpublic String getName(){\n\t\treturn name;\n\t}\n\n\tpublic Integer getAge(){\n\t\treturn age;\n\t}\n\n\tpublic String getPosition(){\n\t\treturn position;\n\t}\n\n\tpublic void setName(String name){\n\t\tthis.name = name;\n\t}\n\n\tpublic void setAge(Integer age){\n\t\tthis.age = age;\n\t}\n\n\tpublic void setPosition(String position){\n\t\tthis.position = position;\n\t}\n}"
        println(clazz)
        Assert.assertEquals(expected, clazz.toString)
    }

    private def createClass(): JavaClass = {
        new ClassBuilderTest().createFullClass
    }

    private def assertInitialClass(initialClass: JavaClass) = {
        val expected = "package com.guide.city.model;\n\nimport java.lang.String;\nimport java.lang.Integer;\nimport java.util.List;\n\nclass FullClass {\n\tprivate String name = \"\";\n\tprivate Integer age = 0;\n\tprotected String position = \"\";\n\n\tList<String> filter(List<String> list){\n\t\tArrayList<String> result = new ArrayList();\n\t\tvalidate(result);\n\t\treturn split(list, result);\n\t}\n\n\tString invokeAnotherFunction(List<String> list){\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n\n\tString invoke(List<String> list) throws Exception {\n\t\tBoolean result;\n\t\tresult = validate(new ArrayList(), list);\n\t\treturn split(list, result);\n\t}\n}"
        val available = initialClass.toString
        //println(available)
        Assert.assertEquals(expected, available)
    }
}
