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
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import pl.atena.library.model.User;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class UserDAO {

	@Inject
	private Logger log;

	@PersistenceContext(unitName = "libraryDBModel")
	private EntityManager em;

	public UserDAO() {
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean update(@NotNull User user) {
		log.info("update: " + user);
		em.merge(user);
		return true;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean delete(@NotNull Long id) {
		User user = findById(id);
		if (user == null) {
			log.warning("User with id = " + id + " was not found");
			return false;
		}

		em.remove(user);
		log.info("User with id = " + id + " was removed");
		return true;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void create(@NotNull User user) {
		em.persist(user);
		log.info("Created new user: " + user);
	}

	public User findById(@NotNull Long id) {
		return em.find(User.class, id);

	}

	public User findByName(@NotNull String name) {
		if (name.isEmpty()) {
			return null;
		}
		TypedQuery<User> query = em.createQuery("select u from User u where u.name = ?1", User.class);
		query.setParameter(1, name);
		return query.getSingleResult();
	}

	public List<User> readAllUsers() {
		return em.createQuery("select u from User u", User.class).getResultList();
	}
}
