package com.liberty.generators.adapters

import com.liberty.builders.ClassBuilderTest
import com.liberty.common.ProjectConfig
import com.liberty.model.JavaClass
import com.liberty.traits.LocationPackage
import org.junit.{Assert, Test}

import scala.util.{Failure, Success}

/**
 * Created by Dmytro_Kovalskyi on 24.07.2014.
 */
class PostgresAdapterTest {
  private val basePackage = LocationPackage("com.city.guide")
  private val config = ProjectConfig.dbStandardPostgres
  @Test def createAccessors() {
    val initialClass = createPojo
    assertInitialClass(initialClass)
    val adapter = new PostgresAdapter(initialClass, basePackage)
    val accessible = adapter.getAccessible
   // println(accessible)
    val expected = "package com.guide.city.model;\n\nimport java.lang.Integer;\nimport java.lang.String;\n\nclass PojoClass {\n\tprivate Integer id = 0;\n\tprotected String name = \"\";\n\tprivate Integer age = 0;\n\tprivate String position = \"\";\n\n\tpublic Integer getId(){\n\t\treturn id;\n\t}\n\n\tpublic String getName(){\n\t\treturn name;\n\t}\n\n\tpublic Integer getAge(){\n\t\treturn age;\n\t}\n\n\tpublic String getPosition(){\n\t\treturn position;\n\t}\n\n\tpublic void setId(Integer id){\n\t\tthis.id = id;\n\t}\n\n\tpublic void setName(String name){\n\t\tthis.name = name;\n\t}\n\n\tpublic void setAge(Integer age){\n\t\tthis.age = age;\n\t}\n\n\tpublic void setPosition(String position){\n\t\tthis.position = position;\n\t}\n}"
    Assert.assertEquals(expected, accessible.toString)
    assertInitialClass(initialClass)

    // Test add accessors into initial class
    adapter.addAccessors()
  //  println(accessible)
    Assert.assertEquals(expected, initialClass.toString)
  }

  @Test def createAccessorsAndAnnotations() {
    val clazz = createPojo
    assertInitialClass(clazz)
    val adapter = new PostgresAdapter(clazz, basePackage)
    adapter.addAccessors()
    adapter.annotateClass()
    val expected = "package com.guide.city.model;\n\nimport java.lang.Integer;\nimport java.lang.String;\nimport javax.persistence.Entity;\nimport javax.persistence.Id;\n\n@Entity(value = \"pojoClass\")\nclass PojoClass {\n\t@Id\n\tprivate Integer id = 0;\n\tprotected String name = \"\";\n\tprivate Integer age = 0;\n\tprivate String position = \"\";\n\n\tpublic Integer getId(){\n\t\treturn id;\n\t}\n\n\tpublic String getName(){\n\t\treturn name;\n\t}\n\n\tpublic Integer getAge(){\n\t\treturn age;\n\t}\n\n\tpublic String getPosition(){\n\t\treturn position;\n\t}\n\n\tpublic void setId(Integer id){\n\t\tthis.id = id;\n\t}\n\n\tpublic void setName(String name){\n\t\tthis.name = name;\n\t}\n\n\tpublic void setAge(Integer age){\n\t\tthis.age = age;\n\t}\n\n\tpublic void setPosition(String position){\n\t\tthis.position = position;\n\t}\n}"
  //  println(clazz)
    Assert.assertEquals(expected, adapter.createEntity.toString)
  }


  private def assertInitialClass(initialClass: JavaClass) = {
    val expected = "package com.guide.city.model;\n\nimport java.lang.Integer;\nimport java.lang.String;\n\nclass PojoClass {\n\tprivate Integer id = 0;\n\tprotected String name = \"\";\n\tprivate Integer age = 0;\n\tprivate String position = \"\";\n}"
    val available = initialClass.toString
    //println(available)
    Assert.assertEquals(expected, available)
  }

  @Test def daoClassGeneration() {
    val entity = createPojo
    val adapter = new PostgresAdapter(entity, basePackage)
    adapter.addAccessors()
    adapter.annotateClass()

    adapter.createDao match {
      case Success(dao) =>
        val available = dao.toString
        val expected = "package com.city.guide.dao;\n\nimport com.city.guide.errors.DaoException;\nimport com.guide.city.model.PojoClass;\nimport java.lang.Exception;\nimport java.lang.Integer;\nimport java.util.List;\nimport javax.persistence.EntityManager;\nimport javax.persistence.criteria.CriteriaQuery;\n\nclass PojoClassDao implements IPojoClassDao {\n\tprivate EntityManager entityManager = null;\n\n\tpublic void PojoClassDao(EntityManager em){\n\t\tthis.entityManager = em;\n\t}\n\n\tpublic void insert(PojoClass entity) throws DaoException {\n\t\ttry {\n\t\t\tentityManager.getTransaction().begin();\n\t\t\tentityManager.persist(entity);\n\t\t\tentityManager.getTransaction().commit();\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic PojoClass find(PojoClass entity) throws DaoException {\n\t\ttry {\n\t\t\treturn entityManager.find(PojoClass.class, entity.getId());\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic List<PojoClass> findAll() throws DaoException {\n\t\ttry {\n\t\t\tCriteriaQuery<PojoClass> criteria = entityManager.getCriteriaBuilder().createQuery(PojoClass.class);\n\t\t\tcriteria.select(criteria.from(PojoClass.class));\n\t\t\treturn entityManager.createQuery(criteria).getResultList();\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic PojoClass findById(Integer id) throws DaoException {\n\t\ttry {\n\t\t\treturn entityManager.find(PojoClass.class, id);\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic void update(PojoClass entity) throws DaoException {\n\t\ttry {\n\t\t\tentityManager.getTransaction().begin();\n\t\t\tentityManager.merge(entity);\n\t\t\tentityManager.getTransaction().commit();\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic void delete(PojoClass entity) throws DaoException {\n\t\ttry {\n\t\t\tentityManager.getTransaction().begin();\n\t\t\tentityManager.remove(entity);\n\t\t\tentityManager.getTransaction().commit();\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n}"
     //   println(available)
        Assert.assertEquals(expected, available)
      case Failure(e) => Assert.fail(e.getMessage)
    }
  }

