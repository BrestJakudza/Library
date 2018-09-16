package pl.atena.library.managedBeens;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import lombok.Data;
import pl.atena.library.DAO.ReservationDAO;
import pl.atena.library.model.Reservation;

@Data
@ViewScoped
@ManagedBean(name = "reservBean")
public class ReservationBean implements Serializable {
	private static final long serialVersionUID = 7800497645700553046L;

	@Inject
	private ReservationDAO reservationDAO;

	private List<Reservation> reservations;

	private List<Reservation> filteredReservations;

	private Reservation selectedReservation;

	@PostConstruct
	public void init() {
		this.reservations = reservationDAO.readAll();
		this.selectedReservation = null;
	}

	public void delete() {
		reservationDAO.delete(this.selectedReservation.getId());
		init();
	}

}
