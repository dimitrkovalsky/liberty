package com.liberty.common

/**
 * Created by Dmytro_Kovalskyi on 24.09.2014.
 */
/**
 * Stores information about generated models
 */
object Register {
  val models = scala.collection.mutable.Map[String, Model]()
  /**
   * Is created WS class that has @ApplicationPath("rest") to change path for rest services
   */
  var wsCreated = false

  var beansXmlCreated = false

  def addModel(model: Model) {
    models += model.name -> model
  }

  def getModel(name: String) = {
    models.get(name)
  }

  def changeModel(model: Model): Unit = {
    models += model.name -> model
  }

}


/**
 * Uses to indicate that some component connected with this model were generated
 * @param name model name
 * @param daoExists  is ${model}Dao generated
 * @param beanExists  is ${model}Bean generated and I${model}Bean interface generated
 * @param restExists  is rest generated
 */
case class Model(name: String, daoExists: Boolean = false, beanExists: Boolean = false, restExists: Boolean = false)

