package pl.atena.library.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ReservationWS {
	private Long bookId;
	private Long userId;
	private Date startDate;

	public ReservationWS() {
	}
	
	public ReservationWS(Long bookId, Long userId, Date startDate) {
		super();
		this.bookId = bookId;
		this.userId = userId;
		this.startDate = startDate;
	}

}
