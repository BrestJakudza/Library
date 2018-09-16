package pl.atena.library.managedBeens;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import lombok.Data;
import pl.atena.library.DAO.UserDAO;
import pl.atena.library.dataGenerators.DataGenerator;
import pl.atena.library.mail.SendEmail;
import pl.atena.library.model.User;

@Data
@ViewScoped
@ManagedBean(name = "usersBean")
public class UsersBean implements Serializable {

	private static final long serialVersionUID = -2652285433806568195L;

	@Inject
	private Logger log;

	@Inject
	private UserDAO userDAO;
	
	@Inject
	private DataGenerator dataGenerator;
	
	@Inject
	private SendEmail sendEmail;
	
	private List<User> users;

	private List<User> filteredUsers;

	private User selectedUser;
	private boolean create;

	@PostConstruct
	public void init() {
		this.users = userDAO.readAllUsers();
		this.selectedUser = null;
		this.create = false;
	}
	
	public void sendEmail() {
		try {
			sendEmail.generateAndSendEmail(null, null, null);
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void create() {
		this.selectedUser = dataGenerator.generateTestUser();
		this.create = true;
	}

	public void save() throws AddressException, MessagingException {
		if (this.create) {
			User newUser = new User();
			newUser.setId(this.selectedUser.getId());
			newUser.setName(this.selectedUser.getName());
			newUser.setSurname(this.selectedUser.getSurname());
			newUser.setEmail(this.selectedUser.getEmail());
			userDAO.create(newUser);
			init();
		} else {
			userDAO.update(selectedUser);
		}
	}

	public void delete() {
		userDAO.delete(this.selectedUser.getId());
		init();
	}
}
