import com.liberty.builders.ClassBuilder
import com.liberty.generators.adapters.PostgresAdapter
import com.liberty.model.{JavaField, PrivateModifier}
import com.liberty.traits.{JavaPackage, LocationPackage}
import com.liberty.types.primitives.{LongType, StringType}

object Runner {
  def main(args: Array[String]) {
    val basePackage = LocationPackage("standard")
    val builder = new ClassBuilder
    builder.setName("Institute")
    builder.addField(JavaField("id", LongType, PrivateModifier))
    builder.addField(JavaField("name", StringType, PrivateModifier))
    builder.addField(JavaField("head", LongType, PrivateModifier))
    builder.addPackage(JavaPackage("standard.models", "Institute"))
    val pojo = builder.getJavaClass

    val adapter = new PostgresAdapter(pojo, basePackage)
    adapter.addAccessors()
    adapter.annotateClass()

    println(adapter.createEntity)
    println(adapter.createDaoInterface.get)
    println(adapter.createDao.get)
  }
}


