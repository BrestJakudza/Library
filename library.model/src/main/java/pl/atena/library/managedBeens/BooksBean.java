package pl.atena.library.managedBeens;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import pl.atena.library.DAO.BookDAO;
import pl.atena.library.dataGenerators.DataGenerator;
import pl.atena.library.model.Book;

@ViewScoped
@ManagedBean(name = "booksBean")
public class BooksBean implements Serializable {

	private static final long serialVersionUID = 6756093678494175086L;

	@Inject
	private Logger log;

	@Inject
	private BookDAO bookDAO;

	@Inject
	private DataGenerator dataGenerator;

	private List<Book> books;

	private List<Book> filteredBooks;

	private Book selectedBook;
	private boolean create;

	@PostConstruct
	public void init() {
		this.books = bookDAO.readAllBooks();
//		this.selectedBook = new Book();
		this.create = false;
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
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	public List<Book> getFilteredBooks() {
		return filteredBooks;
	}

	public void setFilteredBooks(List<Book> filteredBooks) {
		this.filteredBooks = filteredBooks;
	}

	public Book getSelectedBook() {
		return selectedBook;
	}

	public void setSelectedBook(Book selectedBook) {
		this.selectedBook = selectedBook;
	}

}
