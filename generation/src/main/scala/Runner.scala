import com.liberty.operations.ConstructorInvokeOperation
import com.liberty.types.{primitives, collections, DataType}

object Runner {
    def main(args: Array[String]) {
        val data: DataType = collections
            .ListType(collections.MapType(primitives.IntegerType, collections.SetType(primitives.StringType)))

        println(data)
    }
}
