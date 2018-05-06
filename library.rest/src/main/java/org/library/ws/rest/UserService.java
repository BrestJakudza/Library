package org.library.ws.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import pl.atena.library.DAO.UserDAO;
import pl.atena.library.model.User;

@Path("/user")
public class UserService {

	private final Logger LOG = Logger.getLogger(UserService.class.getName());

	@Inject
	private UserDAO userDAO;

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Response updeteUser(@NotNull User user) {
		userDAO.update(user);
		return Response.status(204).entity(user).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(@NotNull @Min(1) @PathParam("id") Long id) {
		userDAO.delete(id);
		return Response.noContent().entity(id).build();
	}

	@GET
	@Path("/{name}")
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
	@Path("/users")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsers() {
		List<User> users = new ArrayList<>();
		users = userDAO.getAllUsers();
		if (users == null || users.size() == 0) {
			return Response.noContent().build();
		} else {
			return Response.ok().entity(users).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(@NotNull User user) {
		if (Objects.nonNull(user)) {
			userDAO.create(user);
			LOG.info("User created: " + user);
			return Response.noContent().entity(user).build();
		} else {
			LOG.severe("User created: " + user);
			return Response.status(204).entity(user).build();
		}
	}

}
