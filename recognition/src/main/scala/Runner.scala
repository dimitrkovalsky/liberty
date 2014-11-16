import com.liberty.builders.ClassBuilder
import com.liberty.common.emulation._
import com.liberty.common.{GrammarGroups, GrammarIds, ProjectConfig}
import com.liberty.model.{JavaClass, JavaField, PrivateModifier}
import com.liberty.transmission.TransmissionManager
import com.liberty.types.primitives.{FloatType, IntegerType, LongType, StringType}

/**
 * User: dimitr
 * Date: 19.10.2014
 * Time: 13:01
 */
object Runner {
  def main(args: Array[String]) {
    emulate() // will be executed after transmission start
    TransmissionManager.startDataTransmission(emulate = true)
  }

  def emulate(): Unit = {
    VoiceEmulator.emulate {
     // GrammarIds.CREATE_PROJECT *> 0 //0 - timeout, the same as *>()
     // GrammarIds.NAME_OF_PROJECT :> "simple" // set name
      GrammarGroups.COMPONENT_CREATION <* 0 // Change active grammar to COMPONENT_CREATION
      GrammarIds.CREATE_CLASS *> 0
      GrammarIds.NAME_OF_CLASS :> "device"
      // emulate
      GrammarGroups.CLASS_FIELD_CREATION :>> "description" x StringType
      GrammarGroups.CLASS_FIELD_CREATION :>> "price" x FloatType
    }
  }

  def init(): Unit = {
    //    val model = getModel
    //    Register.addComponentModel(ComponentModel(model.name))
    //    val rest = new RestController
    //    val b = new BeanController
    //    val d = new DaoController
    //    new AdditionalClassController
    //    rest.createRest(model)
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

}


