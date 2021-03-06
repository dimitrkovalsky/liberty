package com.liberty.generators.adapters

import com.liberty.builders.ClassBuilderTest
import com.liberty.common.NoSQLConfig
import com.liberty.model.JavaClass
import com.liberty.traits.LocationPackage
import org.junit.{Assert, Test}

import scala.util.{Failure, Success}

/**
 * User: Dimitr
 * Date: 01.11.13
 * Time: 10:12
 */
class MongoAdapterTest {
  private val basePackage = LocationPackage("com.city.guide")

  @Test def createAccessors() {
    val initialClass = createPojo
    assertInitialClass(initialClass)
    val adapter = new MongoAdapter(initialClass, basePackage)
    val accessible = adapter.getAccessible
    //println(accessible)
    val expected = "package com.guide.city.model;\n\nimport java.lang.Integer;\nimport java.lang.String;\n\npublic class PojoClass {\n\tprivate Integer id;\n\tprotected String name;\n\tprivate Integer age;\n\tprivate String position;\n\n\tpublic Integer getId() {\n\t\treturn id;\n\t}\n\n\tpublic String getName() {\n\t\treturn name;\n\t}\n\n\tpublic Integer getAge() {\n\t\treturn age;\n\t}\n\n\tpublic String getPosition() {\n\t\treturn position;\n\t}\n\n\tpublic void setId(Integer id) {\n\t\tthis.id = id;\n\t}\n\n\tpublic void setName(String name) {\n\t\tthis.name = name;\n\t}\n\n\tpublic void setAge(Integer age) {\n\t\tthis.age = age;\n\t}\n\n\tpublic void setPosition(String position) {\n\t\tthis.position = position;\n\t}\n}"
    Assert.assertEquals(expected, accessible.toString)
    assertInitialClass(initialClass)

    // Test add accessors into initial class
    adapter.addAccessors()
    //  println(initialClass)
    Assert.assertEquals(expected, initialClass.toString)
  }

  @Test def createAccessorsAndAnnotations() {
    val clazz = createPojo
    assertInitialClass(clazz)
    val adapter = new MongoAdapter(clazz, basePackage)
    adapter.addAccessors()
    adapter.annotateClass()
    val expected = "package com.guide.city.model;\n\nimport com.google.code.morphia.annotations.Entity;\nimport com.google.code.morphia.annotations.Id;\nimport java.lang.Integer;\nimport java.lang.String;\n\n@Entity(value = \"pojoClass\", noClassnameStored = true)\npublic class PojoClass {\n\t@Id\n\tprivate Integer id;\n\tprotected String name;\n\tprivate Integer age;\n\tprivate String position;\n\n\tpublic Integer getId() {\n\t\treturn id;\n\t}\n\n\tpublic String getName() {\n\t\treturn name;\n\t}\n\n\tpublic Integer getAge() {\n\t\treturn age;\n\t}\n\n\tpublic String getPosition() {\n\t\treturn position;\n\t}\n\n\tpublic void setId(Integer id) {\n\t\tthis.id = id;\n\t}\n\n\tpublic void setName(String name) {\n\t\tthis.name = name;\n\t}\n\n\tpublic void setAge(Integer age) {\n\t\tthis.age = age;\n\t}\n\n\tpublic void setPosition(String position) {\n\t\tthis.position = position;\n\t}\n}"
    //println(clazz)
    Assert.assertEquals(expected, adapter.createEntity.toString)
  }


  private def assertInitialClass(initialClass: JavaClass) = {
    val expected = "package com.guide.city.model;\n\nimport java.lang.Integer;\nimport java.lang.String;\n\npublic class PojoClass {\n\tprivate Integer id;\n\tprotected String name;\n\tprivate Integer age;\n\tprivate String position;\n}"
    val available = initialClass.toString
    //println(available)
    Assert.assertEquals(expected, available)
  }

  private def createPojo: JavaClass = new ClassBuilderTest().createPojoClass

