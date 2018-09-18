package pl.atena.library.managedBeens;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import lombok.Data;
import pl.atena.library.DAO.RentDAO;
import pl.atena.library.model.Rent;
import pl.atena.library.model.RentStatus;

@Data
@ViewScoped
@ManagedBean(name = "rentBean")
public class RentBean implements Serializable {
	private static final long serialVersionUID = 7800497645700553046L;

	@Inject
	private RentDAO rentDAO;

	private List<Rent> rents;

	private List<Rent> filteredRents;

	private Rent selectedRent;

	@PostConstruct
	public void init() {
		this.rents = rentDAO.readAll();
		this.selectedRent = null;
	}

	public void delete() {
		rentDAO.delete(this.selectedRent.getId());
		init();
	}

	public void returnBook() {
		if (!RentStatus.Succeeded.equals(this.selectedRent.getStatus())) {
			Rent rent = new Rent(this.selectedRent);
			rent.setStatus(RentStatus.Succeeded);
			rentDAO.update(rent);
		}
	}

}
