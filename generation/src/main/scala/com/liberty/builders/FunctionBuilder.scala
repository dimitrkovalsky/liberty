package com.liberty.builders

import com.liberty.entities.{FunctionSignature, FunctionParameter, JavaFunction}
import com.liberty.types
import com.liberty.operations.Operation

/**
 * User: Dimitr
 * Date: 05.09.13
 * Time: 10:52
 */
// TODO: In future use method getInstance for retrieving appropriate builder for some language
class FunctionBuilder {
    private val function = new JavaFunction

    def setName(name: String) = function.signature.name = name

    def setOutputType(dataType: types.DataType) = function.signature.output = dataType

    def addParam(param: FunctionParameter) = function.signature.addParameter(param)

    def addParams(params: FunctionParameter*) = {
        for (param <- params)
            function.signature.addParameter(param)
    }

    def addThrow(thr:String){
        function.signature.addThrow(thr)
    }

    def getFunction = function

    def addOperation(operation:Operation) = function.body.addOperation(operation)
}
