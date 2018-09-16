package Exceptions;

public class RentExistException extends Exception {
	private static final long serialVersionUID = 2066388442666120270L;

	public RentExistException() {
		super();
	}

	public RentExistException(String msg) {
		super(msg);
	}
}
