import com.liberty.builders.ClassBuilder
import com.liberty.generators.adapters.PostgresAdapter
import com.liberty.model.{JavaField, PrivateModifier}
import com.liberty.parsers.JavaClassParser
import com.liberty.traits.{JavaPackage, LocationPackage}
import com.liberty.types.primitives.{LongType, StringType}

object Runner {

  def main(args: Array[String]): Unit = {

    val clazz = JavaClassParser("DepartmentBean.tmpl").parse()
    println(clazz)

  }

  def generate() {
    val basePackage = LocationPackage("standard")
    val builder = new ClassBuilder
    builder.setName("UserRole")
    builder.addField(JavaField("id", LongType, PrivateModifier))
    builder.addField(JavaField("role", StringType, PrivateModifier))
    builder.addPackage(JavaPackage("standard.models", "UserRole"))
    val student = builder.getJavaClass

    val adapter = new PostgresAdapter(student, basePackage)
    adapter.addAccessors()
    adapter.annotateClass()

    //println(adapter.createEntity)
  }
}


