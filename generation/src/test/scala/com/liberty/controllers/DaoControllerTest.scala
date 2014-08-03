package com.liberty.controllers

import com.liberty.builders.ClassBuilderTest
import com.liberty.model.{JavaClass, JavaField, PrivateModifier}
import com.liberty.types.primitives.IntegerType
import org.junit.{BeforeClass, Before, Test}
import java.io.File
import com.liberty.utils.FileChecker
import com.liberty.common.ProjectConfig
import java.nio.file.{Path, Files}
import java.nio.charset.Charset

/**
 * Created by Dmytro_Kovalskyi on 07.07.2014.
 */
class DaoControllerTest {
  private val STANDARDS_FOLDER = FileChecker.PROJECT_PATH + "generation\\standards\\"
  private val TEST_MONGO_CREATE_FOLDER = STANDARDS_FOLDER + "controllers\\mongo\\testCreate\\project1"
  private val TEST_POSTGRES_CREATE_FOLDER = STANDARDS_FOLDER + "controllers\\postgres\\testCreate\\project1"
  private val TEST_MONGO_UPDATE_FOLDER = STANDARDS_FOLDER + "controllers\\mongo\\testUpdate\\project1"
  private val TEST_POSTGRES_UPDATE_FOLDER = STANDARDS_FOLDER + "controllers\\postgres\\testUpdate\\project1"

  private def createLocation: JavaClass = new ClassBuilderTest().createLocationClass

  private def createAccount: JavaClass = new ClassBuilderTest().createAccountClass

  @Test def createMongoDao() {
    ProjectConfig.defaultDatabase = ProjectConfig.dbStandardMongo
    val controller = new DaoController
    controller.createDao(createAccount)
    FileChecker.checkDirectories(TEST_MONGO_CREATE_FOLDER, ProjectConfig.projectPath)
    FileChecker.removeDirectory(ProjectConfig.projectPath)
  }


  @Test def updateMongoDao() {
    ProjectConfig.defaultDatabase = ProjectConfig.dbStandardMongo
    val controller = new DaoController
    val account = createAccount
    controller.createDao(createAccount)
    controller.createDao(createLocation)
    account.addField(JavaField("status", IntegerType, PrivateModifier))
    controller.changed(account)
    FileChecker.checkDirectoriesFail(TEST_MONGO_CREATE_FOLDER, ProjectConfig.projectPath)
    FileChecker.checkDirectories(TEST_MONGO_UPDATE_FOLDER, ProjectConfig.projectPath)
    FileChecker.removeDirectory(ProjectConfig.projectPath)
  }

  @Test def createPostgresDao() {
    ProjectConfig.defaultDatabase = ProjectConfig.dbStandardPostgres
    val controller = new DaoController
    controller.createDao(createAccount)
    FileChecker.checkDirectories(TEST_POSTGRES_CREATE_FOLDER, ProjectConfig.projectPath)
    FileChecker.checkDirectoriesFail(TEST_MONGO_CREATE_FOLDER, ProjectConfig.projectPath)
    FileChecker.removeDirectory(ProjectConfig.projectPath)
  }

  @Test def updatePostgresDao() {
    ProjectConfig.defaultDatabase = ProjectConfig.dbStandardPostgres
    val controller = new DaoController
    val account = createAccount
    controller.createDao(createAccount)
    controller.createDao(createLocation)
    account.addField(JavaField("status", IntegerType, PrivateModifier))
    controller.changed(account)
    FileChecker.checkDirectoriesFail(TEST_POSTGRES_CREATE_FOLDER, ProjectConfig.projectPath)
    FileChecker.checkDirectories(TEST_POSTGRES_UPDATE_FOLDER, ProjectConfig.projectPath)
    FileChecker.removeDirectory(ProjectConfig.projectPath)
  }
}

object DaoControllerTest{
  @BeforeClass private def clean(){
    FileChecker.removeDirectory(ProjectConfig.projectPath)
  }

}
