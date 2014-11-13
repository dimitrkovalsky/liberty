import com.liberty.builders.ClassBuilder
import com.liberty.common.ProjectConfig
import com.liberty.generators.adapters.PostgresAdapter
import com.liberty.model.{JavaClass, JavaField, PrivateModifier}
import com.liberty.traits.{JavaPackage, LocationPackage}
import com.liberty.types.primitives.{IntegerType, LongType, StringType}

object Runner {

  def main(args: Array[String]) {
  }

  class Data(data: String) {}

  def getModel: JavaClass = {
    val basePackage = ProjectConfig.basePackage
    val builder = new ClassBuilder
    builder.setName("Student")
    builder.addField(JavaField("id", LongType, PrivateModifier))
    builder.addField(JavaField("firstName", StringType, PrivateModifier))
    builder.addField(JavaField("lastName", StringType, PrivateModifier))
    builder.addField(JavaField("department", IntegerType, PrivateModifier))
    builder.addPackage(basePackage.nested("models", "Subject"))
    //    println(   builder.getJavaClass)
    builder.getJavaClass
  }

  def generate() {
    val basePackage = LocationPackage("com.test")
    val builder = new ClassBuilder
    builder.setName("UserRole")
    builder.addField(JavaField("id", LongType, PrivateModifier))
    builder.addField(JavaField("role", StringType, PrivateModifier))
    builder.addPackage(JavaPackage("com.test.models", "UserRole"))
    val student = builder.getJavaClass

    val adapter = new PostgresAdapter(student, basePackage)
    adapter.addAccessors()
    adapter.annotateClass()

    //println(adapter.createEntity)
  }
}


