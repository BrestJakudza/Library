package pl.atena.library.DAO;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

import pl.atena.library.model.Book;

/**
 * Session Bean implementation class BookDAO
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class BookDAO {

	@PersistenceContext(name = "libraryDBModel")
	private EntityManager em;

	@Inject
	private Logger log;

	public BookDAO() {
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean update(@NotNull Book book) {
		em.merge(book);
		log.info("Book updated: " + book);
		return true;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean delete(@NotNull Long id) {
		Book book = findById(id);
		if (book == null) {
			log.warning("Book with id = " + id + " was not found");
			return false;
		}
		em.remove(book);
		log.info("Book with id = " + id + " deleted");
		return (findById(book.getId()) == null ? true : false);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean create(@NotNull Book book) {
		em.persist(book);
		log.info("Created new book: " + book);
		return true;
	}

	public List<Book> getAllBooks() {
		return em.createQuery("select b from Book b order by b.title", Book.class).getResultList();
	}

	public Book findById(@NotNull Long id) {
		return (Book) em.find(Book.class, id);
	}

}
