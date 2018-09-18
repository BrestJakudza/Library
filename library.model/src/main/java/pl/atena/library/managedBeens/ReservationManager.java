package pl.atena.library.managedBeens;

import java.util.Date;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import Exceptions.ReservationEmptyData;
import Exceptions.ReservationExistException;
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

@Stateless
@Local
public class ReservationManager {

	@Inject
	private BookDAO bookDAO;

	@Inject
	private UserDAO userDAO;

	@Inject
	private ReservationUtils reservationUtils;

	@Inject
	private ReservationDAO reservationDAO;

	@Inject
	private BookReservationSender bookReserv;

	public synchronized Reservation bookingReservation(
			@NotNull @Min(1) final Long bookId,
			@NotNull @Min(1) final Long userId,
			@NotNull final Date date)
			throws ReservationEmptyData, ReservationExistException {
		final Book book = bookDAO.findById(bookId);
		final User user = userDAO.findById(userId);

		if (book == null || user == null) {
			throw new ReservationEmptyData(String.format("No data for: book=%s, user=%s", book, user));
		}

//		Long reservationId = reservationDAO.getReservationNextId();

		Reservation reservation = new Reservation(null, user, book,
				ReservationStatus.Queue, date);

		if (reservationUtils.getRentStatusForBook(reservation.getBook()) != null
				&& !reservation.getUser().getId().equals(userId)) {
			throw new ReservationExistException("This book is already rented");
		}

		if (reservationUtils.checkActiveReservationForCurrentUser(reservation) != null) {
			throw new ReservationExistException("This book is already rented by you");
		}

		ReservationStatus reservStatus = reservationUtils.getReservationStatus(reservation);
		
		if (!ReservationStatus.Inprogress.equals(reservStatus)) {
			throw new ReservationExistException(reservStatus.getStatusInfo());
		}
		
		reservationDAO.create(reservation);

		ReservationDTO reservDTO = new ReservationDTO(reservation);
//		reservDTO.setUserName(user.getName() + " " + user.getSurname());
//		reservDTO.setBookName(book.getTitle());

		bookReserv.sender(reservDTO);

		return reservation;
	}
}
