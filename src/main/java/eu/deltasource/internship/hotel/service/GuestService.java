package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Gender;
import eu.deltasource.internship.hotel.domain.Guest;
import eu.deltasource.internship.hotel.exception.FailedInitializationException;
import eu.deltasource.internship.hotel.exception.ItemNotFoundException;
import eu.deltasource.internship.hotel.repository.GuestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * GuestService
 */
@Service
public class GuestService {

	private final GuestRepository guestRepository;

	GuestService(GuestRepository guestRepository) {
		this.guestRepository = guestRepository;
	}

	/**
	 * Checks whether the id has a valid value ,if so
	 * finds the guest with the specified id.
	 *
	 * @param id Represents the sought after guest's id.
	 * @return Returns the sought after guest.
	 */
	public Guest getGuestById(int id) {
		if (assertIdForTheSpecifiedRepository(id, guestRepository)) {
			throw new ItemNotFoundException("id has invalid value !");
		}
		return this.guestRepository.findById(id);
	}

	/**
	 * Checks whether a guest with such an id exists , if he does
	 * deletes him.
	 *
	 * @param id Represents the id of the guest to be deleted.
	 * @return Returns boolean answer based on whether the operation was successful.
	 */
	public boolean removeGuestById(int id) {
		if (assertIdForTheSpecifiedRepository(id, guestRepository)) {
			throw new ItemNotFoundException("id has invalid value !");
		}
		return guestRepository.deleteById(id);
	}

	/**
	 * Updates the information of an already existing guest object.
	 *
	 * @param newGuestId   Represents the new guest id.
	 * @param newFirstName Represents the new  guest's first name.
	 * @param newLastName  Represents the new guest's last name.
	 * @param newGender    Represents the new guest's genger.
	 * @return Returns the updated guest object.
	 */
	public Guest updateGuest(int newGuestId, String newFirstName, String newLastName, Gender newGender) {
		Guest guestItem = new Guest(newGuestId, newFirstName, newLastName, newGender);
		assertGuest(guestItem);
		return guestRepository.updateGuest(guestItem);
	}

	/**
	 * Creates a new guest.
	 *
	 * @param guestId   Represetns the guest's Id.
	 * @param firstName The new guest's first name.
	 * @param lastName  The new guest's last name
	 * @param gender    The new guest's gender.
	 */
	public void createGuest(int guestId, String firstName, String lastName, Gender gender) {
		Guest newGuest = new Guest(guestId, firstName, lastName, gender);
		assertGuest(newGuest);
		guestRepository.save(newGuest);
	}

	/**
	 * @return Returns an unmodifiable list of all the guests.
	 */
	public List<Guest> getAllGuests() {
		return this.guestRepository.findAll();
	}

	/**
	 * Asserts whether the data inside the specified guest is valid.
	 * If the object has null value or the id is negativa
	 *
	 * @param items Represents the guests that are to be asserted.
	 */
	private void assertGuest(Guest... items) {
		for (Guest guest : items) {
			if (guest == null || guest.getGuestId() < 0) {
				throw new ItemNotFoundException("guestItem is invalid !");
			}
			if (guest.getFirstName().contains(" ") || guest.getLastName().contains(" ")) {
				throw new FailedInitializationException("Name contains forbidden symbols !");
			}
		}
	}

	private boolean assertIdForTheSpecifiedRepository(int id, GuestRepository guestRepository) {
		return !(id >= 0 && id <= guestRepository.findAll().size() && (guestRepository.existsById(id)));
	}

	/**
	 * Creates one or more guests.
	 *
	 * @param guests Represents the guests to be created.
	 */
	void createGuests(Guest... guests) {
		if (guests.length == 0) {
			throw new ItemNotFoundException("No Guests to be added !");
		}
		assertGuest(guests);
		guestRepository.saveAll(guests);
	}

}
