package liberty.entities

import liberty.types.RecognitionDataType

/**
 * User: dkovalskyi
 * Date: 15.07.13
 * Time: 15:21
 */
class DataTypeEntity(var dataType: Int) {
  var data: Object = _

  override def toString = {
    dataType match {
      case RecognitionDataType.VOID => "void"
      case RecognitionDataType.BYTE => "byte"
      case RecognitionDataType.INTEGER => "int"
      case RecognitionDataType.FLOAT => "float"
      case RecognitionDataType.DOUBLE => "double"
      case RecognitionDataType.OBJECT => "object"
    }
  }
}

case class VariableEntity(var dataType: DataTypeEntity, var name: String) {
  def toFunctionParam: String = {
    dataType + " " + name
  }
}
