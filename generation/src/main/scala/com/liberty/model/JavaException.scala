package com.liberty.model

import com.liberty.traits.{NoPackage, JavaPackage}

/**
 * Created by Dmytro_Kovalskyi on 19.06.2014.
 */
class JavaException(name: String = "", jPackage: JavaPackage = new NoPackage) extends JavaClass(name, jPackage) {}

object StandardExceptions {

  object Exception extends JavaException("Exception", JavaPackage("java.lang", "Exception"))

}


object JavaException {
  def apply(name: String, jPackage: JavaPackage = new NoPackage) = new JavaException(name, jPackage)
}
