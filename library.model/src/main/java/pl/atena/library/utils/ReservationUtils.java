package pl.atena.library.utils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import pl.atena.library.DAO.RentDAO;
import pl.atena.library.DAO.ReservationDAO;
import pl.atena.library.dto.RentWS;
import pl.atena.library.model.Book;
import pl.atena.library.model.Rent;
import pl.atena.library.model.RentStatus;
import pl.atena.library.model.Reservation;
import pl.atena.library.model.ReservationStatus;

public class ReservationUtils {

	@Inject
	private ReservationDAO reservationDAO;

	@Inject
	private RentDAO rentDAO;

	public RentStatus getRentStatusForBook(@NotNull Book book) {
		List<Rent> rent = rentDAO.readRentByBook(book);
		return (rent.size() > 0 ? rent.get(0).getStatus() : null);
	}

	public ReservationStatus getReservationStatusForBook(@NotNull Book book) {
		List<Reservation> reserv = reservationDAO.readByBook(book);
		return (reserv.size() > 0 ? reserv.get(0).getStatus() : null);
	}

	public ReservationStatus getReservationStatus(@NotNull Reservation reservation) {
		RentStatus rentStatus = getRentStatusForBook(reservation.getBook());

		if (rentStatus != null) {
			if (RentStatus.Expired.equals(rentStatus)) {
				return ReservationStatus.ExpiredRejected;
			} else if (RentStatus.NotSucceeded.equals(rentStatus)) {
				return ReservationStatus.NotSucceeded;
			} else if (RentStatus.Inprogress.equals(rentStatus)) {
				return ReservationStatus.InabilityRejected;
			}
		}

		ReservationStatus reservStatus = getReservationStatusForBook(reservation.getBook());
		return (reservStatus != null
				? ReservationStatus.InabilityRejected
				: ReservationStatus.Inprogress);
	}

	public Reservation checkActiveReservationForCurrentUser(@NotNull Reservation reservation) {
		List<Reservation> activeReserv = reservationDAO.readByBookAndUser(reservation.getBook(),
				reservation.getUser());
		return (activeReserv.size() > 0 ? activeReserv.get(0) : null);
	}

	public Rent checkActiveRent(@NotNull Rent rent) {
		List<Rent> activeRent = rentDAO.readByBookAndUser(rent.getBook(), rent.getUser());
		return (activeRent.size() > 0 ? activeRent.get(0) : null);
	}

	public static Rent rentFromReserv(@NotNull Reservation reservation) {
		Date currDate = new Date();
		return new Rent(null, reservation.getUser(), reservation.getBook(),
				currDate,
				new Date(currDate.getTime() + TimeUnit.DAYS.toMillis(1)), RentStatus.Inprogress,
				null);
	}

	public static Rent rentFromRentWSandReservation(@NotNull RentWS rentWS,
			@NotNull Reservation reservation) {
		return new Rent(null, reservation.getUser(), reservation.getBook(),
				rentWS.getStartDate(), rentWS.getEndDate(), RentStatus.Inprogress, null);
	}

}
