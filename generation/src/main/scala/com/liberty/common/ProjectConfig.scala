package com.liberty.common

import com.liberty.traits.LocationPackage

/**
 * Created by Dmytro_Kovalskyi on 07.07.2014.
 */
object ProjectConfig {
  var projectName = "project1"

  def projectPath = s"D:/test/$projectName"

  var basePackageString = "com.test"

  def basePackage = LocationPackage(basePackageString)

  var dbStandardMongo = DBConfig(DatabaseType.MONGO_DB, NoSQLConfig("liberty-database", "localhost", 27017))
  var dbStandardPostgres = DBConfig(DatabaseType.POSTGRESQL_DB, RelationalConfig("org.postgresql.Driver",
    "jdbc:postgresql://localhost", 5432, "liberty-database", "postgres", "Admin111"))

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
