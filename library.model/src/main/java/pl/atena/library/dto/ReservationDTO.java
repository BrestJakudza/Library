package pl.atena.library.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import pl.atena.library.model.Reservation;
import pl.atena.library.model.ReservationStatus;

@Data
public class ReservationDTO implements Serializable {

	private static final long serialVersionUID = -2757062502035497066L;
	private Long id;
	private Long bookId;
	private Long userId;
	private ReservationStatus status;
	private Date startDate;

	private String userName = null;
	private String bookName = null;

	public ReservationDTO(Reservation reservation) {
		if (reservation == null) {
			reservation = new Reservation();
		}
		this.id = reservation.getId();
		this.bookId = reservation.getBookId();
		this.userId = reservation.getUserId();
		this.status = reservation.getStatus();
		this.startDate = reservation.getStartDate();
	}

	public Reservation getReservation() {
		return new Reservation(this.id, this.bookId, this.userId, this.status, this.startDate);
	}

	public ReservationDTO(Long id, Long bookId, Long userId, ReservationStatus status, Date startDate, 
			String userName, String bookName) {
		super();
		this.id = id;
		this.bookId = bookId;
		this.userId = userId;
		this.status = status;
		this.startDate = startDate;
		this.userName = userName;
		this.bookName = bookName;
	}

}
