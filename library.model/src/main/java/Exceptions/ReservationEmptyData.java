package Exceptions;

public class ReservationEmptyData extends Exception {
	private static final long serialVersionUID = 8942714635516074768L;

	public ReservationEmptyData() {
		super();
	}

	public ReservationEmptyData(String msg) {
		super(msg);
	}

}
