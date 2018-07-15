package pl.atena.library.model;

public enum RentStatus {
	Inprogress("in progress"),
	Succeeded("succeeded"),
	NotSucceeded("not succeeded"),
	Expired("expired reservation");

	private final String CODE;

	RentStatus(String code) {
		this.CODE = code;
	}

	public String getStatusInfo() {
		return this.CODE;
	}
}