  @Test def interfaceCreation() {
    val entity = createPojo
    val adapter = new PostgresAdapter(entity, basePackage)
    adapter.addAccessors()
    adapter.annotateClass()

    adapter.createDaoInterface match {
      case Success(inter) =>
        val available = inter.toString
        val expected = "package com.city.guide.dao;\n\nimport java.util.List;\nimport java.lang.Integer;\nimport com.guide.city.model.PojoClass;\nimport com.city.guide.errors.DaoException;\n\ninterface IPojoClassDao {\n\n\tpublic void insert(PojoClass entity) throws DaoException;\n\n\tpublic PojoClass find(PojoClass entity) throws DaoException;\n\n\tpublic List<PojoClass> findAll() throws DaoException;\n\n\tpublic PojoClass findById(Integer id) throws DaoException;\n\n\tpublic void update(PojoClass entity) throws DaoException;\n\n\tpublic void delete(PojoClass entity) throws DaoException;\n}"
      //  println(available)
        Assert.assertEquals(expected, available)
      case Failure(e) => Assert.fail(e.getMessage)
    }
  }



  @Test def factoryClassGeneration() {
    val entity = createPojo
    val adapter = new PostgresAdapter(entity, basePackage)

    adapter.createDao
    val factoryCreator = adapter.getFactoryCreator
    val factory = factoryCreator.createDaoFactory(config.databaseConfig, List(adapter.getDaoCreationFunction.get)).get

    val expected = "package com.city.guide.common;\n\nimport com.city.guide.dao.IPojoClassDao;\nimport com.city.guide.dao.PojoClassDao;\nimport java.lang.String;\nimport javax.persistence.EntityManager;\nimport javax.persistence.Persistence;\n\nclass DaoFactory {\n\tprivate static String PERSISTENT_UNIT = \"PostgresUnit\";\n\tprivate static EntityManager em = null;\n\n\tstatic {\n\t\tem = Persistence.createEntityManagerFactory(PERSISTENT_UNIT).createEntityManager();\n\t}\n\n\tpublic static IPojoClassDao getPojoClassDao(){\n\t\treturn new PojoClassDao(em);\n\t}\n}"
    val available = factory.toString
  //  println(available)
    Assert.assertEquals(expected, available)
  }

  @Test def persistenceXmlGeneration() {
    val entity = createPojo
    val adapter = new PostgresAdapter(entity, basePackage)

    adapter.createDao
    val factoryCreator = adapter.getFactoryCreator
    val persist = factoryCreator.createWebInfFiles(config.databaseConfig)

    val expected = "<persistence xsi:schemaLocation=\"http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd\" version=\"2.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://xmlns.jcp.org/xml/ns/persistence\">\n      <persistence-unit name=\"PostgresUnit\" transaction-type=\"RESOURCE_LOCAL\">\n        <provider>\n          org.eclipse.persistence.jpa.PersistenceProvider\n        </provider><class>\n          PojoClass\n        </class><properties>\n        <property name=\"javax.persistence.jdbc.driver\" value=\"org.postgresql.Driver\"/>\n        <property name=\"javax.persistence.jdbc.url\" value=\"jdbc:postgresql://localhost:5432/liberty-database\"/>\n        <property name=\"javax.persistence.jdbc.user\" value=\"postgres\"/>\n        <property name=\"javax.persistence.jdbc.password\" value=\"Admin111\"/><property name=\"eclipselink.ddl-generation\" value=\"create-tables\"/><property name=\"eclipselink.create-ddl-jdbc-file-name\" value=\"createDDL_ddlGeneration.jdbc\"/><property name=\"eclipselink.ddl-generation.output-mode\" value=\"both\"/><property name=\"eclipselink.logging.level\" value=\"INFO\"/><property name=\"eclipselink.drop-ddl-jdbc-file-name\" value=\"dropDDL_ddlGeneration.jdbc\"/>\n      </properties>\n      </persistence-unit>\n    </persistence>"
    val available = persist.head.toString()
    //println(available)
    Assert.assertEquals(expected, available)
  }

  private def createPojo: JavaClass = new ClassBuilderTest().createPojoClass
}
