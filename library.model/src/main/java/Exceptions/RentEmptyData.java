package Exceptions;

public class RentEmptyData extends Exception {
	private static final long serialVersionUID = -8003384765124367393L;

	public RentEmptyData() {
		super();
	}

	public RentEmptyData(String msg) {
		super(msg);
	}
}
