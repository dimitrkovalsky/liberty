import com.liberty.builders.ClassBuilder
import com.liberty.helpers.Cloner
import com.liberty.model.{JavaField, PrivateModifier}
import com.liberty.traits.JavaPackage
import com.liberty.types.primitives.{IntegerType, StringType}
import com.liberty.types.standardTypes.DateType

object Runner {
  def main(args: Array[String]) {
    val builder = new ClassBuilder
    builder.setName("Account")
    builder.addPackage(JavaPackage("com.test.model", "Account"))
    builder.addField(JavaField("internalId", IntegerType, PrivateModifier))
    builder.addField(JavaField("androidId", StringType, PrivateModifier))
    builder.addField(JavaField("created", DateType, PrivateModifier))
    val c = builder.getJavaClass

    println(c.deepCopy)
  }
}


