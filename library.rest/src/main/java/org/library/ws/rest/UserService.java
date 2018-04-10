package org.library.ws.rest;

import java.util.Objects;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
