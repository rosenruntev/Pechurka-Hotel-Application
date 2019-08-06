package eu.deltasource.internship.hotel.controllers;

import eu.deltasource.internship.hotel.domain.Guest;
import eu.deltasource.internship.hotel.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("GuestService/")
public class GuestController {
	private GuestService guestService;

	@Autowired
	public GuestController(GuestService guestService) {
		this.guestService = guestService;
	}

	@GetMapping(value = "findGuest/{id}")
	public Guest findGuest(@PathVariable int id) {
		return guestService.getGuestById(id);
	}

	@GetMapping(value = "findAllGuests/")
	public List<Guest> findAllGuests() {
		return guestService.getAllGuests();
	}

	@DeleteMapping(value = "removeGuest/{id}")
	public boolean removeGuestById(@PathVariable int id) {
		return guestService.removeGuestById(id);
	}

	@PutMapping(value = "updateGuest/", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Guest updateGuest(@RequestBody Guest newGuest) {
		return guestService.updateGuest(newGuest.getGuestId(), newGuest.getFirstName(),
			newGuest.getLastName(), newGuest.getGender());
	}

	@PostMapping(value = "createGuest/", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void createGuest(@RequestBody Guest guest) {
		guestService.createGuest(guest.getGuestId(), guest.getFirstName(),
			guest.getLastName(), guest.getGender());
	}

	@PostMapping(value = "createGuests/", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void createGuests(@RequestBody Guest... guests) {
		guestService.createGuests(guests);
	}
}
