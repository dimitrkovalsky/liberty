package com.liberty.traits.persistance

import com.liberty.entities.{JavaFunction, JavaClass}
import com.liberty.generators.DaoGenerator

/**
 * User: Dimitr
 * Date: 27.10.13
 * Time: 12:01
 */
trait CRUDable extends Insertable with Removable with Updatable with Findable {
    def createMethods(entity: JavaClass): List[JavaFunction] = {
        createInsert(entity) :: createFind(entity) :: createUpdate(entity) :: createDelete(entity) :: Nil
    }
}

trait Adaptable {
}

trait Insertable {
    def createInsert(entity: JavaClass): JavaFunction
}

trait Removable {
    def createDelete(entity: JavaClass): JavaFunction
}

trait Updatable {
    def createUpdate(entity: JavaClass): JavaFunction
}

trait Findable {
    def createFind(entity: JavaClass): JavaFunction
}

