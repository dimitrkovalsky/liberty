package com.liberty.model

/**
 * Created by Dmytro_Kovalskyi on 25.07.2014.
 */
/**
 *
 * @param persistenceUnit
 * @param settings
 * @param provider
 * @param classes
 */
case class PersistenceXml(persistenceUnit: String, settings: ConnectionSettings, classes: List[String]) {
  private def createXml(provider: String, props: Map[String, String]) = {
    val xml = <persistence xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                           xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd" version="2.1">
      <persistence-unit name={persistenceUnit} transaction-type="RESOURCE_LOCAL">
        <provider>{provider}</provider>
        {for (c <- classes) yield
        <class>{c}</class>}
        <properties>
          <property name="javax.persistence.jdbc.driver" value={settings.driver}/>
          <property name="javax.persistence.jdbc.url" value={settings.url}/>
          <property name="javax.persistence.jdbc.user" value={settings.user}/>
          <property name="javax.persistence.jdbc.password" value={settings.password}/>
          {for (p <- props) yield
            <property name={p._1} value={p._2}/>}
        </properties>
      </persistence-unit>
    </persistence>
    xml
  }

  def eclipseLinkXml = {
    val props = Map(
      "eclipselink.ddl-generation" -> "create-tables",
      "eclipselink.create-ddl-jdbc-file-name" -> "createDDL_ddlGeneration.jdbc",
      "eclipselink.drop-ddl-jdbc-file-name" -> "dropDDL_ddlGeneration.jdbc",
      "eclipselink.ddl-generation.output-mode" -> "both",
      "eclipselink.logging.level" -> "INFO"
    )
    createXml("org.eclipse.persistence.jpa.PersistenceProvide", props)
  }
}

case class ConnectionSettings(driver: String, url: String, user: String, password: String)
