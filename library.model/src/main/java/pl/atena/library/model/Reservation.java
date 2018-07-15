package pl.atena.library.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	private Long userId;
	
	@NotNull
	private Long bookId;

	@NotNull
	private ReservationStatus status;
	
	@Temporal(TemporalType.DATE)
	private Date startDate;
}
