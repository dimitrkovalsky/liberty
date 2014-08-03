package com.liberty.common

import com.liberty.common.DatabaseType.DatabaseType

/**
 * Created by Dmytro_Kovalskyi on 01.07.2014.
 */

/**
 * Represents database connection settings for different databases.
 * Should be matched to appropriated type in DaoGenerator
 */
trait DatabaseConfig

/**
 * Configuration for database connection
 * @param database
 * @param url
 * @param port
 */
case class NoSQLConfig(database: String, url: String, port: Int) extends DatabaseConfig

case class RelationalConfig(driver: String, url: String, port: Int, database: String, user: String, password: String) extends DatabaseConfig

object DatabaseType extends Enumeration {
  type DatabaseType = Value
  val MONGO_DB, POSTGRES_DB = Value
}

case class DBConfig(databaseType: DatabaseType, databaseConfig: DatabaseConfig)
