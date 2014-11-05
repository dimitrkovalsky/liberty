package com.liberty.parsers

import com.liberty.common.Filable
import org.junit.{Assert, Test}

/**
 * Created by Dmytro_Kovalskyi on 08.09.2014.
 */
class JavaClassParserTest extends Filable {
  val REST_RESOURCE_PATH = STANDARDS_FOLDER + "parsers\\"

  @Test def testRestResource() {
    val parser = new JavaClassParser("DepartmentResource.tmpl") with Pathable {
      override def getTemplatePath: String = REST_RESOURCE_PATH
    }
    val clazz = parser.parse("standard")
    //println(clazz)
    val expected = "package com.test.rest;\n\nimport java.lang.Long;\nimport java.lang.String;\nimport java.util.List;\nimport javax.ejb.Stateless;\nimport javax.inject.Inject;\nimport javax.ws.rs.*;\nimport javax.ws.rs.core.MediaType;\nimport standard.beans.IDepartmentBean;\nimport standard.errors.ApplicationException;\nimport standard.models.Department;\n\n@Path(\"/departments\")\n@Stateless\npublic class DepartmentResource {\n\t@Inject\n\t@Entity(value = \"deps\")\n\tprivate IDepartmentBean departmentBean;\n\tprivate static String CONSTANT = \"SOME DATA\";\n\n\t@GET\n\t@Path(\"/list\")\n\t@Produces(MediaType.APPLICATION_JSON)\n\tpublic List<Department> getAll() throws ApplicationException {\n\t\treturn departmentBean.getAllDepartments();\n\t}\n\n\t@GET\n\t@Path(\"{id}\")\n\t@Produces(MediaType.APPLICATION_JSON)\n\tpublic Department get(Long id) throws ApplicationException {\n\t\tSystem.out.println(\"GET get : \" + id);\n\t\treturn departmentBean.getDepartment(id);\n\t}\n\n\t@POST\n\t@Path(\"/add\")\n\t@Consumes(MediaType.APPLICATION_JSON)\n\tpublic String add(Department data) throws ApplicationException {\n\t\tSystem.out.println(\"POST add : \" + data);\n\t\tdepartmentBean.addDepartment(data);\n\t\treturn \"Added Department\";\n\t}\n\n\t@POST\n\t@Path(\"/update\")\n\t@Consumes(MediaType.APPLICATION_JSON)\n\tpublic String update(Department data) throws ApplicationException {\n\t\tSystem.out.println(\"POST update : \" + data);\n\t\tdepartmentBean.updateDepartment(data);\n\t\treturn \"Updated Department\";\n\t}\n\n\t@DELETE\n\t@Path(\"{id}\")\n\tpublic String delete(Long id){\n\t\ttry {\n\t\t\tdepartmentBean.deleteDepartment(id);\n\t\t\treturn \"Removed Department with id : \" + id;\n\t\t} catch(Exception e){\n\t\t\tSystem.err.println(\"[DepartmentResource] error\");\n\t\t\treturn \"Error removing Department with id : \" + id\n\t\t}\n\t}\n}"
    Assert.assertEquals(expected, clazz.toString)
  }
}
