package com.liberty.model

import org.junit.{Assert, Test}

/**
 * User: Dimitr
 * Date: 10.10.13
 * Time: 10:50
 */
class JavaAnnotationTest {
    @Test def withoutParams() {
        val annotation: JavaAnnotation = new JavaAnnotation("Override")
        val expected = "@Override"
        val available = annotation.toString
     //   println(available)
        Assert.assertEquals(expected, available)
    }

    @Test def withOneParam() {
        val annotation: JavaAnnotation = new JavaAnnotation("Entity")
        annotation.addParameter("table", "simple")
        val expected = "@Entity(table = \"simple\")"
        val available = annotation.toString
        //println(available)
        Assert.assertEquals(expected, available)
    }

    @Test def withTwoParams() {
        val annotation: JavaAnnotation = new JavaAnnotation("NamedQuery")
        annotation.addParameter("name", "findAll")
        annotation.addParameter("query", "SELECT * FROM simple_table")
        val expected = "@NamedQuery(name = \"findAll\", query = \"SELECT * FROM simple_table\")"
        val available = annotation.toString
        //println(available)
        Assert.assertEquals(expected, available)
    }

    @Test def changeParameter() {
        val annotation: JavaAnnotation = new JavaAnnotation("NamedQuery")
        annotation.addParameter("name", "find")
        annotation.addParameter("query", "SELECT * FROM simple_table")
        var expected = "@NamedQuery(name = \"find\", query = \"SELECT * FROM simple_table\")"
        var available = annotation.toString
        //println(available)
        Assert.assertEquals(expected, available)

        annotation.changeParameter("name", "findAll")
        available = annotation.toString
        expected = "@NamedQuery(name = \"findAll\", query = \"SELECT * FROM simple_table\")"
       // println(available)
        Assert.assertEquals(expected, available)
    }
}
