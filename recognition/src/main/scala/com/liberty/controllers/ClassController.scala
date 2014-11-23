package com.liberty.controllers

import com.liberty.builders.ClassBuilder
import com.liberty.common._
import com.liberty.entities.ComplexGrammar
import com.liberty.helpers.SynthesizeHelper
import com.liberty.model.{PrivateModifier, JavaField}
import com.liberty.common.Implicits._
import com.liberty.traits.Writer
import com.liberty.transmission.TransmissionManager
import com.liberty.writers.FileClassWriter

/**
 * Created by Maxxis on 11/8/2014.
 */
class ClassController extends Subscriber {
  var classBuilder: Option[ClassBuilder] = None
  protected val writer: Writer = new FileClassWriter(ProjectConfig.projectPath)

  private def createFieldInner(cg: ComplexGrammar): Boolean = {
    classBuilder.map { b =>
      val tp = TypeMapper.getStandardType(cg.grammars(1).command)
      //TODO: Convert name to camel case
      val name = cg.grammars(2).command.removeSpaces()
      tp match {
        case Some(t) =>
          b.addField(JavaField(name, t, PrivateModifier))
          true
        case _ => System.err.println("[ClassController] Creation field is failed")
          false
      }
    } getOrElse {
      println("[ClassController] ClassBuilder is None")
      false
    }
  }

  def createField(cg: ComplexGrammar) {
    if (createFieldInner(cg))
      notifyClassChanged()
    else
      println("Creation field is failed")

  }

  def startClassCreation() {
    println("Enter name")
    SynthesizeHelper.synthesize("Call class name")
    GrammarController.changeGrammarGroup(GrammarGroups.CLASS_NAMES)
    //TODO: Send UI notification for open dialog
    classBuilder = Some(new ClassBuilder)
  }

  def createClass(className: String): Boolean = {
    classBuilder.foreach { b =>
      val name = className.firstToUpperCase
      b.setName(name)
      b.addPackage(ProjectConfig.basePackage.nested("models").nestedClass(name))
      notifyClassChanged()
    }
    GrammarController.changeGrammarGroup(GrammarGroups.CLASS_EDITING)
    SynthesizeHelper.synthesize(s"Add fields for $className class")
    true
  }

  def notifyClassChanged(): Unit = {
    classBuilder.foreach { b =>
      val clazz = b.getJavaClass
      println("Changed class : " + clazz)
      Register.changeModel(clazz)
      notify(Topics.USER_NOTIFICATION, ClassEditAction(clazz))
      notify(Topics.MODEL_ACTIVATION, ActivateModel(clazz.name))
    }
  }

  def completeCreation(): Unit = {
    classBuilder.foreach { b =>
      val clazz = b.getJavaClass
      ActionBus.publish(Topics.MODEL_ACTIVATION, ActivateModel(clazz.name))
      SynthesizeHelper.synthesize(clazz.name + " class created")
      println("[CONTROLLER WRITE ] : " + clazz)
      writer.write(clazz)
    }
    notifyClassChanged()
    TransmissionManager.activateGrammar(GrammarGroups.COMPONENT_CREATION)

  }

  override protected def onActionReceived: Received = {
    case _ => Right("ok")
  }

  override protected def getSubscriptionTopics: List[String] = Nil
}
