package com.liberty.builders

import com.liberty.StubType
import com.liberty.model._
import com.liberty.operations.Variable
import com.liberty.traits.JavaPackage
import com.liberty.types.collections
import com.liberty.types.primitives.{BooleanType, IntegerType, StringType}
import org.junit.{Assert, Test}

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
    val expect = "public interface Marker {}"
    //println(interface)
    Assert.assertEquals(expect, interface.toString)
  }

  @Test def genericInterface() {
    val builder = new InterfaceBuilder
    builder.setName("Marker")
    builder.addGeneric(StubType("SomeType"))
    builder.addGeneric(StubType("AnotherType"))
    // Need to ignore the same type
//    builder.addGeneric(StubType("AnotherType"))
    val interface = builder.getInterface
    val expect = "public interface Marker<SomeType, AnotherType> {}"
    println(interface)
    Assert.assertEquals(expect, interface.toString)
  }

  @Test def withPackageInterface() {
    val interface = createWithPackageInterface()
    val expect = "package my.test.program;\n\npublic interface Marker {}"
    //  println(interface)
    Assert.assertEquals(expect, interface.toString)
  }

  @Test def withFunctions() {
    val interface = createWithFunctionsInterface()
    val expect = "import java.lang.Boolean;\nimport java.util.List;\nimport java.util.ArrayList;\n\npublic interface TestInterface {\n\n\tvoid test();\n\n\tBoolean validate();\n\n\tBoolean validate(List<String> names);\n\n\tBoolean validate(ArrayList<Integer> costs) throws CostException, ValidationException;\n\n\tList<String> getData();\n}"
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
      List(JavaException("CostException"), JavaException("ValidationException"))))
    builder.addFunctionSignature(new FunctionSignature("getData", collections.ListType(StringType)))
    builder.getInterface
  }

  def createGenericInterface(): JavaInterface = {
    val builder = new InterfaceBuilder
    builder.setName("GenericInterface")
    builder.addFunctionSignature(new FunctionSignature("test"))
    builder.addGeneric("SomeType")
    builder.getInterface

  }
}
