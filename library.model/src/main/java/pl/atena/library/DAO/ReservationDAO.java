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
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import pl.atena.library.model.Reservation;
import pl.atena.library.model.ReservationStatus;

@Stateless
@Local
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ReservationDAO {

	@Inject
	private Logger log;

	@PersistenceContext(unitName = "libraryDBModel")
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

	public List<Reservation> readAll() {
		return em.createNamedQuery("select r from Reservation r", Reservation.class)
				.getResultList();
	}

	public List<Reservation> readByBook(@NotNull Long bookId) {
		TypedQuery<Reservation> query = em.createQuery("select r from Reservation r "
				+ "where r.bookId = ?1 "
				+ "and r.status = ?2)", Reservation.class);
		query.setParameter(1, bookId);
		query.setParameter(2, pl.atena.library.model.ReservationStatus.Inprogress);
		return query.getResultList();
	}

	public List<Reservation> readByBookAndUser(@NotNull Long bookId, @NotNull Long userId) {
		TypedQuery<Reservation> query = em.createQuery("select r from Reservation r "
				+ " where r.status = ?1 "
				+ " and r.bookId = ?2 "
				+ " and r.userId = ?3 ", Reservation.class);
		query.setParameter(1, ReservationStatus.Inprogress);
		query.setParameter(2, bookId);
		query.setParameter(3, userId);
		return query.getResultList();
	}

	@Deprecated
	public Long getReservationNextId() {
		return Long.valueOf(em.createNativeQuery("select nextval('seq_reservid')").getSingleResult()
				.toString());
	}
}
