package com.liberty.operations

import com.liberty.types.ConstructedType

/**
 * User: Dimitr
 * Date: 07.11.13
 * Time: 10:27
 */
class ConstructorInvokeOperation(typeToConstruct: ConstructedType, parameters: List[Expression] = Nil)
    extends FunctionInvokeOperation(typeToConstruct.getConstructor(), parameters)
