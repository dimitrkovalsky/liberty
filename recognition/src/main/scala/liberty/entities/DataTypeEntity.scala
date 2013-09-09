package liberty.entities

import liberty.types.DataType

/**
 * User: dkovalskyi
 * Date: 15.07.13
 * Time: 15:21
 */
class DataTypeEntity(var dataType: Int) {
    var data: Object = _

    override def toString = {
        dataType match {
            case DataType.VOID => "void"
            case DataType.BYTE => "byte"
            case DataType.INTEGER => "int"
            case DataType.FLOAT => "float"
            case DataType.DOUBLE => "double"
            case DataType.OBJECT => "object"
        }
    }
}

case class VariableEntity(var dataType: DataTypeEntity, var name: String) {
    def toFunctionParam: String = {
        dataType + " " + name
    }
}
