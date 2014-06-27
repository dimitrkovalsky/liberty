package com.liberty.traits.persistance

import com.liberty.model.JavaFunction

/**
 * User: Dimitr
 * Date: 27.10.13
 * Time: 12:01
 */
trait CRUDable extends Insertable with Removable with Updatable with Findable {
  def createMethods(): List[JavaFunction] = {
    (createInsert() :: createFind() :: createFindAll :: createFindById() :: createUpdate() :: createDelete() :: Nil).flatten
  }
}

trait Adaptable {
}

trait Insertable {
  def createInsert(): Option[JavaFunction]
}

trait Removable {
  def createDelete(): Option[JavaFunction]
}

trait Updatable {
  def createUpdate(): Option[JavaFunction]
}

trait Findable {
  def createFind(): Option[JavaFunction]

  def createFindAll(): Option[JavaFunction]

  def createFindById(): Option[JavaFunction]
}

