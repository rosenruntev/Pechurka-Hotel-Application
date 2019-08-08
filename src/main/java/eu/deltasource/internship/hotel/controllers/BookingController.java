package eu.deltasource.internship.hotel.controllers;

import eu.deltasource.internship.hotel.domain.Booking;
import eu.deltasource.internship.hotel.service.BookingService;
import eu.deltasource.internship.hotel.transferobject.BookingTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/bookings")
public class BookingController {

	@Autowired
	private BookingService bookingService;

	@RequestMapping(method = POST)
	public void createBooking(@RequestBody BookingTO bookingTO) {
		bookingService.createBooking(bookingTO);
	}

	@RequestMapping(method = GET)
	public List<Booking> getAllBookings() {
		return bookingService.getAllBookings();
	}

	@RequestMapping(value = "/{id}", method = GET)
	public Booking getBookingById(@PathVariable int id) {
		return bookingService.getBookingById(id);
	}

	@RequestMapping(value = "/{id}", method = PUT)
	public void updateBookingDatesById(@PathVariable int id,
									   @RequestBody BookingTO bookingTO) {
		bookingService.updateBookingDatesById(bookingTO);
	}

	@RequestMapping(value = "/{id}", method = DELETE)
	public void removeBooking(@PathVariable int id) {
		bookingService.removeBookingById(id);
	}
}
