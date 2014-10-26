package com.liberty.generators

import com.liberty.builders.ClassBuilderTest
import com.liberty.common.{DBConfig, DatabaseType, NoSQLConfig, ProjectConfig}
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
  private val postgresConfig = ProjectConfig.dbStandardPostgres
  private val basePackage = LocationPackage("com.guide.city")

  @Test def generateTwoMongoDao() {
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
        val expected1 = "package com.guide.city.dao;\n\nimport java.util.List;\nimport java.lang.Integer;\nimport com.guide.city.errors.DaoException;\nimport com.test.model.Account;\n\npublic interface IAccountDao {\n\n\tpublic void insert(Account entity) throws DaoException;\n\n\tpublic Account find(Account entity) throws DaoException;\n\n\tpublic List<Account> findAll() throws DaoException;\n\n\tpublic Account findByInternalId(Integer internalId) throws DaoException;\n\n\tpublic void update(Account entity) throws DaoException;\n\n\tpublic void delete(Account entity) throws DaoException;\n}"
        val expected2 = "package com.guide.city.dao;\n\nimport java.util.List;\nimport java.lang.Integer;\nimport com.guide.city.model.PojoClass;\nimport com.guide.city.errors.DaoException;\n\npublic interface IPojoClassDao {\n\n\tpublic void insert(PojoClass entity) throws DaoException;\n\n\tpublic PojoClass find(PojoClass entity) throws DaoException;\n\n\tpublic List<PojoClass> findAll() throws DaoException;\n\n\tpublic PojoClass findById(Integer id) throws DaoException;\n\n\tpublic void update(PojoClass entity) throws DaoException;\n\n\tpublic void delete(PojoClass entity) throws DaoException;\n}"
        //  println(available)
        assertTwo(available, expected1, expected2)
      case Failure(e) => Assert.fail(e.getMessage)
    }

    daos.foreach {
      case Success(dao) =>
        val available = dao.toString
        val expected1 = "package com.guide.city.dao;\n\nimport com.google.code.morphia.Datastore;\nimport com.google.code.morphia.dao.BasicDAO;\nimport com.guide.city.errors.DaoException;\nimport com.test.model.Account;\nimport java.lang.Exception;\nimport java.lang.Integer;\nimport java.util.List;\n\npublic class AccountDao extends BasicDAO<Account, Integer> implements IAccountDao {\n\tpublic AccountDao(Datastore datastore){\n\t\tsuper(datastore);\n\t}\n\n\tpublic void insert(Account entity) throws DaoException {\n\t\ttry {\n\t\t\tsuper.save(entity);\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic Account find(Account entity) throws DaoException {\n\t\ttry {\n\t\t\treturn super.findOne(\"_id\", entity.getInternalId());\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic List<Account> findAll() throws DaoException {\n\t\ttry {\n\t\t\treturn getCollection().find(Account.class).asList();\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic Account findByInternalId(Integer internalId) throws DaoException {\n\t\ttry {\n\t\t\treturn super.findOne(\"_id\", internalId);\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic void update(Account entity) throws DaoException {\n\t\ttry {\n\t\t\tsuper.save(entity);\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic void delete(Account entity) throws DaoException {\n\t\ttry {\n\t\t\tgetCollection().remove(new BasicDBObject().append(\"internalId\", entity.getInternalId()));\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n}"
        val expected2 = "package com.guide.city.dao;\n\nimport com.google.code.morphia.Datastore;\nimport com.google.code.morphia.dao.BasicDAO;\nimport com.guide.city.errors.DaoException;\nimport com.guide.city.model.PojoClass;\nimport java.lang.Exception;\nimport java.lang.Integer;\nimport java.util.List;\n\npublic class PojoClassDao extends BasicDAO<PojoClass, Integer> implements IPojoClassDao {\n\tpublic PojoClassDao(Datastore datastore){\n\t\tsuper(datastore);\n\t}\n\n\tpublic void insert(PojoClass entity) throws DaoException {\n\t\ttry {\n\t\t\tsuper.save(entity);\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic PojoClass find(PojoClass entity) throws DaoException {\n\t\ttry {\n\t\t\treturn super.findOne(\"_id\", entity.getId());\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic List<PojoClass> findAll() throws DaoException {\n\t\ttry {\n\t\t\treturn getCollection().find(PojoClass.class).asList();\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic PojoClass findById(Integer id) throws DaoException {\n\t\ttry {\n\t\t\treturn super.findOne(\"_id\", id);\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic void update(PojoClass entity) throws DaoException {\n\t\ttry {\n\t\t\tsuper.save(entity);\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic void delete(PojoClass entity) throws DaoException {\n\t\ttry {\n\t\t\tgetCollection().remove(new BasicDBObject().append(\"id\", entity.getId()));\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n}"
        // println(available)
        assertTwo(available, expected1, expected2)
      case Failure(e) => Assert.fail(e.getMessage)
    }

    factory match {
      case Some(f) => val expected = "package com.guide.city.common;\n\nimport com.google.code.morphia.Datastore;\nimport com.google.code.morphia.Morphia;\nimport com.guide.city.dao.AccountDao;\nimport com.guide.city.dao.IAccountDao;\nimport com.guide.city.dao.IPojoClassDao;\nimport com.guide.city.dao.PojoClassDao;\nimport com.mongodb.Mongo;\nimport java.lang.Integer;\nimport java.lang.String;\nimport java.net.UnknownHostException;\n\npublic class DaoFactory {\n\tprivate static String DATABASE_URL = \"127.0.0.1\";\n\tprivate static Integer DATABASE_PORT = 27017;\n\tprivate static String DATABASE_NAME = \"guidedb\";\n\tprivate static Datastore datastore;\n\n\tstatic {\n\t\ttry {\n\t\t\tMongo mongo = new Mongo(DATABASE_URL, DATABASE_PORT);\n\t\t\tMorphia morphia = new Morphia();\n\t\t\tdatastore = morphia.createDatastore(mongo, DATABASE_NAME);\n\t\t} catch(UnknownHostException e){\n\t\t\tSystem.err.println(e.getMessage());\n\t\t}\n\t}\n\n\tpublic static IAccountDao getAccountDao(){\n\t\treturn new AccountDao(datastore);\n\t}\n\n\tpublic static IPojoClassDao getPojoClassDao(){\n\t\treturn new PojoClassDao(datastore);\n\t}\n}"
        Assert.assertEquals(expected, f.toString)
      case _ => Assert.fail("Factory was not created")
    }
    Assert.assertEquals("WEB-INF files should not be created", Nil, generator.createMetaInfFiles)
  }

  @Test def generateTwoPostgresDao() {
    val pojo = createPojo
    val account = createAccount
    val generator = new DaoGenerator(postgresConfig, basePackage)
    generator.addModel(pojo)
    generator.addModel(account)
    val entities: List[JavaClass] = generator.createEntities
    val interfaces = generator.createInterfaces
    val daos = generator.createDaos
    val factory = generator.createFactory

    val expected1 = "package com.test.model;\n\nimport java.lang.Integer;\nimport java.lang.String;\nimport java.util.Date;\nimport javax.persistence.Entity;\nimport javax.persistence.GeneratedValue;\nimport javax.persistence.Id;\n\n@Entity(name = \"account\")\npublic class Account {\n\t@Id\n\t@GeneratedValue\n\tprivate Integer internalId;\n\tprivate String androidId;\n\tprivate Date created;\n\n\tpublic Integer getInternalId(){\n\t\treturn internalId;\n\t}\n\n\tpublic String getAndroidId(){\n\t\treturn androidId;\n\t}\n\n\tpublic Date getCreated(){\n\t\treturn created;\n\t}\n\n\tpublic void setInternalId(Integer internalId){\n\t\tthis.internalId = internalId;\n\t}\n\n\tpublic void setAndroidId(String androidId){\n\t\tthis.androidId = androidId;\n\t}\n\n\tpublic void setCreated(Date created){\n\t\tthis.created = created;\n\t}\n}"
    val expected2 = "package com.guide.city.model;\n\nimport java.lang.Integer;\nimport java.lang.String;\nimport javax.persistence.Entity;\nimport javax.persistence.GeneratedValue;\nimport javax.persistence.Id;\n\n@Entity(name = \"pojoClass\")\npublic class PojoClass {\n\t@Id\n\t@GeneratedValue\n\tprivate Integer id;\n\tprotected String name;\n\tprivate Integer age;\n\tprivate String position;\n\n\tpublic Integer getId(){\n\t\treturn id;\n\t}\n\n\tpublic String getName(){\n\t\treturn name;\n\t}\n\n\tpublic Integer getAge(){\n\t\treturn age;\n\t}\n\n\tpublic String getPosition(){\n\t\treturn position;\n\t}\n\n\tpublic void setId(Integer id){\n\t\tthis.id = id;\n\t}\n\n\tpublic void setName(String name){\n\t\tthis.name = name;\n\t}\n\n\tpublic void setAge(Integer age){\n\t\tthis.age = age;\n\t}\n\n\tpublic void setPosition(String position){\n\t\tthis.position = position;\n\t}\n}"
    entities.foreach(e => assertTwo(e.toString, expected1, expected2))

    interfaces.foreach {
      case Success(interface) =>
        val available = interface.toString
        val expected1 = "package com.guide.city.dao;\n\nimport java.util.List;\nimport java.lang.Integer;\nimport com.guide.city.errors.DaoException;\nimport com.test.model.Account;\n\npublic interface IAccountDao {\n\n\tpublic void insert(Account entity) throws DaoException;\n\n\tpublic Account find(Account entity) throws DaoException;\n\n\tpublic List<Account> findAll() throws DaoException;\n\n\tpublic Account findByInternalId(Integer internalId) throws DaoException;\n\n\tpublic void update(Account entity) throws DaoException;\n\n\tpublic void delete(Account entity) throws DaoException;\n}"
        val expected2 = "package com.guide.city.dao;\n\nimport java.util.List;\nimport java.lang.Integer;\nimport com.guide.city.model.PojoClass;\nimport com.guide.city.errors.DaoException;\n\npublic interface IPojoClassDao {\n\n\tpublic void insert(PojoClass entity) throws DaoException;\n\n\tpublic PojoClass find(PojoClass entity) throws DaoException;\n\n\tpublic List<PojoClass> findAll() throws DaoException;\n\n\tpublic PojoClass findById(Integer id) throws DaoException;\n\n\tpublic void update(PojoClass entity) throws DaoException;\n\n\tpublic void delete(PojoClass entity) throws DaoException;\n}"
        //println(available)
        assertTwo(available, expected1, expected2)
      case Failure(e) => Assert.fail(e.getMessage)
    }
    daos.foreach {
      case Success(dao) =>
        val available = dao.toString
        val expected1 = "package com.guide.city.dao;\n\nimport com.guide.city.errors.DaoException;\nimport com.test.model.Account;\nimport java.lang.Exception;\nimport java.lang.Integer;\nimport java.util.List;\nimport javax.persistence.EntityManager;\nimport javax.persistence.criteria.CriteriaQuery;\n\npublic class AccountDao implements IAccountDao {\n\tprivate EntityManager entityManager;\n\n\tpublic AccountDao(EntityManager em){\n\t\tthis.entityManager = em;\n\t}\n\n\tpublic void insert(Account entity) throws DaoException {\n\t\ttry {\n\t\t\tentityManager.getTransaction().begin();\n\t\t\tentityManager.persist(entity);\n\t\t\tentityManager.getTransaction().commit();\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic Account find(Account entity) throws DaoException {\n\t\ttry {\n\t\t\treturn entityManager.find(Account.class, entity.getInternalId());\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic List<Account> findAll() throws DaoException {\n\t\ttry {\n\t\t\tCriteriaQuery<Account> criteria = entityManager.getCriteriaBuilder().createQuery(Account.class);\n\t\t\tcriteria.select(criteria.from(Account.class));\n\t\t\treturn entityManager.createQuery(criteria).getResultList();\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic Account findByInternalId(Integer internalId) throws DaoException {\n\t\ttry {\n\t\t\treturn entityManager.find(Account.class, internalId);\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic void update(Account entity) throws DaoException {\n\t\ttry {\n\t\t\tentityManager.getTransaction().begin();\n\t\t\tentityManager.merge(entity);\n\t\t\tentityManager.getTransaction().commit();\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic void delete(Account entity) throws DaoException {\n\t\ttry {\n\t\t\tentityManager.getTransaction().begin();\n\t\t\tentityManager.remove(entity);\n\t\t\tentityManager.getTransaction().commit();\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n}"
        val expected2 = "package com.guide.city.dao;\n\nimport com.guide.city.errors.DaoException;\nimport com.guide.city.model.PojoClass;\nimport java.lang.Exception;\nimport java.lang.Integer;\nimport java.util.List;\nimport javax.persistence.EntityManager;\nimport javax.persistence.criteria.CriteriaQuery;\n\npublic class PojoClassDao implements IPojoClassDao {\n\tprivate EntityManager entityManager;\n\n\tpublic PojoClassDao(EntityManager em){\n\t\tthis.entityManager = em;\n\t}\n\n\tpublic void insert(PojoClass entity) throws DaoException {\n\t\ttry {\n\t\t\tentityManager.getTransaction().begin();\n\t\t\tentityManager.persist(entity);\n\t\t\tentityManager.getTransaction().commit();\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic PojoClass find(PojoClass entity) throws DaoException {\n\t\ttry {\n\t\t\treturn entityManager.find(PojoClass.class, entity.getId());\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic List<PojoClass> findAll() throws DaoException {\n\t\ttry {\n\t\t\tCriteriaQuery<PojoClass> criteria = entityManager.getCriteriaBuilder().createQuery(PojoClass.class);\n\t\t\tcriteria.select(criteria.from(PojoClass.class));\n\t\t\treturn entityManager.createQuery(criteria).getResultList();\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic PojoClass findById(Integer id) throws DaoException {\n\t\ttry {\n\t\t\treturn entityManager.find(PojoClass.class, id);\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic void update(PojoClass entity) throws DaoException {\n\t\ttry {\n\t\t\tentityManager.getTransaction().begin();\n\t\t\tentityManager.merge(entity);\n\t\t\tentityManager.getTransaction().commit();\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic void delete(PojoClass entity) throws DaoException {\n\t\ttry {\n\t\t\tentityManager.getTransaction().begin();\n\t\t\tentityManager.remove(entity);\n\t\t\tentityManager.getTransaction().commit();\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n}"
        // println(available)
        assertTwo(available, expected1, expected2)
      case Failure(e) => Assert.fail(e.getMessage)
    }

    factory match {
      case Some(f) => val expected = "package com.guide.city.common;\n\nimport com.guide.city.dao.AccountDao;\nimport com.guide.city.dao.IAccountDao;\nimport com.guide.city.dao.IPojoClassDao;\nimport com.guide.city.dao.PojoClassDao;\nimport java.lang.String;\nimport javax.persistence.EntityManager;\nimport javax.persistence.Persistence;\n\npublic class DaoFactory {\n\tprivate static String PERSISTENT_UNIT = \"PostgresUnit\";\n\tprivate static EntityManager em;\n\n\tstatic {\n\t\tem = Persistence.createEntityManagerFactory(PERSISTENT_UNIT).createEntityManager();\n\t}\n\n\tpublic static IAccountDao getAccountDao(){\n\t\treturn new AccountDao(em);\n\t}\n\n\tpublic static IPojoClassDao getPojoClassDao(){\n\t\treturn new PojoClassDao(em);\n\t}\n}"
        // println(f.toString)
        Assert.assertEquals(expected, f.toString)
      case _ => Assert.fail("Factory was not created")
    }

    //  println(generator.createWebInfFiles)
    generator.createMetaInfFiles match {
      case x :: Nil => val expected = "<persistence xsi:schemaLocation=\"http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd\" version=\"2.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://xmlns.jcp.org/xml/ns/persistence\">\n      <persistence-unit name=\"PostgresUnit\" transaction-type=\"RESOURCE_LOCAL\">\n        <provider>\n          org.eclipse.persistence.jpa.PersistenceProvider\n        </provider><class>\n          PojoClass\n        </class><class>\n          Account\n        </class><properties>\n        <property name=\"javax.persistence.jdbc.driver\" value=\"org.postgresql.Driver\"/>\n        <property name=\"javax.persistence.jdbc.url\" value=\"jdbc:postgresql://localhost:5432/com.liberty-database\"/>\n        <property name=\"javax.persistence.jdbc.user\" value=\"postgres\"/>\n        <property name=\"javax.persistence.jdbc.password\" value=\"Admin111\"/><property name=\"eclipselink.ddl-generation\" value=\"create-tables\"/><property name=\"eclipselink.create-ddl-jdbc-file-name\" value=\"createDDL_ddlGeneration.jdbc\"/><property name=\"eclipselink.ddl-generation.output-mode\" value=\"both\"/><property name=\"eclipselink.logging.level\" value=\"INFO\"/><property name=\"eclipselink.drop-ddl-jdbc-file-name\" value=\"dropDDL_ddlGeneration.jdbc\"/>\n      </properties>\n      </persistence-unit>\n    </persistence>"
        Assert.assertEquals(expected, x.xml.toString())
      case _ => Assert.fail("Created unexpected WEB-INF files")
    }
  }

  def assertTwo(available: String, exp1: String, exp2: String) {
    if (available.equals(exp1) || available.equals(exp2))
      return
    if (available.equals(exp1))
      Assert.assertEquals(exp1, available)
    else
      Assert.assertEquals(exp2, available)
  }
}
