package pl.atena.library.queue;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
/*import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;*/
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import pl.atena.library.dto.ReservationDTO;

@ApplicationScoped
public class BookReservationSender {

	@Inject
	Logger log;

	@Resource(mappedName = "java:/jms/queue/BookReservationQueue")
	private Queue queue;

	@Resource(mappedName = "java:/JmsXA")
	private ConnectionFactory connectionFactory;

	public void sender(ReservationDTO reservation) {
		Connection connection = null;
		MessageProducer publisher = null;
		Session session = null;
		try {
			log.info("Book reservation MQ: " + reservation);
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			publisher = session.createProducer(queue);
			connection.start();
			ObjectMessage message = session.createObjectMessage();
			message.setObjectProperty("book", "reservation");
			message.setObject(reservation);
			publisher.send(message);
			log.info("Book reservation sended");
		} catch (JMSException exc) {
			exc.printStackTrace();
		} finally {
			if (publisher != null)
				try {
					publisher.close();
				} catch (Exception e) {
					log.severe("Error on publisher close: " + e.getLocalizedMessage());
				}
			if (session != null)
				try {
					session.close();
				} catch (Exception e) {
					log.severe("Error on session slose: " + e.getLocalizedMessage());
				}
			if (connection != null)
				try {
					connection.close();
				} catch (Exception e) {
					log.severe("Error on connection close: " + e.getLocalizedMessage());
				}
		}
	}
}
