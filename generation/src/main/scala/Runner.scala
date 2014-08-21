import com.liberty.builders.ClassBuilder
import com.liberty.generators.adapters.PostgresAdapter
import com.liberty.model.{JavaField, PrivateModifier}
import com.liberty.traits.{JavaPackage, LocationPackage}
import com.liberty.types.primitives.{LongType, StringType}

object Runner {
  def main(args: Array[String]) {
    val basePackage = LocationPackage("standard")
    val builder = new ClassBuilder
    builder.setName("Student")
    builder.addField(JavaField("id", LongType, PrivateModifier))
    builder.addField(JavaField("firstName", StringType, PrivateModifier))
    builder.addField(JavaField("lastName", StringType, PrivateModifier))
    builder.addField(JavaField("department", StringType, PrivateModifier))
    builder.addPackage(JavaPackage("standard.models", "Student"))
    val student = builder.getJavaClass

    val adapter = new PostgresAdapter(student, basePackage)
    adapter.addAccessors()
    adapter.annotateClass()

    println(adapter.createEntity)
    //println(adapter.createDaoInterface.get)
  }
}


