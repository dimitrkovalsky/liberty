package com.liberty.generators

import com.liberty.builders.ClassBuilderTest
import com.liberty.model.JavaClass
import org.junit.Test

/**
 * User: Dimitr
 * Date: 01.11.13
 * Time: 10:12
 */
class DaoGeneratorTest {
  private def createPojo: JavaClass = new ClassBuilderTest().createPojoClass
  private def createAccount: JavaClass = new ClassBuilderTest().createAccountClass


  @Test def generateTwoDao(){
    val pojo = createPojo
    val account = createAccount

    println(account)
  }
}
