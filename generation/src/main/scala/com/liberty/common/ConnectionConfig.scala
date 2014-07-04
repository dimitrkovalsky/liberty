package com.liberty.common

import com.liberty.common.DatabaseType.DatabaseType

/**
 * Created by Dmytro_Kovalskyi on 01.07.2014.
 */
/**
 * Configuration for database connection
 * @param database
 * @param url
 * @param port
 */
case class ConnectionConfig(database: String, url: String, port: Int)

object DatabaseType extends Enumeration {
  type DatabaseType = Value
  val MONGO_DB, POSTGRESQL_DB = Value
}

case class DBConfig(databaseType: DatabaseType, connectionConfig: ConnectionConfig)
