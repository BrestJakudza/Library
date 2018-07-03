package pl.atena.library.ws.rest;

import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import pl.atena.library.DAO.UserDAO;
import pl.atena.library.model.User;

@Path("/users")
public class UserEndPoint {

	@Inject
	private Logger log;

	@Inject
	private UserDAO userDAO;

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/user")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Response updateUser(@NotNull User user) {
		userDAO.update(user);
		return Response.ok(user).build();
	}

	@DELETE
	@Path("/user/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(@NotNull @Min(1) @PathParam("id") Long id) {
		userDAO.delete(id);
		return Response.noContent().entity(id).build();
	}

	@GET
	@Path("/user/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@NotNull @Size(min = 1) @PathParam("name") String name) {
		User user = null;
		try {
			user = userDAO.findByName(name);
		} catch (Exception e) {
			return Response.noContent().build();
		}
		return Response.ok().entity(user != null ? user : new User()).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response readAllUsers() {
		List<User> users = userDAO.readAllUsers();
		if (users == null || users.size() == 0) {
			return Response.status(Response.Status.NO_CONTENT).build();
		} else {
			return Response.ok(users).build();
		}
	}

	@POST
	@Path("/user")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(@NotNull User user, @Context UriInfo uriInfo) {
		userDAO.create(user);
		URI createdURI = uriInfo.getAbsolutePathBuilder().path(user.getName()).build();
		log.info("User created: " + user);
		return Response.created(createdURI).build();
	}
	
	@HEAD
	@Path("/test")
	public Response test(@Context UriInfo uri) {
		URI cURI = uri.getAbsolutePathBuilder().path("xxx").build();
		return Response.created(cURI).build();
	}

}
