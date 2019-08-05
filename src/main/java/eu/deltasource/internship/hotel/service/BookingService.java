package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Booking;
import eu.deltasource.internship.hotel.domain.Room;
import eu.deltasource.internship.hotel.repository.BookingRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * A class that represents service for bookings
 */
public class BookingService {

	private final BookingRepository bookingRepository;

	private final RoomService roomService;

	private final GuestService guestService;

	/**
	 * Constructs a booking service with given booking repository, room service and guest service
	 *
	 * @param bookingRepository the booking repository
	 * @param roomService       the room service
	 * @param guestService      the guest service
	 */
	public BookingService(BookingRepository bookingRepository, RoomService roomService, GuestService guestService) {
		this.bookingRepository = bookingRepository;
		this.roomService = roomService;
		this.guestService = guestService;
	}

	/**
	 * Creates a booking with given booking id, guest id, room id, number of people and dates
	 *
	 * @param bookingId      the booking id
	 * @param guestId        the guest id
	 * @param roomId         the room id
	 * @param numberOfPeople the room's capacity
	 * @param fromDate       the date of accommodation
	 * @param toDate         the date of leaving
	 */
	public void createBooking(int bookingId, int guestId, int roomId, int numberOfPeople, LocalDate fromDate, LocalDate toDate) {
		if (existsById(bookingId)) {
			throw new IllegalArgumentException("Booking with id " + bookingId + " already exists.");
		}

		validateGuestId(guestId);
		validateRoomId(roomId);
		Room room = roomService.getRoomById(roomId);
		if (room.getRoomCapacity() != numberOfPeople) {
			throw new IllegalArgumentException("Number of people must be equal to room capacity.");
		}

		validateDates(fromDate, toDate);
		if (isRoomBookedForPeriod(roomId, fromDate, toDate)) {
			throw new IllegalArgumentException("Room is already booked for that period.");
		} else {
			Booking newBooking = new Booking(bookingId, guestId, roomId, numberOfPeople, fromDate, toDate);
			bookingRepository.save(newBooking);
		}
	}

	/**
	 * Returns all bookings in the repository as unmodifiable list
	 *
	 * @return all bookings in the repository as unmodifiable list
	 */
	public List<Booking> getAllBookings() {
		return bookingRepository.findAll();
	}

	/**
	 * Returns a booking with particular id
	 *
	 * @param bookingId the id of the booking
	 * @return a booking with particular id
	 */
	public Booking getBookingById(int bookingId) {
		validateBookingId(bookingId);
		return bookingRepository.findById(bookingId);
	}

	/**
	 * Updates dates of booking with particular id
	 *
	 * @param bookingId       the id of the booking
	 * @param fromDate the date of accommodation
	 * @param toDate   the date of leaving
	 */
	public void updateBookingDatesById(int bookingId, int guestId, LocalDate fromDate, LocalDate toDate) {
		Booking booking = getBookingById(bookingId);
		if (booking.getGuestId() != guestId) {
			throw new IllegalArgumentException("Guest with id " + guestId + " is not the booking's guest");
		}
		validateDates(fromDate, toDate);

		// Check if we are trying to set dates that overlap with current booking dates
		// if we have booking like 2019.8.1 - 2019.8.5 to be able to set dates like 2019.8.2 - 2019.8.4
		// if they overlap, the booking dates will be changed otherwise isRoomBookedForPeriod will be called
		if (!areDatesOverlapping(booking.getFrom(), booking.getTo(), fromDate, toDate)) {
			if (isRoomBookedForPeriod(bookingId, fromDate, toDate)) {
				throw new IllegalArgumentException("Room is already booked for that period.");
			}
		}

		booking.setBookingDates(fromDate, toDate);
		bookingRepository.updateDates(booking);
	}

	/**
	 * Removes a booking with particular id
	 *
	 * @param bookingId the id of the booking
	 */
	public void removeBookingById(int bookingId) {
		if (!existsById(bookingId)) {
			throw new IllegalArgumentException("Booking with id " + bookingId + " does not exists.");
		}

		bookingRepository.deleteById(bookingId);
	}

	/**
	 * Checks if booking with particular id exists
	 *
	 * @param bookingId the id of the booking
	 * @return true if the booking with particular id exists otherwise false
	 */
	public boolean existsById(int bookingId) {
		validateBookingId(bookingId);
		return bookingRepository.existsById(bookingId);
	}

	private void validateBookingId(int bookingId) {
		if (bookingId <= 0) {
			throw new IllegalArgumentException("Booking id cannot be a negative number or zero.");
		}
	}

	private void validateGuestId(int guestId) {
		if (guestId <= 0) {
			throw new IllegalArgumentException("Guest id cannot be a negative number or zero.");
		} else if (!guestService.existsById(guestId)) {
			throw new IllegalArgumentException("Guest with id " + guestId + " does not exist.");
		}
	}

	private void validateRoomId(int roomId) {
		if (roomId <= 0) {
			throw new IllegalArgumentException("Room id cannot be a negative number or zero.");
		}
		if (!roomService.existsById(roomId)) {
			throw new IllegalArgumentException("Room with id " + roomId + " does not exist.");
		}
	}

	private void validateDates(LocalDate fromDate, LocalDate toDate) {
		if (fromDate == null || toDate == null) {
			throw new IllegalArgumentException("Dates cannot be null.");
		} else if (fromDate.equals(toDate) || fromDate.isAfter(toDate)) {
			throw new IllegalArgumentException("From date cannot be same as to date or be after to date");
		}
	}

	private boolean isRoomBookedForPeriod(int roomId, LocalDate fromDate, LocalDate toDate) {
		for (Booking booking : getAllBookings()) {
			if (booking.getRoomId() == roomId) {
				if (areDatesOverlapping(booking.getFrom(), booking.getTo(), fromDate, toDate)) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean areDatesOverlapping(LocalDate bookingFromDate, LocalDate bookingToDate,
										LocalDate requestedFromDate, LocalDate requestedToDate) {
		// Checks if booking dates are before or after requested dates
		if (bookingToDate.isBefore(requestedFromDate) || bookingFromDate.isAfter(requestedToDate)) {
			return false;
		}

		return true;
	}
}
