package pl.atena.library.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Data
@Table(name = "BOOKS")
public class Book implements Serializable{
	private static final long serialVersionUID = 7566958458018806285L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Column(length = 200)
	@Size(min = 2, max = 30)
	private String author;

	@NotNull
	@Column(length = 200)
	@Size(min = 2, max = 200)
	private String title;

	@NotNull
	@Column(length = 1000)
	@Size(min = 5, max = 1000)
	private String description;

	@Column(name = "publication_date")
	@Temporal(TemporalType.DATE)
	private Date publicationDate;

	@NotNull
	@Min(2)
	@Column(name = "nr_of_pages")
	private Integer nrOfPages;

	@Column(name = "img_url")
	private String imgURL;

	public Book() {
	}

	public Book(Book book) {
		super();
		this.id = book.id;
		this.author = book.author;
		this.title = book.title;
		this.description = book.description;
		this.publicationDate = book.publicationDate;
		this.nrOfPages = book.nrOfPages;
		this.imgURL = book.imgURL;
	}

}
