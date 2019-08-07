package eu.deltasource.internship.hotel.exception;

/**
 * Thrown to indicate that the guest id is not valid
 */
public class InvalidGuestIdException extends RuntimeException {

	/**
	 * Constructs an InvalidGuestIdException without a detailed message
	 */
	public InvalidGuestIdException() {
	}

	/**
	 * Constructs an InvalidGuestIdException with a detailed message
	 *
	 * @param message the message of the exception
	 */
	public InvalidGuestIdException(String message) {
		super(message);
	}
}
