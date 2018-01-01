package pl.atena.library.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(length = 200)
	private String title;

	@Column(length = 1000)
	private String description;

	@Column(name = "publication_date")
	@Temporal(TemporalType.DATE)
	private Date publicationDate;

	@Column(name = "nr_of_pages")
	private Integer nrOfPages;

	@Column(name = "img_url")
	private String imgURL;

	@Override
	public String toString() {
		return String.format("Book [id=%s, title=%s, description=%s, publicationDate=%s, nrOfPages=%s, imgURL=%s]", id,
				title, description, publicationDate, nrOfPages, imgURL);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	public Integer getNrOfPages() {
		return nrOfPages;
	}

	public void setNrOfPages(Integer nrOfPages) {
		this.nrOfPages = nrOfPages;
	}

	public String getImgURL() {
		return imgURL;
	}

	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}

}
