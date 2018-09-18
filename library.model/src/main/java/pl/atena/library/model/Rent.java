package pl.atena.library.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@NotNull
	@OneToOne
	@JoinColumn(name = "book_id")
	private Book book;

	@Temporal(TemporalType.DATE)
	private Date startDate;

	@Temporal(TemporalType.DATE)
	private Date endDate;

	@NotNull
	@Enumerated(EnumType.STRING)
	private RentStatus status;

	@Temporal(TemporalType.DATE)
	private Date backDate;

	public Rent() {
	}

	public Rent(Long id, User user, Book book, Date startDate, Date endDate, RentStatus status,
			Date backDate) {
		super();
		this.id = id;
		this.user = user;
		this.book = book;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.backDate = backDate;
	}

	public Rent(Rent rent) {
		super();
		this.id = rent.getId();
		this.user = rent.getUser();
		this.book = rent.getBook();
		this.startDate = rent.getStartDate();
		this.endDate = rent.getEndDate();
		this.status = rent.getStatus();
		this.backDate = rent.getBackDate();
	}

}
