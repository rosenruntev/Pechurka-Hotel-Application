package eu.deltasource.internship.hotel.controllers;

import eu.deltasource.internship.hotel.domain.Guest;
import eu.deltasource.internship.hotel.service.GuestService;
import eu.deltasource.internship.hotel.transferobject.GuestTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The GuestController class has a single private member of type GuestService with a guestRepository member inside.
 * <p>
 * Guests can be created individually , or as a List.
 * <p>
 * Finding a specific guest can be achieved by passing a guest id, or
 * you can get the entire list of guests.
 * <p>
 * Changes to the guests can only be made via the updateGuest,
 * all other methods return unmodifiable versions of the object/s we
 * are looking for.
 */
@RestController
@RequestMapping("GuestService/")
public class GuestController {
	private GuestService guestService;

	/**
	 * GuestController constructor.Initializes the guest service property.
	 *
	 * @param guestService The guestService that is used as a source for the initialization.
	 */
	@Autowired
	public GuestController(GuestService guestService) {
		this.guestService = guestService;
	}

	/**
	 * Finds the guest with guestId = id , converts the found object to a guest transfer object
	 * and returns it.
	 *
	 * @param id The id of the guest we are looking for.
	 * @return Returns a guest transfer object, with guestId = id.
	 */
	@GetMapping(value = "findGuest/{id}")
	public GuestTO findGuest(@PathVariable int id) {
		return convertToGuestTO(guestService.getGuestById(id));
	}

	/**
	 * @return Returns the whole list of guests.
	 */
	@GetMapping(value = "findAllGuests/")
	public List<GuestTO> findAllGuests() {
		return convertToGuestTOs(guestService.getAllGuests());
	}

	/**
	 * Removes the guest with guestId = id, and returns a boolean answer based on the outcome
	 * of the operation.
	 *
	 * @param id The id of the guest we are going to remove.
	 * @return Returns true of successful remove - false on failure.
	 */
	@DeleteMapping(value = "removeGuest/{id}")
	public boolean removeGuest(@PathVariable int id) {
		return guestService.removeGuestById(id);
	}

	/**
	 * Updates the data of a guest.
	 *
	 * @param newGuestTO The guest that is used as a source for the update.
	 * @return Returns a copy of the updated guest in a guest transfer object.
	 */
	@PutMapping(value = "updateGuest/", consumes = MediaType.APPLICATION_JSON_VALUE)
	public GuestTO updateGuest(@RequestBody GuestTO newGuestTO) {
		return convertToGuestTO(guestService.updateGuest(newGuestTO.getGuestId(), newGuestTO.getFirstName(),
			newGuestTO.getLastName(), newGuestTO.getGender()));
	}

	/**
	 * Creates a new guest.
	 *
	 * @param guestTO A guest transfer object that is converted to a guest object and then used to
	 *                create a new guest using the already converted guest as a data source.
	 */
	@PostMapping(value = "createGuest/", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void createGuest(@RequestBody GuestTO guestTO) {
		guestService.createGuest(guestTO.getGuestId(), guestTO.getFirstName(),
			guestTO.getLastName(), guestTO.getGender());
	}

	/**
	 * Creates a number of guests at once.
	 *
	 * @param guestTOS The guests transfer objects that are converted to guest objects and later used
	 *                 to create the new guests , using the already converted objects as a data source.
	 */
	@PostMapping(value = "createGuests/", consumes = MediaType.APPLICATION_JSON_VALUE)
	public void createGuests(@RequestBody GuestTO... guestTOS) {
		guestService.createGuests(convertToGuests(guestTOS));
	}

	/**
	 * Converts a guest transfer object to a Guest object.
	 *
	 * @param guestTO Object to be converted
	 * @return returns the newly generated Guest object.
	 */
	private Guest convertToGuest(GuestTO guestTO) {
		return new Guest(guestTO.getGuestId(), guestTO.getFirstName(), guestTO.getLastName(), guestTO.getGender());
	}

	/**
	 * Converts a number of guest transfer objects to an array of Guest objects.
	 *
	 * @param guestTOS Guest TOs to be converted.
	 * @return Returns an array of newly generated guest objects.
	 */
	private Guest[] convertToGuests(GuestTO... guestTOS) {
		Guest[] guestArray;
		guestArray = new Guest[guestTOS.length];
		for (int guestCounter = 0; guestCounter < guestTOS.length; guestCounter++) {
			guestArray[guestCounter] = convertToGuest(guestTOS[guestCounter]);
		}
		return guestArray;
	}

	/**
	 * Converts a guest object to a guest TO.
	 *
	 * @param guest The guest to be converted.
	 * @return Returns the newly generated guest TO.
	 */
	private GuestTO convertToGuestTO(Guest guest) {
		return new GuestTO(guest.getGuestId(), guest.getFirstName(), guest.getLastName(), guest.getGender());
	}

	/**
	 * Converts a list of guests to a list of guest TOs
	 *
	 * @param guests The list of guests to be converted.
	 * @return Returns the newly generated list of guest TOs.
	 */
	private List<GuestTO> convertToGuestTOs(List<Guest> guests) {
		List<GuestTO> guestTOS = new ArrayList<>();
		for (Guest guest : guests) {
			guestTOS.add(convertToGuestTO(guest));
		}
		return guestTOS;
	}
}
