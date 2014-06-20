package com.liberty.exceptions

/**
 * Created by Dmytro_Kovalskyi on 19.06.2014.
 */
abstract class GenerationException(message: String) extends Exception(message)

case class IdMissedException(message: String = "Id is missed") extends GenerationException(message)
