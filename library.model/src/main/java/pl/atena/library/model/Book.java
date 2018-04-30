package pl.atena.library.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Data
@Table(name = "BOOKS")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(length = 200)
//	@NotNull
//	@Min(value = 3)
//	@Max(value = 200)
	private String title;

	@Column(length = 1000)
//	@NotNull
//	@Min(value = 5)
//	@Max(value = 1000)
	private String description;

	@Column(name = "publication_date")
	@Temporal(TemporalType.DATE)
	private Date publicationDate;

//	@NotNull
//	@Min(value = 2)
	@Column(name = "nr_of_pages")
	private Integer nrOfPages;

	@Column(name = "img_url")
	private String imgURL;

}
