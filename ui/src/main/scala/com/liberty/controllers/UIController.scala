package com.liberty.controllers

import java.net.URL
import java.util.ResourceBundle
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.{control => jfxsc}
import javafx.{event => jfxe, fxml => jfxf}

import com.liberty.builders.ClassBuilder
import com.liberty.common._
import com.liberty.model.{PrivateModifier, JavaField, JavaClass}
import com.liberty.types.primitives.{StringType, IntegerType}

/**
 * User: Dmytro_Kovalskyi
 * Date: 06.11.2014
 * Time: 17:02
 */
class UIController extends jfxf.Initializable {

  @FXML private var dataLabel: Label = _

//  private val notificator = new UiNotifier(onAction)

  override def initialize(url: URL, resourceBundle: ResourceBundle): Unit = {
    dataLabel.setText("simple text")
    val m = getModel
    Register.addComponentModel(m)
    Controllers.restController.createRest(m)
  }

  def onAction(notification: UserNotificationAction): Unit = {
    println("[UIController] received : " + notification)
  }


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
}




