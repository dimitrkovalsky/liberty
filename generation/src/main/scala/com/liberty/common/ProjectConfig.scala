package com.liberty.common

import com.liberty.traits.LocationPackage

/**
 * Created by Dmytro_Kovalskyi on 07.07.2014.
 */
object ProjectConfig {
  var projectName = "project1"
  var path = "H:/System"
  var targetPath = s"$path/$projectName/"
  var serverPath = "H:\\Workspace\\wildfly-8.1.0.Final\\standalone\\deployments"

  def projectPath = s"D:/test/$projectName"

  var basePackageString = "com.test"
//  var basePackageString = "standard"

  def basePackage = LocationPackage(basePackageString)

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
