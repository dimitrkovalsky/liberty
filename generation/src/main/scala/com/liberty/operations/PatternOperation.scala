package com.liberty.operations

import com.liberty.traits.JavaPackage

/**
 * Created by Dmytro_Kovalskyi on 24.07.2014.
 */

/**
 * Represents difficult operation as string.
 * Do not use ';' in the end of pattern.
 * Will be interpreted as single operation
 * @param pattern pattern that should be inserted into result function
 * @param packages package that used int this function. Necessary for correct imports
 */
case class PatternOperation(pattern: String, packages: List[JavaPackage]) extends Operation {
  /**
   * Executes operation
   * @return string representation of operation invoke
   */
  override def execute(): Option[String] = {
    Some(pattern)
  }
}
