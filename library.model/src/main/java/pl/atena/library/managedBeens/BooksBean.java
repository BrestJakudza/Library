package pl.atena.library.managedBeens;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import Exceptions.ReservationEmptyData;
import Exceptions.ReservationExistException;
import lombok.Data;
import pl.atena.library.DAO.BookDAO;
import pl.atena.library.DAO.UserDAO;
import pl.atena.library.dataGenerators.DataGenerator;
import pl.atena.library.dto.ReservationDTO;
import pl.atena.library.model.Book;
import pl.atena.library.model.User;
import pl.atena.library.utils.Growl;

@Data
@ViewScoped
@ManagedBean(name = "booksBean")
public class BooksBean implements Serializable {

	private static final long serialVersionUID = 6756093678494175086L;

	@Inject
	private BookDAO bookDAO;

	@Inject
	private UserDAO userDAO;

	@Inject
	private DataGenerator dataGenerator;

	@Inject
	private ReservationManager reservationManagedBean;

	@Inject
	private Logger log;

	private List<Book> books;

	private List<Book> filteredBooks;

	private Book selectedBook;
	private boolean create;
	private ReservationDTO reservation;
	private List<User> allUsers;

	@PostConstruct
	public void init() {
		this.books = bookDAO.readAllBooks();
		this.create = false;
		this.allUsers = userDAO.readAllUsers();
		this.selectedBook = null;
	}

	public List<User> autocompleteUser(String query) {
		List<User> filteredUsers = new ArrayList<User>();
		for (int i = 0; i < allUsers.size(); i++) {
			User user = allUsers.get(i);
			if (user.getName().toLowerCase().startsWith(query)) {
				filteredUsers.add(user);
			}
		}
		return filteredUsers;
	}

	public void bookingReservation() {
		log.info("bookingReservation");
		if (this.reservation != null) {
			try {
				reservationManagedBean.bookingReservation(this.reservation.getBook().getId(),
						this.reservation.getUser().getId());
				Growl.showMsg(FacesMessage.SEVERITY_INFO, "Reservation complited",
						this.reservation.toString());
			} catch (ReservationEmptyData e) {
				Growl.showMsg(FacesMessage.SEVERITY_ERROR, "Empty data", e.getLocalizedMessage());
			} catch (ReservationExistException e) {
				Growl.showMsg(FacesMessage.SEVERITY_ERROR, "Reservation exists",
						e.getLocalizedMessage());
			}
		}
	}

	public void createReservation() {
		this.reservation = new ReservationDTO();
		this.reservation.setBook(this.selectedBook);
		this.reservation.setStartDate(new Date());
	}

	public void cancelReservation() {
		this.reservation = null;
	}

	public void create() {
		this.selectedBook = dataGenerator.generateTestBook();
		this.create = true;
	}

	public void save() {
		if (this.create) {
			Book newBook = new Book();
			newBook.setTitle(this.selectedBook.getTitle());
			newBook.setAuthor(this.selectedBook.getAuthor());
			newBook.setDescription(this.selectedBook.getDescription());
			newBook.setNrOfPages(this.selectedBook.getNrOfPages());
			newBook.setPublicationDate(this.selectedBook.getPublicationDate());
			bookDAO.create(newBook);
			init();
		} else {
			bookDAO.update(selectedBook);
		}
	}

	public void delete() {
		bookDAO.delete(this.selectedBook.getId());
		init();
	}
}
