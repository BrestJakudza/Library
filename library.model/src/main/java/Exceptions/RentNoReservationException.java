package Exceptions;

public class RentNoReservationException extends Exception {
	private static final long serialVersionUID = -1290017879768041339L;

	public RentNoReservationException() {
		super();
	}

	public RentNoReservationException(String msg) {
		super(msg);
	}
}
