package eu.deltasource.internship.hotel.controller;

import eu.deltasource.internship.hotel.domain.Guest;
import eu.deltasource.internship.hotel.service.GuestService;
import eu.deltasource.internship.hotel.transferobject.GuestTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
	public List<GuestTO> findAllGuests() {
		return convertToGuestTOs(guestService.getAllGuests());
	}

	@DeleteMapping(value = "removeGuest/{id}")
	public boolean removeGuest(@PathVariable int id) {
		return guestService.removeGuestById(id);
	}

	@PutMapping(value = "updateGuest/", consumes = MediaType.APPLICATION_JSON_VALUE)
	public GuestTO updateGuest(@RequestBody GuestTO newGuestTO) {
		return convertToGuestTO(guestService.updateGuest(newGuestTO.getGuestId(), newGuestTO.getFirstName(),
			newGuestTO.getLastName(), newGuestTO.getGender()));
	}

	@PostMapping(value = "createGuest/", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void createGuest(@RequestBody GuestTO guestTO) {
		guestService.createGuest(guestTO.getGuestId(), guestTO.getFirstName(),
			guestTO.getLastName(), guestTO.getGender());
	}

	@PostMapping(value = "createGuests/", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void createGuests(@RequestBody GuestTO... guestTOS) {
		guestService.createGuests(convertToGuests(guestTOS));
	}


	private Guest convertToGuest(GuestTO guestTO) {
		return new Guest(guestTO.getGuestId(), guestTO.getFirstName(), guestTO.getLastName(), guestTO.getGender());
	}

	private Guest[] convertToGuests(GuestTO... guestTOS) {
		Guest[] guestArray;
		guestArray = new Guest[guestTOS.length];
		for (int guestCounter = 0; guestCounter < guestTOS.length; guestCounter++) {
			guestArray[guestCounter] = convertToGuest(guestTOS[guestCounter]);
		}
		return guestArray;
	}

	private GuestTO convertToGuestTO(Guest guest) {
		return new GuestTO(guest.getGuestId(), guest.getFirstName(), guest.getLastName(), guest.getGender());
	}

	private List<GuestTO> convertToGuestTOs(List<Guest> guests) {
		List<GuestTO> guestTOS = new ArrayList<>();
		for (Guest guest : guests) {
			guestTOS.add(convertToGuestTO(guest));
		}
		return guestTOS;
	}
}
