import com.liberty.model.{ConnectionSettings, PersistenceXml}

import scala.xml.XML

object Runner {
  def main(args: Array[String]) {
    val xml = PersistenceXml("chapter04PU", ConnectionSettings("org.postgresql.Driver", "jdbc:postgresql://localhost:5432/my",
      "postgres", "Admin111"),  List("Account", "User")).eclipseLinkXml

   println(xml)
  }
}


