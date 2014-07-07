package com.liberty.controllers

import com.liberty.builders.ClassBuilderTest
import com.liberty.model.JavaClass
import org.junit.Test

/**
 * Created by Dmytro_Kovalskyi on 07.07.2014.
 */
class DaoControllerTest {
  private def createLocation: JavaClass = new ClassBuilderTest().createLocationClass

  private def createAccount: JavaClass = new ClassBuilderTest().createAccountClass

  @Test def createDao() {
    val controller = new DaoController
    controller.createDao(createAccount)
  }
}
