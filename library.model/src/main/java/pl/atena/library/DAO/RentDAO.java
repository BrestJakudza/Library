package pl.atena.library.DAO;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

import pl.atena.library.model.Rent;

@Stateless
@Local
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class RentDAO {

	@Inject
	private Logger log;

	@PersistenceContext(name = "libraryDBModel")
	private EntityManager em;

	public RentDAO() {
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void create(@NotNull Rent rent) {
		em.persist(rent);
		log.info("Rent created: " + rent);
	}

	public Rent read(@NotNull Long id) {
		return em.find(Rent.class, id);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(@NotNull Rent rent) {
		em.merge(rent);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void delete(@NotNull Long id) {
		Rent rent = read(id);
		if (rent == null) {
			log.warning("Rent with id = " + id + " was not found");
			return;
		}
		em.remove(rent);
		log.info("Rent deleted: " + rent);
	}

	public List<Rent> readAllRents() {
		return em.createNamedQuery("select r from Rented r", Rent.class).getResultList();
	}

}
