package com.liberty.entities

/**
 * User: Dimitr
 * Date: 15.09.13
 * Time: 11:16
 */
trait Modifier {}

case object DefaultModifier extends Modifier{
    override def toString: String = ""
}

case object PrivateModifier extends Modifier {
    override def toString: String = "private"
}

case object PublicModifier extends Modifier {
    override def toString: String = "public"
}

case object ProtectedModifier extends Modifier {
    override def toString: String = "protected"
}

