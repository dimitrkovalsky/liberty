package com.liberty.builders

import com.liberty.entities.{FunctionParameter, JavaFunction}
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

    def setName(name: String) = function.name = name

    def setOutputType(dataType: types.DataType) = function.output = dataType

    def addParam(param: FunctionParameter) = function.addParameter(param)

    def addParams(params: FunctionParameter*) = {
        for (param <- params)
            function.addParameter(param)
    }

    def getFunction = function

    def addOperation(operation:Operation) = function.body.addOperation(operation)
}
