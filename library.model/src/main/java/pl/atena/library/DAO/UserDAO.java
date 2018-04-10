package pl.atena.library.DAO;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import pl.atena.library.model.User;

@Stateless
public class UserDAO {

	private final Logger LOG = Logger.getLogger(UserDAO.class.getName());

	@PersistenceContext(unitName = "libraryDBModel")
	private EntityManager em;

	public UserDAO() {
		LOG.info("******* Created");
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void create(User user) {
		em.persist(user);
		LOG.info("Created new user: " + user);
	}
}
