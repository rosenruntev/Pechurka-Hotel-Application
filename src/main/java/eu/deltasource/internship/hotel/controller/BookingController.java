package eu.deltasource.internship.hotel.controller;

import eu.deltasource.internship.hotel.domain.Booking;
import eu.deltasource.internship.hotel.repository.*;
import eu.deltasource.internship.hotel.service.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class BookingController {

	private BookingRepository bookingRepository;
	private GuestRepository guestRepository;
	private RoomRepository roomRepository;

	private BookingService bookingService;
	private GuestService guestService;
	private RoomService roomService;

	public BookingController() {
		bookingRepository = new BookingRepository();
		guestRepository = new GuestRepository();
		roomRepository = new RoomRepository();

		guestService = new GuestService(guestRepository);
		roomService = new RoomService(roomRepository);
		bookingService = new BookingService(bookingRepository, roomService, guestService);
	}

	@RequestMapping(value = "/booking", method = GET)
	public List<Booking> getAllBookings() {
		return bookingService.getAllBookings();
	}
}
