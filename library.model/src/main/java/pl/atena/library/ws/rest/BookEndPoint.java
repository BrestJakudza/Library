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
import pl.atena.library.model.Book;

@Path("/books")
public class BookEndPoint {

	@Inject
	private Logger log;

	@Inject
	private BookDAO bookDAO;

	@POST
	@Path("/book")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(@NotNull Book book, @Context UriInfo uriInfo) {
		bookDAO.create(book);
		URI createdURI = uriInfo.getAbsolutePathBuilder().path(book.getTitle()).build();
		log.info("Book created: " + book);
		return Response.created(createdURI).build();
	}

	@GET
	@Path("/book/{title}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response read(@NotNull @Size(min = 2) @PathParam("title") String title) {
		Book book = null;
		try {
			book = bookDAO.findByTitle(title);
		} catch (Exception e) {
			return Response.noContent().build();
		}
		return Response.ok().entity(book != null ? book : new Book()).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/book")
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Response update(@NotNull Book book) {
		bookDAO.update(book);
		return Response.ok(book).build();
	}

	@DELETE
	@Path("/book/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@NotNull @Min(1) @PathParam("id") Long id) {
		bookDAO.delete(id);
		return Response.noContent().entity(id).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response readAll() {
		List<Book> books = bookDAO.readAllBooks();
		if (books == null || books.size() == 0) {
			return Response.status(Response.Status.NO_CONTENT).build();
		} else {
			return Response.ok(books).build();
		}
	}

}
