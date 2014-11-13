import java.net.Socket

import com.liberty.builders.ClassBuilder
import com.liberty.common.{ComponentModel, Register, ProjectConfig}
import com.liberty.controllers.{AdditionalClassController, DaoController, BeanController, RestController}
import com.liberty.model.{PrivateModifier, JavaField, JavaClass}
import com.liberty.transmission.TransmissionManager
import com.liberty.types.primitives.{LongType, StringType, IntegerType}

/**
 * User: dimitr
 * Date: 19.10.2014
 * Time: 13:01
 */
object Runner {
  def main(args: Array[String]) {
    //  TransmissionManager.startDataTransmission()
    val model = getModel
    Register.addComponentModel(ComponentModel(model.name))
    val rest = new RestController
    val b = new BeanController
    val d = new DaoController
    new AdditionalClassController
    rest.createRest(model)
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
