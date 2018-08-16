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
@Table(name = "RENT")
public class Rent {

	@Id
	@SequenceGenerator(name = "Seq_RentId", sequenceName = "Seq_RentId", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "Seq_RentId", strategy = GenerationType.SEQUENCE)
	private Long id;

	@NotNull
	private Long userId;

	@NotNull
	private Long bookId;

	@Temporal(TemporalType.DATE)
	private Date startDate;

	@Temporal(TemporalType.DATE)
	private Date endDate;

	@NotNull
	@Enumerated(EnumType.STRING)
	private RentStatus status;

	@Temporal(TemporalType.DATE)
	private Date backDate;

}
