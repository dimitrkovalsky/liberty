package com.liberty.common

import com.liberty.model.JavaClass

/**
 * Created by Dmytro_Kovalskyi on 24.09.2014.
 */
/**
 * Stores information about generated models
 */
object Register {
  val componentModels = scala.collection.mutable.Map[String, ComponentModel]()
  val models = scala.collection.mutable.Map[String, JavaClass]()
  /**
   * Is created WS class that has @ApplicationPath("rest") to change path for rest services
   */
  var wsCreated = false

  var beansXmlCreated = false

  /**
   * Uses to indicate what structures was generated with with model
   * @param model
   */
  def addComponentModel(model: ComponentModel) {
    componentModels += model.name -> model
  }

  def addModel(clazz: JavaClass): Unit = {
    models += clazz.name -> clazz
  }

  /**
   * Model will be created if it is missing
   * @param clazz
   */
  def changeModel(clazz: JavaClass): Unit = {
    models += clazz.name -> clazz
  }

  def getModel(name: String) = {
    models.get(name)
  }

  def addComponentModel(clazz: JavaClass) {
    componentModels += clazz.name -> ComponentModel(clazz.name)
  }

  def getComponentModel(name: String) = {
    componentModels.get(name)
  }

  def changeComponentModel(model: ComponentModel): Unit = {
    componentModels += model.name -> model
  }

  /**
   * Map of common classes needed for work generated code such as ApplicationException
   */
  val commonClasses = scala.collection.mutable.Map[String, Boolean]()
}


/**
 * Uses to indicate that some component connected with this model were generated
 * @param name model name
 * @param daoExists  is ${model}Dao generated
 * @param beanExists  is ${model}Bean generated and I${model}Bean interface generated
 * @param restExists  is rest generated
 */
case class ComponentModel(name: String, daoExists: Boolean = false, beanExists: Boolean = false, restExists: Boolean = false)

