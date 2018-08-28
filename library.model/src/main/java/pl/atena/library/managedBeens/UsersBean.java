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

import pl.atena.library.DAO.UserDAO;
import pl.atena.library.dataGenerators.DataGenerator;
import pl.atena.library.mail.SendEmail;
import pl.atena.library.model.User;


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
//		this.selectedBook = new Book();
		this.create = false;
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
			userDAO.create(newUser);
			init();
			sendEmail.generateAndSendEmail();
		} else {
			userDAO.update(selectedUser);
		}
	}

	public void delete() {
		userDAO.delete(this.selectedUser.getId());
		init();
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<User> getFilteredUsers() {
		return filteredUsers;
	}

	public void setFilteredUsers(List<User> filteredUsers) {
		this.filteredUsers = filteredUsers;
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

}
