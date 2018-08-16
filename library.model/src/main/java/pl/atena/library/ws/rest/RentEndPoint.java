package pl.atena.library.ws.rest;

import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import pl.atena.library.DAO.RentDAO;
import pl.atena.library.DAO.ReservationDAO;
import pl.atena.library.model.Rent;
import pl.atena.library.model.Reservation;
import pl.atena.library.model.ReservationStatus;
import pl.atena.library.utils.ReservationUtils;

@Path("/rent")
public class RentEndPoint {

	@Inject
	private Logger log;

	@Inject
	private RentDAO rentDAO;

	@Inject
	private ReservationDAO reservationDAO;

	@Inject
	private ReservationUtils reservationUtils;

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(@NotNull Rent rent, @Context UriInfo uriInfo) {
		rentDAO.create(rent);
		URI createdURI = uriInfo.getAbsolutePathBuilder().path(rent.getId().toString())
				.build();
		log.info("Rent created: " + rent);
		return Response.created(createdURI).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response read(@NotNull @Min(1) @PathParam("id") Long id) {
		Rent rent = rentDAO.read(id);
		return (rent != null
				? Response.ok().entity(rent).build()
				: Response.status(404).entity("Rent with id = " + id + " was not found")
						.build());
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Response update(@NotNull Rent rent) {
		rentDAO.update(rent);
		return Response.ok(rent).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@NotNull @Min(1) @PathParam("id") Long id) {
		rentDAO.delete(id);
		return Response.noContent().entity(id).build();
	}

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readAll() {
		List<Rent> rent = rentDAO.readAll();
		if (rent == null || rent.size() == 0) {
			return Response.status(Response.Status.NO_CONTENT).build();
		} else {
			return Response.ok(rent).build();
		}
	}

	@POST
	@Path("/reservation/{id}")
	public Response makeRent(
			@NotNull @Min(1) @PathParam("id") Long id,
			@Context UriInfo uriInfo) {
		Reservation reservation = reservationDAO.read(id);
		
		if (reservationUtils.getRentStatusForBook(reservation.getBookId()) != null) {
			return Response.notModified("This book is already rented by you").build();
		}

		if (reservation == null || !ReservationStatus.Inprogress.equals(reservation.getStatus())) {
			return Response
					.notModified("Book is not reserved for you (reservation status != Inprogress)")
					.build();
		}

		Rent rent = ReservationUtils.rentFromReserv(reservation);
		rentDAO.create(rent);
		reservation.setStatus(ReservationStatus.Succeeded);
		reservationDAO.update(reservation);
		
		URI createdURI = uriInfo.getBaseUriBuilder()
				.path(RentEndPoint.class.getAnnotation(Path.class).value())
				.path(String.valueOf(rent.getId())).build();

		return Response.created(createdURI).build();
	}

}
