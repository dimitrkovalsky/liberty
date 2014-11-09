package com.liberty.common

import com.liberty.traits.LocationPackage

object ProjectConfig {

  //project configs
  var projectName = "project1"
  var path = "D:\\test"
//  var path = "C:\\Workspace"
  def projectPath = s"$path\\$projectName"
  var targetPath = s"$projectPath\\target"
  var basePackageString = "com.test"

  //server configs
  var serverPath = "H:\\Workspace\\wildfly-8.1.0.Final"
  var serverDeploymentPath = s"$serverPath\\standalone\\deployments"
  var serverStartPath = s"$serverPath\\bin\\standalone.bat"
  var browser = "chrome.exe"
  var startPage = "http://localhost:8080"
  
  def basePackage = LocationPackage(basePackageString)

  //database configs
  var dbStandardMongo = DBConfig(DatabaseType.MONGO_DB, NoSQLConfig("com.liberty-database", "localhost", 27017))
  var dbStandardPostgres = DBConfig(DatabaseType.POSTGRES_DB, RelationalConfig("org.postgresql.Driver",
    "jdbc:postgresql://localhost", 5432, "com.liberty-database", "postgres", "Admin111"))
    var defaultDatabase: DBConfig = dbStandardMongo

  /**
   * Uses to get list of managed entities for DAO
   */
  var entityClasses: Set[String] = Set.empty

  def addEntity(entity: String) {
    entityClasses = entityClasses + entity
  }

  def deleteEntity(entity: String) {
    entityClasses = entityClasses.filterNot(_ == entity)
  }
}
