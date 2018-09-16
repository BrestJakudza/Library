package pl.atena.library.managedBeens;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import Exceptions.RentExistException;
import Exceptions.RentNoReservationException;
import pl.atena.library.DAO.RentDAO;
import pl.atena.library.DAO.ReservationDAO;
import pl.atena.library.model.Rent;
import pl.atena.library.model.Reservation;
import pl.atena.library.model.ReservationStatus;
import pl.atena.library.utils.ReservationUtils;

@Stateless
@Local
public class RentManager {

	@Inject
	private RentDAO rentDAO;

	@Inject
	private ReservationDAO reservationDAO;

	@Inject
	private ReservationUtils reservationUtils;

	public synchronized Rent makeRent(@NotNull @Min(1) Long reservationId,
			@NotNull @Min(1) Long userId)
			throws RentNoReservationException, RentExistException {
		Reservation reservation = reservationDAO.read(reservationId);

		if (reservation == null || !ReservationStatus.Inprogress.equals(reservation.getStatus())) {
			throw new RentNoReservationException(
					"No reservation for this book");
		} else if (!reservation.getUser().getId().equals(userId)) {
			throw new RentNoReservationException(
					"Book is not reserved for you (reservation status != Inprogress)");
		}

		if (reservationUtils.getRentStatusForBook(reservation.getBook().getId()) != null) {
			throw new RentExistException("This book is already rented");
		}

		Rent rent = ReservationUtils.rentFromReserv(reservation);
		rentDAO.create(rent);
		reservation.setStatus(ReservationStatus.Succeeded);
		reservationDAO.update(reservation);

		return rent;
	}
}
