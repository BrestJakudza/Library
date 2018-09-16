package pl.atena.library.model;

public enum MsgStatus {
	Ok("all good"),
	Warning("was warnings"),
	Error("somthing was wrong");

	private final String CODE;

	MsgStatus(String code) {
		this.CODE = code;
	}

	public String getStatusInfo() {
		return this.CODE;
	}
}
