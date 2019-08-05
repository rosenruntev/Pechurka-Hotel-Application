package eu.deltasource.internship.hotel.controller;

import eu.deltasource.internship.hotel.domain.Booking;
import eu.deltasource.internship.hotel.domain.Gender;
import eu.deltasource.internship.hotel.domain.Guest;
import eu.deltasource.internship.hotel.domain.Room;
import eu.deltasource.internship.hotel.domain.commodity.AbstractCommodity;
import eu.deltasource.internship.hotel.domain.commodity.Bed;
import eu.deltasource.internship.hotel.domain.commodity.BedType;
import eu.deltasource.internship.hotel.repository.BookingRepository;
import eu.deltasource.internship.hotel.repository.GuestRepository;
import eu.deltasource.internship.hotel.repository.RoomRepository;
import eu.deltasource.internship.hotel.service.BookingService;
import eu.deltasource.internship.hotel.service.GuestService;
import eu.deltasource.internship.hotel.service.RoomService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

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

		Set<AbstractCommodity> commodities = new HashSet<>();
		commodities.add(new Bed(BedType.SINGLE));
		roomRepository.save(new Room(1, commodities));
		guestRepository.save(new Guest(1, "f", "l", Gender.MALE));
		bookingRepository.save(new Booking(1, 1, 1,
			1, LocalDate.now(), LocalDate.now().plusMonths(1)));
	}

	@RequestMapping(value = "/booking", method = POST)
	public void createBooking(@RequestBody Booking booking) {
		bookingService.createBooking(booking.getGuestId(), booking.getNumberOfPeople(),
			booking.getFrom(), booking.getTo());
	}

	@RequestMapping(value = "/booking", method = GET)
	public List<Booking> getAllBookings() {
		return bookingService.getAllBookings();
	}
}
