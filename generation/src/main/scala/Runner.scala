import com.liberty.entities.JavaInterface
import com.liberty.operations.ConstructorInvokeOperation
import com.liberty.types.{primitives, collections, DataType}

object Runner {
    def main(args: Array[String]) {
        val in = new JavaInterface
        in.name = "some"
        val other = new JavaInterface
        other.name = "some"
        println(in.equals(other))
    }
}


