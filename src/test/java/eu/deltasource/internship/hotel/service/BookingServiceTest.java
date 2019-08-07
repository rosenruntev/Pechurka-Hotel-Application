package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Booking;
import eu.deltasource.internship.hotel.domain.Gender;
import eu.deltasource.internship.hotel.domain.Guest;
import eu.deltasource.internship.hotel.domain.Room;
import eu.deltasource.internship.hotel.domain.commodity.AbstractCommodity;
import eu.deltasource.internship.hotel.domain.commodity.Bed;
import eu.deltasource.internship.hotel.domain.commodity.BedType;
import eu.deltasource.internship.hotel.exception.AvailableRoomNotFoundException;
import eu.deltasource.internship.hotel.exception.InvalidBookingDatesException;
import eu.deltasource.internship.hotel.exception.InvalidGuestIdException;
import eu.deltasource.internship.hotel.repository.BookingRepository;
import eu.deltasource.internship.hotel.repository.GuestRepository;
import eu.deltasource.internship.hotel.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BookingServiceTest {

	private RoomRepository roomRepository;
	private GuestRepository guestRepository;
	private BookingRepository bookingRepository;

	private RoomService roomService;
	private GuestService guestService;
	private BookingService bookingService;

	private LocalDate today;
	private LocalDate afterFiveDays;

	@BeforeEach
	public void setUp() {
		roomRepository = new RoomRepository();
		guestRepository = new GuestRepository();
		bookingRepository = new BookingRepository();

		roomService = new RoomService(roomRepository);
		guestService = new GuestService(guestRepository);
		bookingService = new BookingService(bookingRepository, roomService, guestService);

		today = LocalDate.now();
		afterFiveDays = today.plusDays(5);
	}

	private void addSimpleRoomWithCommodity() {
		Set<AbstractCommodity> commodities = new HashSet<>();
		Bed bed = new Bed(BedType.SINGLE);
		commodities.add(bed);
		roomService.saveRoom(new Room(1, commodities));
	}

	private void addSimpleGuest() {
		// TODO: use GuestService to add the guest after merge
		Guest guest = new Guest(1, "first name", "last name", Gender.MALE);
		guestRepository.save(guest);
	}

	@Test
	public void createBookingShouldThrowExceptionIfGuestIdIsInvalid() {
		// Given
		// When
		// Then
		assertThrows(InvalidGuestIdException.class, () -> {
			bookingService.createBooking(-1, 1, today, afterFiveDays);
		});
	}

	@Test
	public void createBookingShouldThrowExceptionIfGuestDoesNotExist() {
		// Given
		// When
		// Then
		assertThrows(InvalidGuestIdException.class, () -> {
			bookingService.createBooking(1, 1, today, afterFiveDays);
		});
	}

	@Test
	public void createBookingShouldThrowExceptionIfDatesAreEqual() {
		// Given
		addSimpleRoomWithCommodity();
		addSimpleGuest();

		// When
		// Then
		assertThrows(InvalidBookingDatesException.class, () -> {
			bookingService.createBooking(1, 1, today, today);
		});
	}

	@Test
	public void createBookingShouldThrowExceptionIfFromDateIsAfterToDate() {
		// Given
		addSimpleRoomWithCommodity();
		addSimpleGuest();

		// When
		// Then
		assertThrows(InvalidBookingDatesException.class, () -> {
			bookingService.createBooking(1, 1, afterFiveDays, today);
		});
	}

	@Test
	public void createBookingShouldThrowExceptionIfThereAreNotAvailableRooms() {
		// Given
		addSimpleRoomWithCommodity();
		addSimpleGuest();

		// When
		bookingService.createBooking(1, 1, today, afterFiveDays);

		// Then
		assertThrows(AvailableRoomNotFoundException.class, () -> {
			bookingService.createBooking(1, 1, today, afterFiveDays);
		});
	}

	@Test
	public void createBookingShouldCreateNewBookingInBookingRepository() {
		// Given
		addSimpleRoomWithCommodity();
		addSimpleGuest();

		// When
		bookingService.createBooking(1, 1, today, afterFiveDays);

		// Then
		assertEquals(true, bookingService.existsById(1));
		Booking booking = bookingService.getBookingById(1);
		assertEquals(1, booking.getGuestId());
		assertEquals(1, booking.getRoomId());
		assertEquals(1, booking.getNumberOfPeople());
		assertEquals(today, booking.getFrom());
		assertEquals(afterFiveDays, booking.getTo());
	}

	@Test
	public void getAllBookingsShouldReturnListWithAllBookings() {
		// Given
		addSimpleRoomWithCommodity();
		addSimpleGuest();
		bookingService.createBooking(1, 1,
			today, afterFiveDays);

		LocalDate anotherFromDate = today.plusDays(10);
		LocalDate anotherToDate = today.plusDays(20);
		bookingService.createBooking(1, 1,
			anotherFromDate, anotherToDate);

		// When
		List<Booking> bookings = bookingService.getAllBookings();

		// Then
		assertEquals(2, bookings.size());
		assertEquals(true, bookingService.existsById(1));
		assertEquals(true, bookingService.existsById(2));
	}

	@Test
	public void getBookingByIdShouldReturnTheBookingWithParticularId() {
		// Given
		addSimpleRoomWithCommodity();
		addSimpleGuest();
		bookingService.createBooking(1, 1, today, afterFiveDays);

		// When
		Booking booking = bookingService.getBookingById(1);

		// Then
		assertEquals(1, booking.getBookingId());
		assertEquals(1, booking.getGuestId());
		assertEquals(1, booking.getNumberOfPeople());
		assertEquals(today, booking.getFrom());
		assertEquals(afterFiveDays, booking.getTo());
	}

	@Test
	public void updateBookingDatesByIdShouldChangeBookingDates() {
		// Given
		addSimpleRoomWithCommodity();
		addSimpleGuest();
		LocalDate afterTenDays = today.plusDays(10);
		LocalDate afterFifteenDays = today.plusDays(15);
		LocalDate afterTwentyDays = today.plusDays(20);
		LocalDate afterTwentyFiveDays = today.plusDays(25);

		bookingService.createBooking(1, 1,
			afterTenDays, afterFifteenDays);

		// When
		bookingService.updateBookingDatesById(1, 1,
			today, afterFiveDays);

		bookingService.updateBookingDatesById(1, 1,
			afterTwentyDays, afterTwentyFiveDays);

		// Then
		Booking booking = bookingService.getBookingById(1);
		assertEquals(afterTwentyDays, booking.getFrom());
		assertEquals(afterTwentyFiveDays, booking.getTo());
	}

	@Test
	public void updateBookingShouldBeAbleToUpdateDatesThatOverlapWithPreviousDates() {
		// Given
		addSimpleRoomWithCommodity();
		addSimpleGuest();

		bookingService.createBooking(1, 1,
			today, afterFiveDays);

		// When
		LocalDate newFromDate = today.plusDays(1);
		LocalDate newToDate = LocalDate.now().plusDays(4);
		bookingService.updateBookingDatesById(1, 1, newFromDate, newToDate);

		// Then
		Booking booking = bookingService.getBookingById(1);
		assertEquals(newFromDate, booking.getFrom());
		assertEquals(newToDate, booking.getTo());
	}

	@Test
	public void removeBookingByIdShouldRemoveTheBooking() {
		// Given
		addSimpleRoomWithCommodity();
		addSimpleGuest();

		bookingService.createBooking(1, 1,
			today, afterFiveDays);

		// When
		bookingService.removeBookingById(1);

		// Then
		assertEquals(false, bookingService.existsById(1));
		assertEquals(0, bookingRepository.count());
	}
}
