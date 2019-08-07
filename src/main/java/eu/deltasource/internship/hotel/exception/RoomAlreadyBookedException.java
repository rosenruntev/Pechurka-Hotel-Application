package eu.deltasource.internship.hotel.exception;

/**
 * Thrown to indicate that the room is already booked
 */
public class RoomAlreadyBookedException extends RuntimeException {

	/**
	 * Constructs a RoomAlreadyBookedException without a detailed message
	 */
	public RoomAlreadyBookedException() {
	}

	/**
	 * Constructs a RoomAlreadyBookedException with a detailed message
	 *
	 * @param message the message of the exception
	 */
	public RoomAlreadyBookedException(String message) {
		super(message);
	}
}
