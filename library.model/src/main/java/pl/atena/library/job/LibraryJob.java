package pl.atena.library.job;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timer;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import pl.atena.library.DAO.RentDAO;
import pl.atena.library.mail.SendEmail;
import pl.atena.library.model.Rent;
import pl.atena.library.model.RentStatus;

@Startup
@Singleton
public class LibraryJob {

	@Inject
	private Logger log;

	@Inject
	private RentDAO rentDAO;

	@Inject
	SendEmail sendEmail;

	private String caption = "Rent for book was expired";

	private String message = "Dear user %s %s,<br/>" +
			"<br/>" +
			"your rent for the book '%s' was expired '%s'.<br/>" +
			"Please, return book as soon as is possible.<br/>" +
			"<br/>" +
			"------------<br/>" +
			"Best regards,<br/>" +
			"Java Academy Library System<br/>";

	@Schedule(second = "*/10", minute = "*", hour = "*")
	public void execute(Timer timer) {
		List<Rent> expiredRents = rentDAO.readExpiredRents();
		for (Rent rent : expiredRents) {
			log.info("===> Sending email to -> " + rent.getUser().getEmail());
			try {
				sendEmail(rent);
			} catch (AddressException e) {
				log.severe(e.getLocalizedMessage());
			} catch (MessagingException e) {
				log.severe(e.getLocalizedMessage());
			}
			rent.setStatus(RentStatus.Expired);
			rent.setSendMailDate(new Date());
			log.info("===> Updating status and send e-mail date ...");
			rentDAO.update(rent);
		}
	}

	private void sendEmail(Rent rent) throws AddressException, MessagingException {
		this.sendEmail.generateAndSendEmail(rent.getUser().getEmail(), this.caption,
				makeMessage(rent));
	}

	private String makeMessage(Rent rent) {
		return String.format(this.message, rent.getUser().getName(),
				rent.getUser().getSurname(), rent.getBook().getTitle(), rent.getEndDate());
	}

	@PostConstruct
	public void init() {
		log.info("===> Library Job -> Start...");
	}
}
