package pl.atena.library.utils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import pl.atena.library.DAO.RentDAO;
import pl.atena.library.DAO.ReservationDAO;
import pl.atena.library.model.Rent;
import pl.atena.library.model.RentStatus;
import pl.atena.library.model.Reservation;
import pl.atena.library.model.ReservationStatus;

public class ReservationUtils {

	@Inject
	private ReservationDAO reservationDAO;

	@Inject
	private RentDAO rentDAO;

	public RentStatus getRentStatusForBook(@NotNull Long bookId) {
		List<Rent> rent = rentDAO.readRentByBook(bookId);
		return (rent.size() > 0 ? rent.get(0).getStatus() : null);
	}

	public ReservationStatus getReservationStatusForBook(@NotNull Long bookId) {
		List<Reservation> reserv = reservationDAO.readByBook(bookId);
		return (reserv.size() > 0 ? reserv.get(0).getStatus() : null);
	}

	public ReservationStatus getReservationStatus(@NotNull Reservation reservation) {
		RentStatus rentStatus = getRentStatusForBook(reservation.getBookId());

		if (rentStatus != null) {
			if (RentStatus.Expired.equals(rentStatus)) {
				return ReservationStatus.ExpiredRejected;
			} else if (RentStatus.NotSucceeded.equals(rentStatus)) {
				return ReservationStatus.NotSucceeded;
			} else if (RentStatus.Inprogress.equals(rentStatus)) {
				return ReservationStatus.InabilityRejected;
			}
		}

		ReservationStatus reservStatus = getReservationStatusForBook(reservation.getBookId());
		return (reservStatus != null
				? ReservationStatus.InabilityRejected
				: ReservationStatus.Inprogress);
	}

	public Reservation checkActiveReservation(@NotNull Reservation reservation) {
		List<Reservation> activeReserv = reservationDAO.readByBookAndUser(reservation.getBookId(),
				reservation.getUserId());
		return (activeReserv.size() > 0 ? activeReserv.get(0) : null);
	}

	public Rent checkActiveRent(@NotNull Rent rent) {
		List<Rent> activeRent = rentDAO.readByBookAndUser(rent.getBookId(),
				rent.getUserId());
		return (activeRent.size() > 0 ? activeRent.get(0) : null);
	}

	public static Rent rentFromReserv(@NotNull Reservation reservation) {
		Date currDate = new Date();
		return new Rent(null, reservation.getUserId(), reservation.getBookId(), currDate,
				new Date(currDate.getTime() + TimeUnit.DAYS.toMillis(1)), RentStatus.Inprogress, null);
	}

}
