package com.liberty.errors

/**
 * User: Dimitr
 * Date: 05.09.13
 * Time: 11:21
 */
class Error(message: String) {
  override def toString: String = "<ERROR> Message : " + message
}
