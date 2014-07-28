package com.liberty.generators

import com.liberty.builders.ClassBuilderTest
import com.liberty.common.{DBConfig, DatabaseType, NoSQLConfig}
import com.liberty.model.JavaClass
import com.liberty.traits.LocationPackage
import org.junit.{Assert, Test}

import scala.util.{Failure, Success}

/**
 * User: Dimitr
 * Date: 01.11.13
 * Time: 10:12
 */
class DaoGeneratorTest {
  private def createPojo: JavaClass = new ClassBuilderTest().createPojoClass

  private def createAccount: JavaClass = new ClassBuilderTest().createAccountClass

  private val config = NoSQLConfig("guidedb", "127.0.0.1", 27017)
  private val mongoConfig = DBConfig(DatabaseType.MONGO_DB, config)
  private val basePackage = LocationPackage("com.guide.city")

  @Test def generateTwoDao() {
    val pojo = createPojo
    val account = createAccount
    val generator = new DaoGenerator(mongoConfig, basePackage)
    generator.addModel(pojo)
    generator.addModel(account)
    val entities: List[JavaClass] = generator.createEntities
    val interfaces = generator.createInterfaces
    val daos = generator.createDaos
    val factory = generator.createFactory

    // entities.foreach(e => println(e.toString))
    interfaces.foreach {
      case Success(interface) =>
        val available = interface.toString
        val expected1 = "package com.guide.city.dao;\n\nimport java.util.List;\nimport java.lang.Integer;\nimport com.guide.city.errors.DaoException;\nimport com.test.model.Account;\n\ninterface IAccountDao {\n\n\tpublic void insert(Account entity) throws DaoException;\n\n\tpublic Account find(Account entity) throws DaoException;\n\n\tpublic List<Account> findAll() throws DaoException;\n\n\tpublic Account findByInternalId(Integer internalId) throws DaoException;\n\n\tpublic void update(Account entity) throws DaoException;\n\n\tpublic void delete(Account entity) throws DaoException;\n}"
        val expected2 = "package com.guide.city.dao;\n\nimport java.util.List;\nimport java.lang.Integer;\nimport com.guide.city.model.PojoClass;\nimport com.guide.city.errors.DaoException;\n\ninterface IPojoClassDao {\n\n\tpublic void insert(PojoClass entity) throws DaoException;\n\n\tpublic PojoClass find(PojoClass entity) throws DaoException;\n\n\tpublic List<PojoClass> findAll() throws DaoException;\n\n\tpublic PojoClass findById(Integer id) throws DaoException;\n\n\tpublic void update(PojoClass entity) throws DaoException;\n\n\tpublic void delete(PojoClass entity) throws DaoException;\n}"
        //  println(available)
        Assert.assertTrue(available.equals(expected1) || available.equals(expected2))
      case Failure(e) => Assert.fail(e.getMessage)
    }

    daos.foreach {
      case Success(dao) =>
        val available = dao.toString
        val expected1 = "package com.guide.city.dao;\n\nimport com.google.code.morphia.Datastore;\nimport com.google.code.morphia.dao.BasicDAO;\nimport com.guide.city.errors.DaoException;\nimport com.test.model.Account;\nimport java.lang.Exception;\nimport java.lang.Integer;\nimport java.util.List;\n\nclass AccountDao extends BasicDAO<Account, Integer> implements IAccountDao {\n\tpublic void AccountDao(Datastore datastore){\n\t\tsuper(datastore);\n\t}\n\n\tpublic void insert(Account entity) throws DaoException {\n\t\ttry {\n\t\t\tsuper.save(entity);\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic Account find(Account entity) throws DaoException {\n\t\ttry {\n\t\t\treturn super.findOne(\"_id\", entity.getInternalId());\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic List<Account> findAll() throws DaoException {\n\t\ttry {\n\t\t\treturn getCollection().find(Account.class).asList();\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic Account findByInternalId(Integer internalId) throws DaoException {\n\t\ttry {\n\t\t\treturn super.findOne(\"_id\", internalId);\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic void update(Account entity) throws DaoException {\n\t\ttry {\n\t\t\tsuper.save(entity);\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic void delete(Account entity) throws DaoException {\n\t\ttry {\n\t\t\tgetCollection().remove(new BasicDBObject().append(\"internalId\", entity.getInternalId()));\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n}"
        val expected2 = "package com.guide.city.dao;\n\nimport com.google.code.morphia.Datastore;\nimport com.google.code.morphia.dao.BasicDAO;\nimport com.guide.city.errors.DaoException;\nimport com.guide.city.model.PojoClass;\nimport java.lang.Exception;\nimport java.lang.Integer;\nimport java.util.List;\n\nclass PojoClassDao extends BasicDAO<PojoClass, Integer> implements IPojoClassDao {\n\tpublic void PojoClassDao(Datastore datastore){\n\t\tsuper(datastore);\n\t}\n\n\tpublic void insert(PojoClass entity) throws DaoException {\n\t\ttry {\n\t\t\tsuper.save(entity);\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic PojoClass find(PojoClass entity) throws DaoException {\n\t\ttry {\n\t\t\treturn super.findOne(\"_id\", entity.getId());\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic List<PojoClass> findAll() throws DaoException {\n\t\ttry {\n\t\t\treturn getCollection().find(PojoClass.class).asList();\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic PojoClass findById(Integer id) throws DaoException {\n\t\ttry {\n\t\t\treturn super.findOne(\"_id\", id);\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic void update(PojoClass entity) throws DaoException {\n\t\ttry {\n\t\t\tsuper.save(entity);\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic void delete(PojoClass entity) throws DaoException {\n\t\ttry {\n\t\t\tgetCollection().remove(new BasicDBObject().append(\"id\", entity.getId()));\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n}"
        // println(available)
        Assert.assertTrue(available.equals(expected1) || available.equals(expected2))
      case Failure(e) => Assert.fail(e.getMessage)
    }

    factory match {
      case Some(f) => println(f.toString)
      case _ => println("Error")
    }
  }
}
