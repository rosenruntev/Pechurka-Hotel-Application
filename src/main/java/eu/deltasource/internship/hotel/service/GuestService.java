package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Guest;
import eu.deltasource.internship.hotel.repository.GuestRepository;

/**
 * A class that represents service for guests
 */
public class GuestService {

	private final GuestRepository guestRepository;

	/**
	 * Constructs a guest service with given guest repository
	 *
	 * @param guestRepository the guest repository
	 */
	public GuestService(GuestRepository guestRepository) {
		this.guestRepository = guestRepository;
	}

	/**
	 * Returns the guest with particular id
	 *
	 * @param id the id of the guest
	 * @return the guest with particular id
	 */
	public Guest getGuestById(int id) {
		return guestRepository.findById(id);
	}
}
