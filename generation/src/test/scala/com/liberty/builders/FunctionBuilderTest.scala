package com.liberty.builders

import org.junit.{Assert, Test}
import com.liberty.types
import com.liberty.entities.FunctionParameter
import com.liberty.types.primitives._
import com.liberty.types.collections._
import com.liberty.operations._

class FunctionBuilderTest {

    @Test def emptyFunctionTest() {
        val builder = new FunctionBuilder
        builder.setName("create")
        builder.setOutputType(new types.Void)
        builder.addParams(FunctionParameter("name", StringType),
            FunctionParameter("languages", MapType(StringType, IntegerType)))
        val function = builder.getFunction
        val resultString = "void create(String name, Map<String, Integer> languages){" +
            "\n\t// Empty body\n}"
        // println(function.toString)
        Assert.assertEquals(resultString, function.toString)

    }

    @Test def withBodyTest() {
        val builder = new FunctionBuilder
        builder.setName("filter")
        builder.setOutputType(ListType(StringType))
        val paramName = "list"
        builder.addParam(FunctionParameter(paramName, ListType(StringType)))
        val result = "result"
       // builder.addOperation(CreationOperation(result, ArrayListType(StringType)))
        builder.addOperation(FunctionInvokeOperation("validate", List(result)))
        builder.addOperation(ReturnOperation(FunctionInvokeOperation("split", List(paramName, result))))
        val function = builder.getFunction
        val resultString = "List<String> filter(List<String> list){" +
            "\n\tArrayList<String> result = new ArrayList();" +
            "\n\tvalidate(result)" +
            "\n\treturn split(list, result);\n}"
        //println(function.toString)
        Assert.assertEquals(resultString, function.toString)

    }
}


