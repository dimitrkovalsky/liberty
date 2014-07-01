package com.liberty.generators

import com.liberty.generators.adapters.StubAdapter
import com.liberty.model.JavaClass
import com.liberty.traits.persistance.DaoAdapter

/**
 * User: Dimitr
 * Date: 01.11.13
 * Time: 9:44
 */
/**
 * Requires to set adapter
 * @param entity
 * @param adapter
 */
class DaoGenerator(var entity: JavaClass, var adapter: DaoAdapter = new StubAdapter) {
}
