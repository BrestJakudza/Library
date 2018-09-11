package pl.atena.library.mail;

import java.util.Optional;
import java.util.Properties;
import java.util.logging.Logger;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Stateless
@LocalBean
public class SendEmail {
	private static Properties mailServerProperties;
	private static Session getMailSession;
	private static MimeMessage generateMailMessage;

	@Inject
	private Logger log;

	public void generateAndSendEmail(String recipient, String subject, String emailBody)
			throws AddressException, MessagingException {
		recipient = Optional.of(recipient).orElse("Jakudza@tut.by");
		subject = Optional.of(subject).orElse("Greetings from library..");
		emailBody = Optional.of(emailBody)
				.orElse("Test email by library. <br><br> Regards, <br>Library Admin");

		// 1st ===> setup Mail Server Properties..
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		log.info("===> Mail Server Properties have been setup successfully..");

		// 2nd ===> get Mail Session..
		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.addRecipient(Message.RecipientType.TO,
				new InternetAddress(recipient));
//				new InternetAddress("aleksander.lukjanczuk@atena.pl"));
		generateMailMessage.setSubject(subject);
		generateMailMessage.setContent(emailBody, "text/html");
		log.info("===> Mail Session has been created successfully..");

		// 3rd ===> Get Session and Send mail
		Transport transport = getMailSession.getTransport("smtp");

		// Enter your correct gmail UserID and Password
		// if you have 2FA enabled then provide App Specific Password
		transport.connect("smtp.gmail.com", "librarynoreplay@gmail.com", "librarynoreplay123");
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();
	}
}
