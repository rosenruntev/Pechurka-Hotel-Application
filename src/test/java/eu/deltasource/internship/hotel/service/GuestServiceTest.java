package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Gender;
import eu.deltasource.internship.hotel.domain.Guest;
import eu.deltasource.internship.hotel.exception.ItemNotFoundException;
import eu.deltasource.internship.hotel.repository.GuestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GuestServiceTest {


	private GuestRepository guestRepository;

	private GuestService guestService;

	private String fName;
	private String lName;
	private Gender guestGender;
	private int guestId;
	private Guest testGuest;

	@BeforeEach
	public void setUp() {

		// Initialize Repositories
		guestRepository = new GuestRepository();

		//Initialize Services
		guestService = new GuestService(guestRepository);

		// Initialize guest
		fName = "testFName";
		lName = "testLNAme";
		guestGender = Gender.MALE;
		guestId = 1;
		testGuest = new Guest(guestId, fName, lName, guestGender);


	}

	@Test
	public void testGetGuestById() {
		//given
		int invalidId = -1;
		int invalidId2 = guestRepository.findAll().size() + 2;
		Guest expectedGuest;
		//when
		guestRepository.save(testGuest);
		guestId = guestRepository.findAll().get(0).getGuestId();
		expectedGuest = guestRepository.findById(guestId);
		//then
		assert (!guestRepository.findAll().isEmpty());
		assert (guestId == 1);
		assert (expectedGuest != null);
		assertThrows(ItemNotFoundException.class, () -> {
			guestService.getGuestById(invalidId);
		});
		assertThrows(ItemNotFoundException.class, () -> {
			guestService.getGuestById(invalidId2);
		});

	}

	@Test
	public void testRemoveGuestById() {
		//given
		int invalidId = -1;
		int invalidId2 = guestRepository.findAll().size() + 2;
		//when
		guestRepository.saveAll(testGuest);
		guestId = guestRepository.findAll().get(0).getGuestId();
		guestRepository.deleteById(guestId);
		//then
		assert (guestRepository.findAll().isEmpty());
		assertThrows(ItemNotFoundException.class, () -> {
			guestService.removeGuestById(invalidId);
		});
		assertThrows(ItemNotFoundException.class, () -> {
			guestService.removeGuestById(invalidId2);
		});

	}

	@Test
	public void testUpdateGuest() {
		//given

		String updateFName = "newGuestFName";
		String updateLName = "newGuestLName";
		int newGuestId = 1;
		int invalidId = -1;
		Gender newGuestGender = Gender.FEMALE;
		//when
		guestService.createGuest(guestId, fName, lName, guestGender);
		Guest newGuest = new Guest(newGuestId, updateFName, updateLName, newGuestGender);
		guestService.updateGuest(guestId, fName, lName, guestGender);
		//then
		assertThrows(ItemNotFoundException.class, () -> {
			guestService.updateGuest(invalidId, updateFName, updateLName, guestGender);
		});
	}

	@Test
	public void testCreateGuest() {
		//when
		guestService.createGuest(guestId, fName, lName, guestGender);
		//then
		assert (guestService.getGuestById(guestId).equals(testGuest));
		assertThrows(ItemNotFoundException.class, () -> {
			guestService.createGuest(-1, fName, lName, guestGender);
		});
	}

}
