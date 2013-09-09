package com.liberty

/**
 * User: Dimitr
 * Date: 05.09.13
 * Time: 11:52
 */
package object patterns {
    def JavaFunctionPattern(output: String, functionName: String, parameters: String, body: String): String = {
        s"$output $functionName($parameters){" +
            s"\n\t$body\n}"
    }
}
