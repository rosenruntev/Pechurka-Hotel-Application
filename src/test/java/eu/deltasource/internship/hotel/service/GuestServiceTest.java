package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Gender;
import eu.deltasource.internship.hotel.domain.Guest;
import eu.deltasource.internship.hotel.exception.FailedInitializationException;
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
	public void getGuestByIdShouldReturnTheGuestThatHasTheSpecifiedId() {
		//given

		Guest expectedGuest;
		//when
		guestRepository.save(testGuest);
		guestId = guestService.getAllGuests().get(0).getGuestId();
		expectedGuest = guestService.getGuestById(guestId);
		//then
		assertFalse(guestService.getAllGuests().isEmpty());
		assertEquals(1, guestId);
		assertEquals(testGuest, expectedGuest);
	}

	@Test
	public void getGuestByIdShouldThrowExceptionWhenIdIsInvalid() {
		//given
		int invalidId = -1;
		int invalidId2 = guestService.getAllGuests().size() + 2;
		//then
<<<<<<< HEAD
=======
		assertFalse(guestRepository.findAll().isEmpty());
		assertEquals(1, guestId);
		assertEquals(testGuest, expectedGuest);
>>>>>>> aba5c41d302a8e69174b459f161b06a5d66571fd
		assertThrows(ItemNotFoundException.class, () -> {
			guestService.getGuestById(invalidId);
		});
		assertThrows(ItemNotFoundException.class, () -> {
			guestService.getGuestById(invalidId2);
		});

	}

	@Test
	public void removeGuestByIdShouldDeleteTheGuestWithTheSpecifiedId() {
		//when
		guestService.createGuests(testGuest);
		guestId = guestService.getAllGuests().get(0).getGuestId();
		//then
		assertTrue(guestService.removeGuestById(guestId));
		assert (guestService.getAllGuests().isEmpty());
	}

	@Test
	public void removeGuestByIdShouldThrowExceptionWhenIdIsInvalid() {
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
	public void testUpdateGuestSuccessScenario() {
		//given
		String updateFName = "newGuestFName";
		String updateLName = "newGuestLName";
		int newGuestId = 1;
		Gender newGuestGender = Gender.FEMALE;
		//when
		guestService.createGuest(guestId, fName, lName, guestGender);
		Guest newGuest = new Guest(newGuestId, updateFName, updateLName, newGuestGender);
		guestService.updateGuest(guestId, fName, lName, guestGender);
		//then
		assertEquals(guestService.getGuestById(newGuestId), newGuest);
	}
	@Test
	public  void updateGuestShouldThrowExceptionWhenThePassedGuestInformationIsInvalid(){
		//given
		String updateFName = "newGuestFName";
		String updateLName = "newGuestLName";
		String invalidFName = null;
		String invalidLName = "";
		int invalidId = -1;
		//when
		guestService.createGuest(guestId, fName, lName, guestGender);
		//then
		assertThrows(FailedInitializationException.class,()->{
			guestService.updateGuest(guestId,invalidFName,updateLName,guestGender);
		});
		assertThrows(FailedInitializationException.class,()->{
			guestService.updateGuest(guestId,updateFName,invalidLName,guestGender);
		});
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
		assertThrows(FailedInitializationException.class, () -> {
			final Guest failedInitializationGuest = new Guest(1, null, null, Gender.MALE);
		});
	}

}
