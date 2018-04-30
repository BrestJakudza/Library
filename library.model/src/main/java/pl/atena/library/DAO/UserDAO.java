package pl.atena.library.DAO;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import pl.atena.library.model.User;

@Stateless
@LocalBean
public class UserDAO {

	private final Logger LOG = Logger.getLogger(UserDAO.class.getName());

	@PersistenceContext(unitName = "libraryDBModel")
	private EntityManager em;

	public UserDAO() {
		LOG.info("******* Created");
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean update(User user) {
		if (user == null) {
			return false;
		}
		em.merge(user);
		return true;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean delete(Long id) {
		if (id == null) {
			return false;
		}
		User user = findById(id);
		if (user == null) {
			return false;
		}

		em.remove(user);
		return true;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void create(User user) {
		em.persist(user);
		LOG.info("Created new user: " + user);
	}

	public User findById(Long id) {
		if (id == null) {
			return null;
		}

		return em.find(User.class, id);

	}

	public User fingByName(String name) {
		if (name == null || name.isEmpty()) {
			return null;
		}
		TypedQuery<User> query = em.createQuery("select u from User u where u.name = ?1", User.class);
		query.setParameter(1, name);
		return query.getSingleResult();
	}

	public List<User> getAllUsers() {
		TypedQuery<User> query = em.createQuery("select u from User u", User.class);
		return query.getResultList();
	}
}
