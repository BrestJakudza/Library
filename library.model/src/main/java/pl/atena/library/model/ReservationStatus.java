package pl.atena.library.model;

public enum ReservationStatus {
	Queue("in queue"),
	Inprogress("in progress"),
	Succeeded("succeeded"),
	Canceled("canceled"),
	NotSucceeded("not succeeded"),
	ExpiredRejected("rejected due to expired reservation"),
	InabilityRejected("rejected due to the inability to make a reservation");

	private final String CODE;

	ReservationStatus(String code) {
		this.CODE = code;
	}

	public String getStatusInfo() {
		return this.CODE;
	}

}
