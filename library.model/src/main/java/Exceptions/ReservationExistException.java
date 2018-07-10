package Exceptions;

public class ReservationExistException extends Exception {

	private static final long serialVersionUID = 1L;

	public ReservationExistException() {
		super();
	}

	public ReservationExistException(String message) {
		super(message);
	}

}
