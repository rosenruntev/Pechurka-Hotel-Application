package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.repository.BookingRepository;
import eu.deltasource.internship.hotel.repository.GuestRepository;
import eu.deltasource.internship.hotel.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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
		roomService = new RoomService(roomRepository);

		guestRepository = new GuestRepository();
		guestService = new GuestService(guestRepository);

		bookingRepository = new BookingRepository();
		bookingService = new BookingService(bookingRepository, roomService, guestService);

		bookingFromDate = LocalDate.of(2019, 7, 20);
		bookingToDate = LocalDate.of(2019, 7, 25);
	}

	@Test
	public void createBookingShouldThrowExceptionIfBookingIdIsNegative() {
		assertThrows(IllegalArgumentException.class, () -> {
			bookingService.createBooking(-1, 1, 1, 1,
				bookingFromDate, bookingToDate);
		});
	}
}
