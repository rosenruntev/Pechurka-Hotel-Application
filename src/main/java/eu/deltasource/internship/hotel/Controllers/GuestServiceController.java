package eu.deltasource.internship.hotel.Controllers;

import eu.deltasource.internship.hotel.domain.Gender;
import eu.deltasource.internship.hotel.domain.Guest;
import eu.deltasource.internship.hotel.repository.GuestRepository;
import eu.deltasource.internship.hotel.service.GuestService;

import org.springframework.web.bind.annotation.*;


import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class GuestServiceController {
	private GuestService guestService;


	private GuestServiceController() {
	}

	public GuestServiceController(GuestService guestService) {
		this.guestService = guestService;
	}

	@RequestMapping("GuestService/getGuestByID/{id}")
	public Guest findGuest(@PathVariable int id) {
		return guestService.getGuestById(1);
	}

	@RequestMapping("GuestService/removeGuestById/{id}")
	public boolean deleteGuest(@PathVariable int id) {
		return guestService.removeGuestById(id);
	}

	@RequestMapping("GuestService/updateGuest")
	public Guest updateGuest(int guestId, String firstName, String lastName, Gender gender) {
		return guestService.updateGuest(guestId, firstName, lastName, gender);
	}

	@RequestMapping(value = "GeuestService/updateGuest", method = RequestMethod.POST)
	public void createGuest(@RequestBody int guestId, String firstName, String lastName, Gender gender) {
		guestService.createGuest(guestId, firstName, lastName, gender);
	}

	@RequestMapping("Guest/getAllGuests")
	public List<Guest> getAllGuests() {
		return guestService.getAllGuests();
	}


}
