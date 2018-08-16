package pl.atena.library.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
@Table(name = "RESERVATION")
public class Reservation {

	@Id
	@SequenceGenerator(name = "Seq_ReservId", sequenceName = "Seq_ReservId", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "Seq_ReservId", strategy = GenerationType.SEQUENCE)
	private Long id;

	@NotNull
	private Long userId;

	@NotNull
	private Long bookId;

	@NotNull
	@Enumerated(EnumType.STRING)
	private ReservationStatus status;

	@Temporal(TemporalType.DATE)
	private Date startDate;

	public Reservation() {
	}

	public Reservation(Long id, Long userId, Long bookId, ReservationStatus status,
			Date startDate) {
		this.id = id;
		this.userId = userId;
		this.bookId = bookId;
		this.status = status;
		this.startDate = startDate;
	}

}
