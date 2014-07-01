package com.liberty.model

/**
 * User: Dimitr
 * Date: 15.09.13
 * Time: 11:16
 */
sealed trait Modifier {}

case object DefaultModifier extends Modifier {
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

case object PublicStaticModifier extends Modifier {
  override def toString: String = "public static"
}

case object PrivateStaticModifier extends Modifier {
  override def toString: String = "private static"
}

case object ProtectedStaticModifier extends Modifier {
  override def toString: String = "protected static"
}

case object StaticModifier extends Modifier {
  override def toString: String = "static"
}
