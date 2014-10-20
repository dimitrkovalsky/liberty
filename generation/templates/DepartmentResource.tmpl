package standard.rest;

import standard.beans.IDepartmentBean;
import standard.errors.ApplicationException;
import standard.models.Department;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
/**
 * Created by Dmytro_Kovalskyi on 01.09.2014.
 */
@Path("/departments")
@Stateless
public class DepartmentResource {
    @Inject
    private IDepartmentBean departmentBean;

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Department> getAll() throws ApplicationException {
        return departmentBean.getAllDepartments();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Department get(@PathParam("id") Long id) throws ApplicationException {
        System.out.println("GET get : " + id);
        return departmentBean.getDepartment(id);
    }

    // Consumes {"name":"Dep_1","instituteId":1}
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public String add(Department data) throws ApplicationException {
        System.out.println("POST add : " + data);
        departmentBean.addDepartment(data);
        return "Added Department";
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public String update(Department data) throws ApplicationException {
        System.out.println("POST update : " + data);
        departmentBean.updateDepartment(data);
        return "Updated Department";
    }


    // TODO: departmentBean.getAllDepartmentsByInstitute(id) in InstituteResource

    @DELETE
    @Path("{id}")
    public String delete(@PathParam("id") Long id) {
        try {
            departmentBean.deleteDepartment(id);
            return "Removed Department with id : " + id;
        } catch (Exception e) {
            System.err.println("[DepartmentResource] error");
            return "Error removing Department with id : " + id;
        }
    }
}
