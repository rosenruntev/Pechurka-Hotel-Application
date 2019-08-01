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
		validateBookingId(bookingId);
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
			bookingRepository.save(new Booking(bookingId, guestId, roomId, numberOfPeople, fromDate, toDate));
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
	 * @param id the id of the booking
	 * @return a booking with particular id
	 */
	public Booking getBookingById(int id) {
		validateBookingId(id);
		checkIfBookingWithIdDoesNotExists(id);

		return bookingRepository.findById(id);
	}

	/**
	 * Updates dates of booking with particular id
	 *
	 * @param id       the id of the booking
	 * @param fromDate the date of accommodation
	 * @param toDate   the date of leaving
	 */
	public void updateBookingDatesById(int id, LocalDate fromDate, LocalDate toDate) {
		validateBookingId(id);
		checkIfBookingWithIdDoesNotExists(id);
		validateDates(fromDate, toDate);
		if (isRoomBookedForPeriod(id, fromDate, toDate)) {
			throw new IllegalArgumentException("Room is already booked for that period.");
		}

		Booking booking = getBookingById(id);
		Booking bookingWithChangedDates = new Booking(id, booking.getGuestId(),
			booking.getRoomId(), booking.getNumberOfPeople(), fromDate, toDate);
		bookingRepository.updateDates(bookingWithChangedDates);
	}

	/**
	 * Removes a booking with particular id
	 *
	 * @param id the id of the booking
	 */
	public void removeBookingById(int id) {
		validateBookingId(id);
		checkIfBookingWithIdDoesNotExists(id);

		bookingRepository.deleteById(id);
	}

	/**
	 * Checks if booking with particular id exists
	 *
	 * @param id the id of the booking
	 * @return true if the booking with particular id exists otherwise false
	 */
	public boolean existsById(int id) {
		validateBookingId(id);
		return bookingRepository.existsById(id);
	}

	private void validateBookingId(int bookingId) {
		if (bookingId <= 0) {
			throw new IllegalArgumentException("Booking id cannot be a negative number or zero.");
		}
	}

	private void checkIfBookingWithIdDoesNotExists(int id) {
		if (!existsById(id)) {
			throw new IllegalArgumentException("Booking with id " + id + " does not exists.");
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
		List<Booking> bookings = bookingRepository.findAll();
		for (Booking booking : bookings) {
			if (booking.getRoomId() == roomId) {
				if (!(booking.getTo().isBefore(fromDate) || booking.getFrom().isAfter(toDate))) {
					return true;
				}
			}
		}

		return false;
	}
}
