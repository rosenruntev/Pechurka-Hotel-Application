package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Booking;
import eu.deltasource.internship.hotel.domain.Gender;
import eu.deltasource.internship.hotel.domain.Guest;
import eu.deltasource.internship.hotel.domain.Room;
import eu.deltasource.internship.hotel.domain.commodity.AbstractCommodity;
import eu.deltasource.internship.hotel.domain.commodity.Bed;
import eu.deltasource.internship.hotel.domain.commodity.BedType;
import eu.deltasource.internship.hotel.exception.ItemNotFoundException;
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
	private RoomService roomService;
	private GuestRepository guestRepository;
	private GuestService guestService;
	private BookingRepository bookingRepository;
	private BookingService bookingService;
	private LocalDate bookingFromDate;
	private LocalDate bookingToDate;

	@BeforeEach
	public void setUp() {
		roomRepository = new RoomRepository();
		guestRepository = new GuestRepository();
		bookingRepository = new BookingRepository();

		roomService = new RoomService(roomRepository);
		guestService = new GuestService(guestRepository);
		bookingService = new BookingService(bookingRepository, roomService, guestService);

		bookingFromDate = LocalDate.now();
		bookingToDate = bookingFromDate.plusDays(5);
	}

	@Test
	public void createBookingShouldThrowExceptionIfBookingIdIsNegative() {
		// Then
		assertThrows(IllegalArgumentException.class, () -> {
			// When
			bookingService.createBooking(-1, 1, 1, 1,
				bookingFromDate, bookingToDate);
		});
	}

	@Test
	public void createBookingShouldThrowExceptionIfBookingIdAlreadyExists() {
		// Given
		// TODO: use GuestService to add the guest after merge
		guestRepository.save(new Guest(1, "first name", "last name", Gender.MALE));

		Set<AbstractCommodity> commodities = new HashSet<>();
		commodities.add(new Bed(BedType.SINGLE));
		roomService.saveRoom(new Room(1, commodities));

		bookingService.createBooking(1, 1, 1, 1, bookingFromDate,
			bookingToDate);

		// Then
		assertThrows(IllegalArgumentException.class, () -> {
			// When
			bookingService.createBooking(1, 1, 1, 1,
				LocalDate.now(), LocalDate.now().plusDays(1));
		});
	}

	@Test
	public void createBookingShouldThrowExceptionIfGuestDoesNotExist() {
		// Given
		Set<AbstractCommodity> commodities = new HashSet<>();
		commodities.add(new Bed(BedType.SINGLE));
		roomService.saveRoom(new Room(1, commodities));

		// Then
		assertThrows(IllegalArgumentException.class, () -> {
			// When
			bookingService.createBooking(1, 1, 1, 1,
				bookingFromDate, bookingToDate);
		});
	}

	@Test
	public void createBookingShouldThrowExceptionIfRoomDoesNotExist() {
		// Given
		// TODO: use GuestService to add the guest after merge
		guestRepository.save(new Guest(1, "first name", "last name", Gender.MALE));

		// Then
		assertThrows(IllegalArgumentException.class, () -> {
			// When
			bookingService.createBooking(1, 1, 1, 1,
				bookingFromDate, bookingToDate);
		});
	}

	@Test
	public void createBookingShouldThrowExceptionIfRoomHasDifferentCapacity() {
		// Given
		// TODO: use GuestService to add the guest after merge
		guestRepository.save(new Guest(1, "first name", "last name", Gender.MALE));

		Set<AbstractCommodity> commodities = new HashSet();
		commodities.add(new Bed(BedType.DOUBLE));
		roomService.saveRoom(new Room(1, commodities));

		// Then
		assertThrows(IllegalArgumentException.class, () -> {
			// When
			bookingService.createBooking(1, 1, 1, 1,
				bookingFromDate, bookingToDate);
		});
	}

	@Test
	public void createBookingShouldThrowExceptionIfDatesAreEqual() {
		// Given
		// TODO: use GuestService to add the guest after merge
		guestRepository.save(new Guest(1, "first name", "last name", Gender.MALE));

		Set<AbstractCommodity> commodities = new HashSet();
		commodities.add(new Bed(BedType.DOUBLE));
		roomService.saveRoom(new Room(1, commodities));

		// Then
		assertThrows(IllegalArgumentException.class, () -> {
			// When
			bookingService.createBooking(1, 1, 1, 2,
				LocalDate.now(), LocalDate.now());
		});
	}

	@Test
	public void createBookingShouldThrowExceptionIfFromDateIsAfterToDate() {
		// Given
		// TODO: use GuestService to add the guest after merge
		guestRepository.save(new Guest(1, "first name", "last name", Gender.MALE));

		Set<AbstractCommodity> commodities = new HashSet();
		commodities.add(new Bed(BedType.DOUBLE));
		roomService.saveRoom(new Room(1, commodities));

		// Then
		assertThrows(IllegalArgumentException.class, () -> {
			// When
			bookingService.createBooking(1, 1, 1, 2,
				LocalDate.now().plusDays(1), LocalDate.now());
		});
	}

	@Test
	public void createBookingShouldThrowExceptionIfRoomIsBookedForThatPeriod() {
		// Given
		// TODO: use GuestService to add the guest after merge
		guestRepository.save(new Guest(1, "first name", "last name", Gender.MALE));

		Set<AbstractCommodity> commodities = new HashSet();
		commodities.add(new Bed(BedType.DOUBLE));
		roomService.saveRoom(new Room(1, commodities));

		bookingService.createBooking(1, 1, 1, 2,
			bookingFromDate, bookingToDate);

		// Then
		assertThrows(IllegalArgumentException.class, () -> {
			// When
			bookingService.createBooking(2, 1, 1, 2,
				bookingFromDate, bookingToDate);
		});
	}

	@Test
	public void createBookingShouldCreateNewBookingInBookingRepository() {
		// Given
		// TODO: use GuestService to add the guest after merge
		guestRepository.save(new Guest(1, "first name", "last name", Gender.MALE));

		Set<AbstractCommodity> commodities = new HashSet();
		commodities.add(new Bed(BedType.DOUBLE));
		roomService.saveRoom(new Room(1, commodities));

		// When
		bookingService.createBooking(1, 1, 1, 2,
			bookingFromDate, bookingToDate);

		// Then
		assertEquals(true, bookingService.existsById(1));
	}

	@Test
	public void getAllBookingsShouldReturnListWithAllBookings() {
		// Given
		// TODO: use GuestService to add the guest after merge
		guestRepository.save(new Guest(1, "first name", "last name", Gender.MALE));

		Set<AbstractCommodity> commodities = new HashSet();
		commodities.add(new Bed(BedType.DOUBLE));
		roomService.saveRoom(new Room(1, commodities));

		bookingService.createBooking(1, 1, 1, 2,
			bookingFromDate, bookingToDate);

		// When
		List<Booking> bookings = bookingService.getAllBookings();

		// Then
		assertEquals(1, bookings.size());
	}

	@Test
	public void getBookingByIdShouldReturnTheBookingWithParticularId() {
		// Given
		// TODO: use GuestService to add the guest after merge
		guestRepository.save(new Guest(1, "first name", "last name", Gender.MALE));

		Set<AbstractCommodity> commodities = new HashSet();
		commodities.add(new Bed(BedType.DOUBLE));
		roomService.saveRoom(new Room(1, commodities));

		bookingService.createBooking(1, 1, 1, 2,
			bookingFromDate, bookingToDate);

		// When
		Booking booking = bookingService.getBookingById(1);

		// Then
		assertEquals(1, booking.getBookingId());
	}

	@Test
	public void updateBookingDatesByIdShouldChangeBookingDates() {
		// Given
		// TODO: use GuestService to add the guest after merge
		guestRepository.save(new Guest(1, "first name", "last name", Gender.MALE));

		Set<AbstractCommodity> commodities = new HashSet();
		commodities.add(new Bed(BedType.DOUBLE));
		roomService.saveRoom(new Room(1, commodities));

		bookingService.createBooking(1, 1, 1, 2,
			bookingFromDate, bookingToDate);

		// When
		LocalDate newFromDate = bookingFromDate.plusDays(20);
		LocalDate newToDate = bookingToDate.plusDays(20);
		bookingService.updateBookingDatesById(1, newFromDate, newToDate);

		// Then
		assertEquals(newFromDate, bookingService.getBookingById(1).getFrom());
		assertEquals(newToDate, bookingService.getBookingById(1).getTo());
	}

	@Test
	public void removeBookingByIdShouldRemoveTheBooking() {
		// Given
		// TODO: use GuestService to add the guest after merge
		guestRepository.save(new Guest(1, "first name", "last name", Gender.MALE));

		Set<AbstractCommodity> commodities = new HashSet();
		commodities.add(new Bed(BedType.DOUBLE));
		roomService.saveRoom(new Room(1, commodities));

		bookingService.createBooking(1, 1, 1, 2,
			bookingFromDate, bookingToDate);

		// When
		bookingService.removeBookingById(1);

		// Then
		assertEquals(false, bookingService.existsById(1));
	}
}
