package eu.deltasource.internship.hotel.exception;

public class InvalidItemException extends RuntimeException {
	public InvalidItemException() {
		super();
	}

	public InvalidItemException(String msg) {
		super(msg);
	}
}
