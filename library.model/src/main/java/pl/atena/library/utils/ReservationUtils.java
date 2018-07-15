package pl.atena.library.utils;

import java.util.List;

import javax.inject.Inject;

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
	
	public ReservationStatus getInsertStatus(Reservation reservation) {
		List<Rent> rent = null;
		rent = rentDAO.readRentByBook(reservation.getBookId());

		if (!rent.isEmpty()) {
			if (RentStatus.Expired.equals(rent.get(0).getStatus())) {
				return ReservationStatus.ExpiredRejected;
			} else if (RentStatus.NotSucceeded.equals(rent.get(0).getStatus())) {
				return ReservationStatus.NotSucceeded;
			} else if (RentStatus.Inprogress.equals(rent.get(0).getStatus())) {
				return ReservationStatus.InabilityRejected;
			}
		}

		List<Reservation> reserv = reservationDAO.find(reservation.getBookId(), reservation.getUserId());
		if (!reserv.isEmpty()) {
			return ReservationStatus.InabilityRejected;
		}
		return ReservationStatus.Inprogress;
	}
	
}
