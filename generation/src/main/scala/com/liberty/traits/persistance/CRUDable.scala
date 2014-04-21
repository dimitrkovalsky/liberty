package com.liberty.traits.persistance

import com.liberty.model.{JavaFunction, JavaClass}
import com.liberty.generators.DaoGenerator

/**
 * User: Dimitr
 * Date: 27.10.13
 * Time: 12:01
 */
trait CRUDable extends Insertable with Removable with Updatable with Findable {
    def createMethods(): List[JavaFunction] = {
        createInsert() :: createFind() :: createUpdate() :: createDelete() :: Nil
    }
}

trait Adaptable {
}

trait Insertable {
    def createInsert(): JavaFunction
}

trait Removable {
    def createDelete(): JavaFunction
}

trait Updatable {
    def createUpdate(): JavaFunction
}

trait Findable {
    def createFind(): JavaFunction
}

