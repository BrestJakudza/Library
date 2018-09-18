package pl.atena.library.managedBeens;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import Exceptions.RentExistException;
import Exceptions.RentNoReservationException;
import lombok.Data;
import pl.atena.library.DAO.ReservationDAO;
import pl.atena.library.dto.RentWS;
import pl.atena.library.model.Rent;
import pl.atena.library.model.Reservation;
import pl.atena.library.utils.Growl;

@Data
@ViewScoped
@ManagedBean(name = "reservBean")
public class ReservationBean implements Serializable {
	private static final long serialVersionUID = 7800497645700553046L;

	@Inject
	private ReservationDAO reservationDAO;

	@Inject
	private RentManager rentManager;

	private List<Reservation> reservations;

	private List<Reservation> filteredReservations;

	private Reservation selectedReservation;
	private Rent rent;

	@PostConstruct
	public void init() {
		this.reservations = reservationDAO.readAll();
		this.selectedReservation = null;
		this.rent = null;
	}

	public void createRent() {
		this.rent = new Rent(null, this.selectedReservation.getUser(),
				this.selectedReservation.getBook(), new Date(), new Date(), null, null);
	}

	public void cancelRent() {
		this.rent = null;
	}

	public void delete() {
		reservationDAO.delete(this.selectedReservation.getId());
		init();
	}

	public void makeRent() {
		RentWS rentWS = new RentWS(this.selectedReservation.getId(),
				this.selectedReservation.getUser().getId(), this.rent.getStartDate(),
				this.rent.getEndDate());
		try {
			rentManager.makeRent(rentWS);
			init();
		} catch (RentNoReservationException e) {
			Growl.showMsg(FacesMessage.SEVERITY_ERROR, "NoReservation", e.getLocalizedMessage());
		} catch (RentExistException e) {
			Growl.showMsg(FacesMessage.SEVERITY_ERROR, "RentExists", e.getLocalizedMessage());
		}
	}

}
