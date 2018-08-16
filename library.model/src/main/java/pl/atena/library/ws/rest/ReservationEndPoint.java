package pl.atena.library.ws.rest;

import java.net.URI;
import java.util.Date;
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

import pl.atena.library.DAO.BookDAO;
import pl.atena.library.DAO.ReservationDAO;
import pl.atena.library.DAO.UserDAO;
import pl.atena.library.dto.ReservationDTO;
import pl.atena.library.model.Book;
import pl.atena.library.model.Reservation;
import pl.atena.library.model.ReservationStatus;
import pl.atena.library.model.User;
import pl.atena.library.queue.BookReservationSender;
import pl.atena.library.utils.ReservationUtils;

@Path("/reservation")
public class ReservationEndPoint {

	@Inject
	private Logger log;

	@Inject
	private ReservationDAO reservationDAO;
	
	@Inject
	private ReservationUtils reservationUtils;

	@Inject
	private BookDAO bookDAO;

	@Inject
	private UserDAO userDAO;

	@Inject
	private BookReservationSender bookReserv;

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(@NotNull Reservation reservation, @Context UriInfo uriInfo) {
		reservationDAO.create(reservation);
		URI createdURI = uriInfo.getAbsolutePathBuilder().path(reservation.getId().toString())
				.build();
		log.info("Reservation saved: " + reservation);
		return Response.created(createdURI).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response read(@NotNull @Min(1) @PathParam("id") Long id) {
		Reservation reservation = reservationDAO.read(id);
		ReservationDTO reservationDTO = new ReservationDTO(reservation);
		reservationDTO.setBookId(null);
		reservationDTO.setUserId(null);
		return (reservation != null
				? Response.ok().entity(reservation).build()
				: Response.status(404).entity("Reservation with id = " + id + " was not found")
						.build());
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Response update(@NotNull Reservation reservation) {
		reservationDAO.update(reservation);
		return Response.ok(reservation).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@NotNull @Min(1) @PathParam("id") Long id) {
		reservationDAO.delete(id);
		return Response.noContent().entity(id).build();
	}

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response readAll() {
		List<Reservation> reservation = reservationDAO.readAll();
		if (reservation == null || reservation.size() == 0) {
			return Response.status(Response.Status.NO_CONTENT).build();
		} else {
			return Response.ok(reservation).build();
		}
	}

	@POST
	@Path("/book/{bookId}/by/user/{userId}")
	public Response bookingReservation(
			@NotNull @Min(1) @PathParam("bookId") Long bookId,
			@NotNull @Min(1) @PathParam("userId") Long userId,
			@Context UriInfo uriInfo) {
		Book book = bookDAO.findById(bookId);
		User user = userDAO.findById(userId);

		if (book == null || user == null) {
			return Response.status(404).entity(String.format("book=%s, user=%s", book, user))
					.build();
		}

//		Long reservationId = reservationDAO.getReservationNextId();

		Reservation reservation = new Reservation(null, user.getId(), book.getId(),
				ReservationStatus.Queue, new Date());
		
		if (reservationUtils.checkActiveReservation(reservation) != null) {
			return Response.notModified("This book is already reserved by you").build();
		}
		
		reservationDAO.create(reservation);
		
		ReservationDTO reservDTO = new ReservationDTO(reservation);
		reservDTO.setUserName(user.getName() + " " + user.getSurname());
		reservDTO.setBookName(book.getTitle());
		
		bookReserv.sender(reservDTO);

		URI createdURI = uriInfo.getBaseUriBuilder()
				.path(ReservationEndPoint.class.getAnnotation(Path.class).value())
				.path(String.valueOf(reservation.getId())).build();

		return Response.created(createdURI).build();
	}

}
