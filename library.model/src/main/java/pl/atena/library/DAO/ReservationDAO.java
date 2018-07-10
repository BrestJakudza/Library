package pl.atena.library.DAO;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import Exceptions.ReservationExistException;
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
	public void create(@NotNull Reservation reservation) throws ReservationExistException {
		Reservation reserv = null;
		try {
			reserv = find(reservation.getBookId(), reservation.getUserId());
			throw new ReservationExistException("book with id=" + reservation.getBookId() 
			+ " is already reserved by user with id=" + reservation.getUserId());
		} catch (NoResultException e) {
			log.info(e.getLocalizedMessage());
		}
		if (reserv == null) {
			em.persist(reservation);
			log.info("reservation created: " + reservation);
		} else {
			log.warning("book with id=" + reservation.getBookId() 
			+ " is already reserved");
		}
	}

	public Reservation read(@NotNull Long id) {
		return em.find(Reservation.class, id);
	}
	
	public Reservation find(@NotNull Long bookId, @NotNull Long userId) throws NoResultException  {
		TypedQuery<Reservation> query =  em.createQuery("select r from Reservation r "
				+ "where r.bookId = ?1 "
				+ "and r.userId = ?2", Reservation.class);
		query.setParameter(1, bookId);
		query.setParameter(2, userId);
		return query.getSingleResult();
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
