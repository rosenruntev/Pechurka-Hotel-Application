package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.repository.BookingRepository;
import eu.deltasource.internship.hotel.repository.GuestRepository;
import eu.deltasource.internship.hotel.repository.RoomRepository;
import org.junit.Test;

import java.time.LocalDate;

public class BookingServiceTest {

	@Test(expected = IllegalArgumentException.class)
	public void createBookingShouldThrowExceptionIfBookingIdIsNegative() {
		GuestRepository guestRepository = new GuestRepository();
		GuestService guestService = new GuestService(guestRepository);

		RoomRepository roomRepository = new RoomRepository();
		RoomService roomService = new RoomService(roomRepository);

		BookingRepository bookingRepository = new BookingRepository();
		BookingService bookingService = new BookingService(bookingRepository, roomService, guestService);

		LocalDate fromDate = LocalDate.of(2019, 7, 20);
		LocalDate toDate = LocalDate.of(2019, 7, 30);

		bookingService.createBooking(-1, 1, 1, 1, fromDate, toDate);
	}
}
