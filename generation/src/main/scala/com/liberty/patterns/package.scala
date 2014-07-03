package com.liberty

/**
 * User: Dimitr
 * Date: 05.09.13
 * Time: 11:52
 */
// TODO : add formatter support
package object patterns {
  def JavaFunctionPattern(signature: String, body: String): String = {
    s"$signature{\n\t$body\n}"
  }

  def JavaFunctionSignaturePattern(modifier: String, output: String, functionName: String, parameters: String,
                                   functionThrows: List[String]): String = {
    val thr = functionThrows match {
      case Nil => ""
      case x :: xs => " throws " + functionThrows.mkString(", ") + " "
    }
    val mod = if (modifier.isEmpty) modifier else modifier + " "
    s"$mod$output $functionName($parameters)$thr"
  }

  def JavaFunctionInterfacePattern(signature: String): String = {
    signature.trim + ";"
  }

  def JavaInterfacePattern(jPackage: String, imports: String, name: String, generics: String, signatures: String): String = {
    s"$jPackage\n$imports\n\ninterface $name$generics {\n\n\t$signatures\n}"
  }

  def JavaMarkerInterfacePattern(jPackage: String, name: String, generics: String): String = {
    s"${if (jPackage.isEmpty) "" else jPackage + "\n\n"}interface $name$generics {}"
  }

  // TODO : Change all empty strings into None
  def JavaClassPattern(jPackage: String, imports: String, annotations: String, name: String, generics: String,
                       inherit: String, fields: String, staticBlocks: String, functions: String): String = {
    s"${
      if (jPackage.isEmpty) ""
      else {
        jPackage + "\n\n"
      }
    }${
      if (imports.isEmpty) ""
      else {
        imports + "\n\n"
      }
    }${annotations}${
      if (fields.isEmpty && functions.isEmpty && staticBlocks.isEmpty)
        s"class $name$generics$inherit {}"
      if (fields.isEmpty && staticBlocks.isEmpty)
        s"class $name$generics$inherit {\n$functions\n}"
      else if (functions.isEmpty && staticBlocks.isEmpty)
        s"class $name$generics$inherit {\n\t$fields\n}"
      else if (staticBlocks.isEmpty) s"class $name$generics$inherit {\n\t$fields\n\n$functions\n}"
      else s"class $name$generics$inherit {\n\t$fields\n\n$staticBlocks\n\n$functions\n}"
    }"
  }
}
