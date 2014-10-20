import com.liberty.builders.ClassBuilder
import com.liberty.common.ProjectConfig
import com.liberty.controllers.{BeanController, RestController}
import com.liberty.generators.adapters.PostgresAdapter
import com.liberty.model.{JavaClass, JavaField, PrivateModifier}
import com.liberty.traits.{JavaPackage, LocationPackage}
import com.liberty.types.primitives.{IntegerType, LongType, StringType}

object Runner {

  def main(args: Array[String]) {
    val controller = new BeanController()
    controller.createBean(getModel)

    val rsController = new RestController
    rsController.createRest(getModel)
  }

  class Data(data: String) {}

  def getModel: JavaClass = {
    val basePackage = ProjectConfig.basePackage
    val builder = new ClassBuilder
    builder.setName("Subject")
    builder.addField(JavaField("id", IntegerType, PrivateModifier))
    builder.addField(JavaField("name", StringType, PrivateModifier))
    builder.addField(JavaField("lecturer", StringType, PrivateModifier))
    builder.addField(JavaField("departmentId", StringType, PrivateModifier))
    builder.addPackage(basePackage.nested("models", "Subject"))
    //    println(   builder.getJavaClass)
    builder.getJavaClass
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


