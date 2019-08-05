package eu.deltasource.internship.hotel.Controllers;

import eu.deltasource.internship.hotel.domain.Gender;
import eu.deltasource.internship.hotel.domain.Guest;
import eu.deltasource.internship.hotel.repository.GuestRepository;
import eu.deltasource.internship.hotel.service.GuestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GuestController {
	private Guest newGuest1 = new Guest(1,"Ivan","Todorov",Gender.MALE);
	private Guest newGuest2 = new Guest(2,"Maria","Topalova",Gender.FEMALE);
	private Guest newGuest3 = new Guest(3,"Georgi","Delev",Gender.FEMALE);
	private GuestRepository guestRepository = new GuestRepository(newGuest1,newGuest2,newGuest3);
	private GuestService guestService = new GuestService(guestRepository);



	private GuestController() {
	}

	public GuestController(GuestService guestService) {
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

	@RequestMapping(value = "GuestService/updateGuest", method = RequestMethod.POST)
	public void createGuest(@RequestBody int guestId, String firstName, String lastName, Gender gender) {
		guestService.createGuest(guestId, firstName, lastName, gender);
	}

	@RequestMapping("Guest/getAllGuests")
	public List<Guest> getAllGuests() {
		return guestService.getAllGuests();
	}


}
