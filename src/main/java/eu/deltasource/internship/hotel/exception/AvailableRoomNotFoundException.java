package eu.deltasource.internship.hotel.exception;

/**
 * Thrown to indicate that there is not any available room
 */
public class AvailableRoomNotFoundException extends RuntimeException {

	/**
	 * Constructs an AvailableRoomNotFoundException without a detailed message
	 */
	public AvailableRoomNotFoundException() {
	}

	/**
	 * Constructs an AvailableRoomNotFoundException with a detailed message
	 *
	 * @param message the message of the exception
	 */
	public AvailableRoomNotFoundException(String message) {
		super(message);
	}
}
