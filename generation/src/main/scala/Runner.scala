import com.liberty.model.JavaInterface

object Runner {
  def main(args: Array[String]) {
    val in = new JavaInterface
    in.name = "some"
    val other = new JavaInterface
    other.name = "some"
    println(in.equals(other))
  }
}


