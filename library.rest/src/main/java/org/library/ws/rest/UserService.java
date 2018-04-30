package org.library.ws.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
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
	public Response updeteUser(User user) {
		if (user == null) {
			return Response.status(200).tag("Please, set an user data").build();
		}
		userDAO.update(user);
		return Response.status(204).entity(user).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(@PathParam("id") Long id) {
		if (id == null) {
			return Response.status(200).tag("Please, set an user id").build();
		}
		userDAO.delete(id);
		return Response.status(204).entity(id).build();
	}

	@GET
	@Path("/{name}")
	// @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@PathParam("name") String name) {
		if (name == null || name.isEmpty()) {
			return Response.status(201).entity("Set name to search").build();
		} else {
			User user = null;
			try {
				user = userDAO.fingByName(name);
			} catch (Exception e) {
				//
			}
			return Response.status(200).entity(user != null ? user : new User()).build();
		}
	}

	@GET
	@Path("/users")
	// @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsers() {
		List<User> users = new ArrayList<>();
		users = userDAO.getAllUsers();
		if (users == null || users.size() == 0) {
			return Response.status(201).entity(users).build();
		} else {
			return Response.status(200).entity(users).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(User user) {
		if (Objects.nonNull(user)) {
			userDAO.create(user);
			LOG.info("User created: " + user);
			return Response.status(201).entity(user).build();
		} else {
			LOG.severe("User created: " + user);
			return Response.status(204).entity(user).build();
		}
	}

}
