package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Gender;
import eu.deltasource.internship.hotel.domain.Guest;
import eu.deltasource.internship.hotel.exception.FailedInitializationException;
import eu.deltasource.internship.hotel.exception.InvalidItemException;
import eu.deltasource.internship.hotel.exception.ItemNotFoundException;
import eu.deltasource.internship.hotel.repository.GuestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GuestServiceTest {


	private GuestRepository guestRepository;
	private GuestService guestService;
	private Guest testGuest;


	/**
	 * Creates an empty guest repository and adds it to a guest service.
	 * In addition creates a single test guest with a test first/last name , with male gender and Id = 1.
	 */
	@BeforeEach
	void setUp() {
		int guestId;
		String fName;
		String lName;
		Gender guestGender;
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
	void getGuestById_ShouldReturnTheGuest_WhoHasTheSpecifiedId() {
		//given
		int guestId;
		Guest expectedGuest;
		//when
		guestRepository.save(testGuest);
		guestId = testGuest.getGuestId();
		expectedGuest = guestService.getGuestById(guestId);
		//then
		assertEquals(testGuest.getFirstName(), guestService.getGuestById(guestId).getFirstName());
		assertEquals(testGuest.getLastName(), guestService.getGuestById(guestId).getLastName());
		assertEquals(testGuest.getGuestId(), guestId);
		assertEquals(testGuest.getGender(), testGuest.getGender());
		assertEquals(expectedGuest, testGuest);
	}

	@Test
	void getGuestById_ShouldThrowException_WhenIdIsInvalid() {
		//given
		int invalidId = -1;
		int invalidId2 = guestService.getAllGuests().size() + 2;
		//then
		assertThrows(ItemNotFoundException.class, () -> {
			guestService.getGuestById(invalidId);
		});
		assertThrows(ItemNotFoundException.class, () -> {
			guestService.getGuestById(invalidId2);
		});
	}

	@Test
	void removeGuestById_ShouldDeleteTheGuest_WithTheSpecifiedId() {
		//given
		int guestId;
		//when
		guestService.createGuests(testGuest);
		guestId = guestService.getAllGuests().get(0).getGuestId();
		//then
		assertTrue(guestService.removeGuestById(guestId));
		assertTrue(guestService.getAllGuests().isEmpty());
	}

	@Test
	void removeGuestById_ShouldThrowException_WhenIdIsInvalid() {
		//given
		int invalidId = -1;
		int invalidId2 = guestService.getAllGuests().size() + 2;
		//then
		assertThrows(ItemNotFoundException.class, () -> {
			guestService.removeGuestById(invalidId);
		});
		assertThrows(ItemNotFoundException.class, () -> {
			guestService.removeGuestById(invalidId2);
		});

	}

	@Test
	void updateGuest_ShouldUpdateTheGuestData() {
		//given
		String updateFName = "newGuestFName";
		String updateLName = "newGuestLName";
		int newGuestId = 1;
		Gender newGuestGender = Gender.FEMALE;
		//when
		guestService.createGuest(testGuest.getGuestId(), testGuest.getFirstName(), testGuest.getLastName(), testGuest.getGender());
		Guest newGuest = new Guest(newGuestId, updateFName, updateLName, newGuestGender);
		guestService.updateGuest(newGuestId, updateFName, updateLName, newGuestGender);
		//then
		assertEquals(guestService.getGuestById(newGuestId), newGuest);
		assertEquals(newGuest.getFirstName(), guestService.getGuestById(newGuestId).getFirstName());
		assertEquals(newGuest.getLastName(), guestService.getGuestById(newGuestId).getLastName());
		assertEquals(newGuest.getGender(), guestService.getGuestById(newGuestId).getGender());
	}

	@Test
	void updateGuest_ShouldThrowException_WhenThePassedGuestInformationIsInvalid() {
		//given
		String updateFName = "newGuestFName";
		String updateLName = "newGuestLName";
		String invalidFName = null;
		String invalidLName = "";
		int invalidId = -1;
		//when
		guestService.createGuest(testGuest.getGuestId(), testGuest.getFirstName(), testGuest.getLastName(), testGuest.getGender());
		//then
		assertThrows(FailedInitializationException.class, () -> {
			guestService.updateGuest(testGuest.getGuestId(), invalidFName, updateLName, Gender.MALE);
		});
		assertThrows(FailedInitializationException.class, () -> {
			guestService.updateGuest(testGuest.getGuestId(), updateFName, invalidLName, Gender.MALE);
		});
		assertThrows(InvalidItemException.class, () -> {
			guestService.updateGuest(invalidId, updateFName, updateLName, Gender.MALE);
		});
	}

	@Test
	void createGuest_ShouldCreateNewGuest() {
		//given
		//when
		guestService.createGuest(testGuest.getGuestId(), testGuest.getFirstName(), testGuest.getLastName(), testGuest.getGender());
		//then
		assertEquals(guestService.getGuestById(testGuest.getGuestId()), testGuest);
		assertThrows(InvalidItemException.class, () -> {
			guestService.createGuest(-1, testGuest.getFirstName(), testGuest.getLastName(), Gender.MALE);
		});
		assertThrows(FailedInitializationException.class, () -> {
			final Guest failedInitializationGuest = new Guest(1, null, null, Gender.MALE);
		});
	}

	@Test
	void createGuests_ShouldCreateAMultipleGuestsAtOnce() {
		//given
		Guest guest1 = new Guest(1, "Ivan", "Poparov", Gender.MALE);
		Guest guest2 = new Guest(1, "Gogata", "Velev", Gender.MALE);
		Guest guest3 = new Guest(1, "Nakata", "Alfa", Gender.MALE);
		int expectedRepositorySize = 3;
		//when
		guestService.createGuests(guest1, guest2, guest3);
		//then
		assertEquals(guestService.getAllGuests().size(), expectedRepositorySize);
		assertTrue(guestService.getAllGuests().containsAll(Arrays.asList(guest1, guest2, guest3)));
	}

	@Test
	void createGuests_ShouldThrowException_ZeroGuestsInArrayFormat() {
		//given
		Guest[] emptyArrayOfGuests = new Guest[0];
		//then
		assertThrows(ItemNotFoundException.class, () -> {
			guestService.createGuests(emptyArrayOfGuests);
		});
	}
}
