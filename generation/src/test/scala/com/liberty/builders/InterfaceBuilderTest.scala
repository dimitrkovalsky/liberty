package com.liberty.builders

import org.junit.{Assert, Test}
import com.liberty.model.{JavaInterface, FunctionParameter, FunctionSignature}
import com.liberty.types.{collections, primitives}
import com.liberty.types.primitives.{IntegerType, BooleanType, StringType}
import com.liberty.operations.Variable
import com.liberty.traits.JavaPackage
import com.liberty.{StubType, types}

/**
 * User: Dimitr
 * Date: 14.09.13
 * Time: 11:54
 */
class InterfaceBuilderTest {
    @Test def markerInterface() {
        val builder = new InterfaceBuilder
        builder.setName("Marker")
        val interface = builder.getInterface
        val expect = "interface Marker {}"
        println(interface)
        Assert.assertEquals(expect, interface.toString)
    }

    @Test def genericInterface() {
        val builder = new InterfaceBuilder
        builder.setName("Marker")
        builder.addGeneric(StubType("SomeType"))
        builder.addGeneric(StubType("AnotherType"))
        // Need to ignore the same type
        builder.addGeneric(StubType("AnotherType"))
        val interface = builder.getInterface
        val expect = "interface Marker<SomeType, AnotherType> {}"
        //println(interface)
        Assert.assertEquals(expect, interface.toString)
    }

    @Test def withPackageInterface() {
        val interface = createWithPackageInterface()
        val expect = "package my.test.program;\n\ninterface Marker {}"
      //  println(interface)
        Assert.assertEquals(expect, interface.toString)
    }

    @Test def withFunctions() {
        val interface = createWithFunctionsInterface()
        val expect = "interface TestInterface {\n\tvoid test();\n\tBoolean validate();\n\tBoolean validate(List<String> names);\n\tBoolean validate(ArrayList<Integer> costs) throws CostException, ValidationException;\n\tList<String> getData();\n}"
        //println(interface)
        Assert.assertEquals(expect, interface.toString)
    }

    def createWithPackageInterface(): JavaInterface = {
        val builder = new InterfaceBuilder
        builder.setName("Marker")
        builder.addPackage(JavaPackage("my.test.program", "Marker"))
        builder.getInterface
    }

    def createWithFunctionsInterface(): JavaInterface = {
        val builder = new InterfaceBuilder
        builder.setName("TestInterface")
        builder.addFunctionSignature(new FunctionSignature("test"))
        builder.addFunctionSignature(new FunctionSignature("validate", BooleanType))
        builder.addFunctionSignature(new FunctionSignature("validate", BooleanType,
            List(FunctionParameter(Variable("names"), collections.ListType(StringType)))))
        builder.addFunctionSignature(new FunctionSignature("validate", BooleanType,
            List(FunctionParameter(Variable("costs"), collections.ArrayListType(IntegerType))),
            List("CostException", "ValidationException")))
        builder.addFunctionSignature(new FunctionSignature("getData", collections.ListType(StringType)))
        builder.getInterface
    }

    def createGenericInterface():JavaInterface  = {
        val builder = new InterfaceBuilder
        builder.setName("GenericInterface")
        builder.addFunctionSignature(new FunctionSignature("test"))
        builder.addGeneric("SomeType")
        builder.getInterface

    }
}
