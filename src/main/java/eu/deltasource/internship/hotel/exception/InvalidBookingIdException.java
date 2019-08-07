package eu.deltasource.internship.hotel.exception;

/**
 * Thrown to indicate that the booking id is not valid
 */
public class InvalidBookingIdException extends RuntimeException {

	/**
	 * Constructs an InvalidBookingIdException without a detailed message
	 */
	public InvalidBookingIdException() {
	}

	/**
	 * Constructs an InvalidBookingIdException with a detailed message
	 *
	 * @param message the message of the exception
	 */
	public InvalidBookingIdException(String message) {
		super(message);
	}
}
