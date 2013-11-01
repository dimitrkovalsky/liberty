package com.liberty.generators.adapters

import com.liberty.traits.Accessible
import com.liberty.entities.JavaClass

/**
 * User: Dimitr
 * Date: 01.11.13
 * Time: 9:45
 */
class MongoAdapter(var javaClass: JavaClass) extends Accessible {
    def getAccessible: JavaClass = getAccessible(javaClass)

    def addAccessors() = super.addAccessors(javaClass)
}