  @Test def daoClassGeneration() {
    val entity = createPojo
    val adapter = new MongoAdapter(entity, basePackage)
    adapter.addAccessors()
    adapter.annotateClass()

    adapter.createDao match {
      case Success(dao) =>
        val available = dao.toString
        val expected = "package com.city.guide.dao;\n\nimport com.city.guide.errors.DaoException;\nimport com.google.code.morphia.Datastore;\nimport com.google.code.morphia.dao.BasicDAO;\nimport com.guide.city.model.PojoClass;\nimport java.lang.Exception;\nimport java.lang.Integer;\nimport java.util.List;\n\npublic class PojoClassDao extends BasicDAO<PojoClass, Integer> implements IPojoClassDao {\n\tpublic PojoClassDao(Datastore datastore){\n\t\tsuper(datastore);\n\t}\n\n\tpublic void insert(PojoClass entity) throws DaoException {\n\t\ttry {\n\t\t\tsuper.save(entity);\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic PojoClass find(PojoClass entity) throws DaoException {\n\t\ttry {\n\t\t\treturn super.findOne(\"_id\", entity.getId());\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic List<PojoClass> findAll() throws DaoException {\n\t\ttry {\n\t\t\treturn getCollection().find(PojoClass.class).asList();\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic PojoClass findById(Integer id) throws DaoException {\n\t\ttry {\n\t\t\treturn super.findOne(\"_id\", id);\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic void update(PojoClass entity) throws DaoException {\n\t\ttry {\n\t\t\tsuper.save(entity);\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n\n\tpublic void delete(PojoClass entity) throws DaoException {\n\t\ttry {\n\t\t\tgetCollection().remove(new BasicDBObject().append(\"id\", entity.getId()));\n\t\t} catch(Exception e){\n\t\t\tthrow new DaoException(e);\n\t\t}\n\t}\n}"
        //println(available)
        Assert.assertEquals(expected, available)
      case Failure(e) => Assert.fail(e.getMessage)
    }
  }

  @Test def interfaceCreation() {
    val entity = createPojo
    val adapter = new MongoAdapter(entity, basePackage)
    adapter.addAccessors()
    adapter.annotateClass()

    adapter.createDaoInterface match {
      case Success(inter) =>
        val available = inter.toString
        val expected = "package com.city.guide.dao;\n\nimport java.util.List;\nimport java.lang.Integer;\nimport com.guide.city.model.PojoClass;\nimport com.city.guide.errors.DaoException;\n\npublic interface IPojoClassDao {\n\n\tpublic void insert(PojoClass entity) throws DaoException;\n\n\tpublic PojoClass find(PojoClass entity) throws DaoException;\n\n\tpublic List<PojoClass> findAll() throws DaoException;\n\n\tpublic PojoClass findById(Integer id) throws DaoException;\n\n\tpublic void update(PojoClass entity) throws DaoException;\n\n\tpublic void delete(PojoClass entity) throws DaoException;\n}"
        //println(available)
        Assert.assertEquals(expected, available)
      case Failure(e) => Assert.fail(e.getMessage)
    }
  }


  @Test def factoryClassGeneration() {
    val entity = createPojo
    val adapter = new MongoAdapter(entity, basePackage)
    val config = NoSQLConfig("guidedb", "127.0.0.1", 27017)
    adapter.createDao
    val factoryCreator = adapter.getFactoryCreator
    val factory = factoryCreator.createDaoFactory(config, List(adapter.getDaoCreationFunction.get)).get

    val expected = "package com.city.guide.common;\n\nimport com.city.guide.dao.IPojoClassDao;\nimport com.city.guide.dao.PojoClassDao;\nimport com.google.code.morphia.Datastore;\nimport com.google.code.morphia.Morphia;\nimport com.mongodb.Mongo;\nimport java.lang.Integer;\nimport java.lang.String;\nimport java.net.UnknownHostException;\n\npublic class DaoFactory {\n\tprivate static String DATABASE_URL = \"127.0.0.1\";\n\tprivate static Integer DATABASE_PORT = 27017;\n\tprivate static String DATABASE_NAME = \"guidedb\";\n\tprivate static Datastore datastore;\n\n\tstatic {\n\t\ttry {\n\t\t\tMongo mongo = new Mongo(DATABASE_URL, DATABASE_PORT);\n\t\t\tMorphia morphia = new Morphia();\n\t\t\tdatastore = morphia.createDatastore(mongo, DATABASE_NAME);\n\t\t} catch(UnknownHostException e){\n\t\t\tSystem.err.println(e.getMessage());\n\t\t}\n\t}\n\n\tpublic static IPojoClassDao getPojoClassDao() {\n\t\treturn new PojoClassDao(datastore);\n\t}\n}"
    val available = factory.toString
    //println(available)
    Assert.assertEquals(expected, available)
  }
}
