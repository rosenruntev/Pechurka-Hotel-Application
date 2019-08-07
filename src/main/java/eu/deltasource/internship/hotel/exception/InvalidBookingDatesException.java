package eu.deltasource.internship.hotel.exception;

/**
 * Thrown to indicate that the booking dates are invalid
 */
public class InvalidBookingDatesException extends RuntimeException {

	/**
	 * Constructs an InvalidBookingDatesException without a detailed message
	 */
	public InvalidBookingDatesException() {
	}

	/**
	 * Constructs an InvalidBookingDatesException with a detailed message
	 *
	 * @param message the message of the exception
	 */
	public InvalidBookingDatesException(String message) {
		super(message);
	}
}
