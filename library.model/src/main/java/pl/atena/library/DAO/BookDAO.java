package pl.atena.library.DAO;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import pl.atena.library.model.Book;

/**
 * Session Bean implementation class BookDAO
 */
@Stateless
@LocalBean
public class BookDAO {

	@PersistenceContext(name = "libraryDBModel")
	private EntityManager em;

	public BookDAO() {
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean update(Book book) {
		if (book == null) {
			return false;
		}
		em.merge(book);
		return true;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean delete(Long id) {
		Book book = findById(id);
		if (book == null) {
			return false;
		}
		em.remove(book);
		return (findById(book.getId()) == null ? true : false);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean create(Book book) {
		em.persist(book);
		return true;
	}

	public List<Book> getAllBooks() {
		TypedQuery<Book> query = em.createQuery("select b from Book b order by b.title", Book.class);
		return query.getResultList();
	}

	public Book findById(Long id) {
		return (Book) em.find(Book.class, id);
	}

}
