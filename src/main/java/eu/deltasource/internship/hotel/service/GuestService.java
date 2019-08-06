package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Gender;
import eu.deltasource.internship.hotel.domain.Guest;
import eu.deltasource.internship.hotel.exception.InvalidItemException;
import eu.deltasource.internship.hotel.exception.ItemNotFoundException;
import eu.deltasource.internship.hotel.repository.GuestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The GuestService class has a single private final member of type GuestRepository.
 * It represents the service, managing the GuestRepository.
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
@Service
public class GuestService {

	private final GuestRepository guestRepository;

	public GuestService(GuestRepository guestRepository) {
		this.guestRepository = guestRepository;
	}

	/**
	 * Checks whether the id has a valid value ,if so
	 * finds the guest with the specified id.
	 *
	 * @param id Represents the guest's id.
	 * @return Returns the guest with guestId = id.
	 */
	public Guest getGuestById(int id) {
		validateId(id);
		return this.guestRepository.findById(id);
	}

	/**
	 * Checks whether a guest with such an id exists , if he does -
	 * deletes him.
	 *
	 * @param id Represents the id of the guest to be deleted.
	 * @return Returns boolean answer based on whether the operation was successful.
	 */
	public boolean removeGuestById(int id) {
		validateId(id);
		return guestRepository.deleteById(id);
	}

	/**
	 * Updates the information of an already existing guest object.
	 *
	 * @param newGuestId   Represents the new guest id.
	 * @param newFirstName Represents the new  guest's first name.
	 * @param newLastName  Represents the new guest's last name.
	 * @param newGender    Represents the new guest's gender.
	 * @return Returns a copy of the updated guest object.
	 */
	public Guest updateGuest(int newGuestId, String newFirstName, String newLastName, Gender newGender) {
		Guest guestItem = new Guest(newGuestId, newFirstName, newLastName, newGender);
		validateGuests(guestItem);
		return guestRepository.updateGuest(guestItem);
	}

	/**
	 * Creates a new guest.
	 *
	 * @param guestId   Represents the guest's Id.
	 * @param firstName The new guest's first name.
	 * @param lastName  The new guest's last name
	 * @param gender    The new guest's gender.
	 */
	public void createGuest(int guestId, String firstName, String lastName, Gender gender) {
		Guest newGuest = new Guest(guestId, firstName, lastName, gender);
		validateGuests(newGuest);
		guestRepository.save(newGuest);
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
		validateGuests(guests);
		guestRepository.saveAll(guests);
	}

	/**
	 * @return Returns an unmodifiable list of all the guests.
	 */
	public List<Guest> getAllGuests() {
		return this.guestRepository.findAll();
	}

	/**
	 * Asserts whether the data inside the specified guest is valid.
	 * If the object has null value or the id is negative  - throws
	 * an InvalidItemException.
	 *
	 * @param items Represents the guests that are to be asserted.
	 */
	private void validateGuests(Guest... items) {
		for (Guest guest : items) {
			if (guest == null || guest.getGuestId() < 0) {
				throw new InvalidItemException("guestItem is invalid !");
			}
		}
	}

	private void validateId(int id) {
		if (id < 0 || (!guestRepository.existsById(id))) {
			throw new ItemNotFoundException("id has invalid value !");
		}
	}

}
