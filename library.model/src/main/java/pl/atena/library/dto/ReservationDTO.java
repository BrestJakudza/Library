package pl.atena.library.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import pl.atena.library.model.Book;
import pl.atena.library.model.Reservation;
import pl.atena.library.model.ReservationStatus;
import pl.atena.library.model.User;

@Data
public class ReservationDTO implements Serializable {

	private static final long serialVersionUID = -2757062502035497066L;
	private Long id;
	private User user;
	private Book book;
	private ReservationStatus status;
	private Date startDate;

	private String userName = null;
	private String bookName = null;
	
	public ReservationDTO() {
	}

	public ReservationDTO(Reservation reservation) {
		if (reservation == null) {
			reservation = new Reservation();
		}
		this.id = reservation.getId();
		this.book = reservation.getBook();
		this.user = reservation.getUser();
		this.status = reservation.getStatus();
		this.startDate = reservation.getStartDate();
	}

	public Reservation getReservation() {
		return new Reservation(this.id, this.user, this.book, this.status, this.startDate);
	}

	public ReservationDTO(Long id, User user, Book book, ReservationStatus status, Date startDate, 
			String userName, String bookName) {
		super();
		this.id = id;
		this.book = book;
		this.user = user;
		this.status = status;
		this.startDate = startDate;
		this.userName = userName;
		this.bookName = bookName;
	}

}
