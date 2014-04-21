package com.liberty.builders

import org.junit.{Assert, Test}
import com.liberty.types
import com.liberty.types.primitives._
import com.liberty.operations._
import com.liberty.types.collections.MapType
import com.liberty.types.collections.ListType
import com.liberty.model.{PublicModifier, JavaAnnotation, JavaFunction, FunctionParameter}
import com.liberty.types.collections._
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.VoidType
import com.liberty.operations.FunctionInvokeOperation
import com.liberty.operations.Variable
import com.liberty.types.collections.MapType
import com.liberty.types.collections.ListType
import com.liberty.operations.CreationOperation
import com.liberty.operations.ReturnOperation
import com.liberty.types.collections.ArrayListType

class FunctionBuilderTest {

    @Test def emptyFunction() {
        val builder = new FunctionBuilder
        builder.setName("create")
        builder.setOutputType(new types.VoidType)
        builder.addParams(FunctionParameter(Variable("name"), StringType),
            FunctionParameter(Variable("languages"), MapType(StringType, IntegerType)))
        val function = builder.getFunction
        val resultString = "void create(String name, Map<String, Integer> languages){" +
            "\n\t// Empty body\n}"
        // println(function.toString)
        Assert.assertEquals(resultString, function.toString)

    }

    @Test def withBody() {
        val function = createFilterFunction
        val resultString = "List<String> filter(List<String> list){" +
            "\n\tArrayList<String> result = new ArrayList();" +
            "\n\tvalidate(result);" +
            "\n\treturn split(list, result);\n}"
        //println(function.toString)
        Assert.assertEquals(resultString, function.toString)

    }


    @Test def functionInvokeFunctionsTest() {
        val function = createInvokeAnotherFunction

        val resultString = "String invokeAnotherFunction(List<String> list){\n\tBoolean result;\n\tresult = validate(new ArrayList(), list);\n\treturn split(list, result);\n}"
        // println(function.toString)
        Assert.assertEquals(resultString, function.toString)
    }

    @Test def throwFunction() {
        val function = createInvokeFunction

        val resultString = "String invoke(List<String> list) throws Exception {\n\tBoolean result;\n\tresult = validate(new ArrayList(), list);\n\treturn split(list, result);\n}"
        // println(function.toString)
        Assert.assertEquals(resultString, function.toString)
    }

    def createInvokeFunction: JavaFunction = {
        val builder = new FunctionBuilder
        builder.setName("invoke")
        builder.setOutputType(StringType)
        val input = Variable("list")
        builder.addParam(FunctionParameter(input, ListType(StringType)))
        val result = Variable("result")
        builder.addOperation(VariableDeclarationOperation(result, BooleanType))
        builder.addOperation(
            FunctionInvokeOperation("validate", List(CreationOperation(ArrayListType(StringType)), input), result))
        builder.addOperation(ReturnOperation(FunctionInvokeOperation("split", List(input, result))))
        builder.addThrow("Exception")
        builder.getFunction
    }

    def createInvokeAnotherFunction: JavaFunction = {
        val builder = new FunctionBuilder
        builder.setName("invokeAnotherFunction")
        builder.setOutputType(StringType)
        val input = Variable("list")
        builder.addParam(FunctionParameter(input, ListType(StringType)))

        val result = Variable("result")
        builder.addOperation(VariableDeclarationOperation(result, BooleanType))
        builder.addOperation(
            FunctionInvokeOperation("validate", List(CreationOperation(ArrayListType(StringType)), input), result))
        builder.addOperation(ReturnOperation(FunctionInvokeOperation("split", List(input, result))))
        builder.getFunction
    }

    def createFilterFunction: JavaFunction = {
        val builder = new FunctionBuilder
        builder.setName("filter")
        builder.setOutputType(ListType(StringType))
        val paramName = Variable("list")
        builder.addParam(FunctionParameter(paramName, ListType(StringType)))
        val result = Variable("result")
        builder.addOperation(CreationOperation(ArrayListType(StringType), result))
        builder.addOperation(FunctionInvokeOperation("validate", List(result)))
        builder.addOperation(ReturnOperation(FunctionInvokeOperation("split", List(paramName, result))))
        builder.getFunction
    }

    def createToStringFunction: JavaFunction = {
        val builder = new FunctionBuilder
        builder.setName("toString")
        builder.setOutputType(StringType)
        builder.addAnnotation(JavaAnnotation("Override"))
        builder.addOperation(ReturnOperation(Value("\"toString invoked\"")))
        builder.addModifier(PublicModifier)
        builder.getFunction
    }
}


