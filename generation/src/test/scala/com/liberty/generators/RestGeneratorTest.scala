package com.liberty.generators

import com.liberty.common.TestModels
import org.junit.{Assert, Test}

/**
 * Created by Dmytro_Kovalskyi on 26.09.2014.
 */
class RestGeneratorTest {
  @Test def generateRest() {
    val generator = new RestGenerator()
    val result = generator.createRest(TestModels.getSubject)
    result match {
      case Some(rest) =>
        val expected = "package standard.rest;\n\nimport java.lang.Integer;\nimport java.lang.String;\nimport java.util.List;\nimport javax.ejb.Stateless;\nimport javax.inject.Inject;\nimport javax.ws.rs.*;\nimport javax.ws.rs.core.MediaType;\nimport standard.beans.ISubjectBean;\nimport standard.errors.ApplicationException;\nimport standard.models.Subject;\n\n@Path(\"/subjects\")\n@Stateless\npublic class SubjectResource {\n\t@Inject\n\tprivate ISubjectBean subjectBean;\n\n\t@GET\n\t@Path(\"/list\")\n\t@Produces(MediaType.APPLICATION_JSON)\n\tpublic List<Subject> getAll() throws ApplicationException {\n\t\treturn subjectBean.getAllSubjects();\n\t}\n\n\t@GET\n\t@Path(\"{id}\")\n\t@Produces(MediaType.APPLICATION_JSON)\n\tpublic Subject get(@PathParam(\"id\") Integer id) throws ApplicationException {\n\t\tSystem.out.println(\"GET get : \" + id);\n\t\treturn subjectBean.getSubject(id);\n\t}\n\n\t@POST\n\t@Path(\"/add\")\n\t@Consumes(MediaType.APPLICATION_JSON)\n\tpublic String add(Subject data) throws ApplicationException {\n\t\tSystem.out.println(\"POST add : \" + data);\n\t\tsubjectBean.addSubject(data);\n\t\treturn \"Added Subject\";\n\t}\n\n\t@POST\n\t@Path(\"/update\")\n\t@Consumes(MediaType.APPLICATION_JSON)\n\tpublic String update(Subject data) throws ApplicationException {\n\t\tSystem.out.println(\"POST update : \" + data);\n\t\tsubjectBean.updateSubject(data);\n\t\treturn \"Updated Subject\";\n\t}\n\n\t@DELETE\n\t@Path(\"{id}\")\n\tpublic String delete(@PathParam(\"id\") Integer id) {\n\t\ttry {\n\t\t\tsubjectBean.deleteSubject(id);\n\t\t\treturn \"Removed Subject with id : \" + id;\n\t\t} catch(Exception e){\n\t\t\tSystem.err.println(\"[SubjectResource] error\");\n\t\t\treturn \"Error removing Subject with id : \" + id;\n\t\t}\n\t}\n}"
        Assert.assertEquals(expected, rest.toString)
      case None => Assert.fail("WS was not created")
    }
  }

  @Test def generateWS() {
    val generator = new RestGenerator()
    val result = generator.createWsClass
    val expected = "package standard.rest;\n\nimport javax.ws.rs.ApplicationPath;\nimport javax.ws.rs.core.Application;\n\n@ApplicationPath(\"rest\")\npublic class WS extends Application {\n\n}"
    Assert.assertEquals(expected, result.toString)
  }
}

