package pl.atena.library.dto;

import java.util.Date;

import lombok.Data;

@Data
public class RentWS {

	private Long reservationId;
	private Long userId;
	private Date startDate;
	private Date endDate;

	public RentWS() {
	}

	public RentWS(Long reservationId, Long userId, Date startDate, Date endDate) {
		super();
		this.reservationId = reservationId;
		this.userId = userId;
		this.startDate = startDate;
		this.endDate = endDate;
	}

}
