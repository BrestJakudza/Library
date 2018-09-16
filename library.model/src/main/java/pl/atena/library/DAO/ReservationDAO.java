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

import pl.atena.library.model.Book;
import pl.atena.library.model.Reservation;
import pl.atena.library.model.ReservationStatus;
import pl.atena.library.model.User;

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
		return em.createQuery("select r from Reservation r", Reservation.class)
				.getResultList();
	}

	public List<Reservation> readByBook(@NotNull Book book) {
		TypedQuery<Reservation> query = em.createQuery("select r from Reservation r "
				+ "where r.book = ?1 "
				+ "and r.status = ?2)", Reservation.class);
		query.setParameter(1, book);
		query.setParameter(2, pl.atena.library.model.ReservationStatus.Inprogress);
		return query.getResultList();
	}

	public List<Reservation> readByBookAndUser(@NotNull Book book, @NotNull User user) {
		TypedQuery<Reservation> query = em.createQuery("select r from Reservation r "
				+ " where r.status = ?1 "
				+ " and r.book = ?2 "
				+ " and r.user = ?3 ", Reservation.class);
		query.setParameter(1, ReservationStatus.Inprogress);
		query.setParameter(2, book);
		query.setParameter(3, user);
		return query.getResultList();
	}

	@Deprecated
	public Long getReservationNextId() {
		return Long.valueOf(em.createNativeQuery("select nextval('seq_reservid')").getSingleResult()
				.toString());
	}
}
