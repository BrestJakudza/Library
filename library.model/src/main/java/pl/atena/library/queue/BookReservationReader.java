package pl.atena.library.queue;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import pl.atena.library.DAO.ReservationDAO;
import pl.atena.library.dto.ReservationDTO;
import pl.atena.library.model.Reservation;
import pl.atena.library.utils.ReservationUtils;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/BookReservationQueue"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
		@ActivationConfigProperty(propertyName = "book", propertyValue = "reservation") })

@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class BookReservationReader implements MessageListener {

	@Inject
	Logger log;
	
	@Inject
	ReservationDAO reservationDAO;
	
	@Inject
	private ReservationUtils reservationUtils;

	@Override
	public void onMessage(Message message) {
		try {
			ObjectMessage objMessage = (ObjectMessage) message;
			ReservationDTO reservationDTO = (ReservationDTO) objMessage.getObject();
			log.info("Reservation readed from queue:" + reservationDTO);
			
			Reservation reservation = reservationDTO.getReservation();
			reservation.setStatus(reservationUtils.getReservationStatus(reservation));
			reservationDAO.update(reservation);
		} catch (JMSException e) {
			log.severe("Somthing was wrong: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

}
