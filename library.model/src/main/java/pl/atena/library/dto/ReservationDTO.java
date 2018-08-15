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

	private String userName;
	private String bookName;

	public ReservationDTO(Reservation reservation) {
		this.id = reservation.getId();
		this.bookId = reservation.getBookId();
		this.userId = reservation.getUserId();
		this.status = reservation.getStatus();
		this.startDate = reservation.getStartDate();
	}
	
	public Reservation getReservation() {
		return new Reservation(this.id, this.bookId, this.userId, this.status, this.startDate);
	}

}
