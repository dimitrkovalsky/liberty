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

  var dbStandardMongo = DBConfig(DatabaseType.MONGO_DB, ConnectionConfig("liberty-database", "localhost", 27017))
}
