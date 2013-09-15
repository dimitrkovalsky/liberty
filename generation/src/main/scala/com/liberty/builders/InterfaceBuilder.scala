package com.liberty.builders

import com.liberty.entities.{FunctionSignature, JavaInterface}

/**
 * User: Dimitr
 * Date: 14.09.13
 * Time: 11:50
 */
// TODO: Add inheritance support
class InterfaceBuilder {
    private val javaInterface = new JavaInterface

    def setName(name: String) {
        javaInterface.name = name
    }

    def addFunctionSignature(signature: FunctionSignature) {
        javaInterface.addSignature(signature)
    }

    def getInterface = javaInterface
}
