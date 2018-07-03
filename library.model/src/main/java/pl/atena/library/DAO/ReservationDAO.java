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

import pl.atena.library.model.Reservation;

@Stateless
@Local
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ReservationDAO {

	@Inject
	private Logger log;

	@PersistenceContext(name = "libraryDBModel")
	private EntityManager em;

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void create(@NotNull Reservation reservation) {
		em.persist(reservation);
		log.info("reservation created: " + reservation);
	}

	public Reservation read(@NotNull Long id) {
		return em.find(Reservation.class, id);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void update(@NotNull Reservation reservation) {
		em.merge(reservation);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void delete(@NotNull Long id) {
		Reservation reservation = read(id);
		if (reservation == null) {
			log.warning("Reservation with id = " + id + " was not found");
			return;
		}
		em.remove(reservation);
		log.info("Reservation deleted: " + reservation);
	}

	public List<Reservation> readAllRents() {
		return em.createNamedQuery("select r from Reservation r", Reservation.class).getResultList();
	}

}
