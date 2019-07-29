package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Gender;
import eu.deltasource.internship.hotel.domain.Guest;
import eu.deltasource.internship.hotel.domain.Hotel;
import eu.deltasource.internship.hotel.domain.Room;
import eu.deltasource.internship.hotel.domain.commodity.*;
import eu.deltasource.internship.hotel.exception.ItemNotFoundException;
import eu.deltasource.internship.hotel.repository.BookingRepository;
import eu.deltasource.internship.hotel.repository.GuestRepository;
import eu.deltasource.internship.hotel.repository.RoomRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static eu.deltasource.internship.hotel.domain.commodity.BedType.SINGLE;

public class GuestServiceTest {

	private BookingRepository bookingRepository;
	private GuestRepository guestRepository;
	private RoomRepository roomRepository;

	private RoomService roomService;
	private GuestService guestService;
	private BookingService bookingService;

	private Hotel hotel;

	private AbstractCommodity doubleBed;
	private AbstractCommodity toilet;
	private AbstractCommodity shower;

	private Room doubleRoom;
	private Room singleRoom;
	private Room kingSizeRoom;
	private Room threePeopleKingSizeRoom;
	private Room fourPersonRoom;
	private Room fivePersonRoom;

	private String fName;
	private String lName;
	private Gender guestGender;
	private int guestId;
	private Guest testGuest;

	@Before
	public void setUp() {

		// Initialize Repositories
		bookingRepository = new BookingRepository();
		guestRepository = new GuestRepository();
		roomRepository = new RoomRepository();

		//Initialize Services
		roomService = new RoomService(roomRepository);
		guestService = new GuestService(guestRepository);
		bookingService = new BookingService(bookingRepository, roomService, guestService);

		// Initialize Hotel (API), which does nothing

		hotel = new Hotel(bookingService, guestService, roomService);

		// Filling up hotel with ready rooms to use

		// Initialize guest
		fName = "testFName";
		lName = "testLNAme";
		guestGender = Gender.MALE;
		guestId = 1;
		testGuest = new Guest(guestId, fName, lName, guestGender);

		//Comodities for a double room
		doubleBed = new Bed(BedType.DOUBLE);
		toilet = new Toilet();
		shower = new Shower();

		Set<AbstractCommodity> doubleSet = new HashSet<>(Arrays.asList(doubleBed, toilet, shower));

		// commodities for a single room
		Set<AbstractCommodity> singleSet = new HashSet<>(Arrays.asList(new Bed(SINGLE), new Toilet(), new Shower()));

		// commodities for a double room with king size bed
		Set<AbstractCommodity> kingSizeSet = new HashSet<>(Arrays.asList(new Bed(BedType.KING_SIZE), new Toilet(), new Shower()));

		// commodities for a 3 person room with a king size and a single
		Set<AbstractCommodity> threePeopleKingSizeSet = new HashSet<>(Arrays.asList(new Bed(BedType.KING_SIZE), new Bed(SINGLE), new Toilet(), new Shower()));

		// commodities for a 4 person room with 2 doubles
		Set<AbstractCommodity> fourPersonSet = new HashSet<>(Arrays.asList(new Bed(BedType.DOUBLE), new Bed(BedType.DOUBLE), new Toilet(), new Shower()));

		// commodities for a 4 person room with 2 doubles
		Set<AbstractCommodity> fivePersonSet = new HashSet<>(Arrays.asList(new Bed(BedType.KING_SIZE), new Bed(BedType.DOUBLE), new Bed(SINGLE), new Toilet(), new Toilet(), new Shower()));

		// create some rooms
		doubleRoom = new Room(1, doubleSet);
		singleRoom = new Room(1, singleSet);
		kingSizeRoom = new Room(1, kingSizeSet);
		threePeopleKingSizeRoom = new Room(1, threePeopleKingSizeSet);
		fourPersonRoom = new Room(1, fourPersonSet);
		fivePersonRoom = new Room(1, fivePersonSet);

		// adds the rooms to the repository, which then can be accesses from the RoomService
		roomService.saveRooms(doubleRoom, singleRoom, kingSizeRoom, threePeopleKingSizeRoom, fourPersonRoom, fivePersonRoom);

	}

	@Test
	public void testGetGuestById_successScenario() {
		//given

		Guest expectedGuest;
		//when
		guestRepository.save(testGuest);
		guestId = guestRepository.findAll().get(0).getGuestId();
		expectedGuest = guestRepository.findById(guestId);
		//then
		assert (!guestRepository.findAll().isEmpty());
		assert (guestId == 1);
		assert (expectedGuest != null);
	}

	@Test(expected = ItemNotFoundException.class)
	public void testGetGuestById_failScenario_1() {
		//given
		int invalidId = -1;
		//then
		guestService.getGuestById(invalidId);
	}

	@Test(expected = ItemNotFoundException.class)
	public void testGetGuestById_failScenario_2() {
		//given
		int invalidId = guestRepository.findAll().size() + 1;
		//then
		guestService.getGuestById(invalidId);
	}


	@Test
	public void testRemoveGuestById_successScenario() {
		//when
		guestRepository.saveAll(testGuest);
		guestId = guestRepository.findAll().get(0).getGuestId();
		guestRepository.deleteById(guestId);
		//then
		assert (guestRepository.findAll().isEmpty());
	}

	@Test(expected = ItemNotFoundException.class)
	public void testRemoveGuestById_failScenario_1() {
		//given

		int invalidId = guestRepository.findAll().size() + 2;
		//when

		guestRepository.save(testGuest);
		guestService.removeGuestById(invalidId);
	}

	@Test(expected = ItemNotFoundException.class)
	public void testRemoveGuestById_failScenario_2() {
		//given

		int invalidId = -1;
		//when

		guestRepository.save(testGuest);
		//then

		assert (guestService.removeGuestById(invalidId));
	}

	@Test
	public void testUpdateGuest_successScenario() {
		//given

		String updateFName = "newGuestFName";
		String updateLName = "newGuestLName";
		int newGuestId = 1;
		Gender newGuestGender = Gender.FEMALE;
		//when
		guestService.createGuest(guestId, fName, lName, guestGender);
		Guest newGuest = new Guest(newGuestId, updateFName, updateLName, newGuestGender);
		guestService.updateGuest(guestId,fName,lName,guestGender);

		//then
		assert (newGuest.equals(guestService.getGuestById(newGuestId)));
	}


	@Test(expected = ItemNotFoundException.class)
	public void testUpdateGuest_failScenario() {
		//given
		int newGuestId = 1;
		//when
		guestService.createGuest(guestId, fName, lName, guestGender);
		Guest newGuest = null;
		guestService.updateGuest(-1,fName,lName,guestGender);

		//then
		assert (newGuest.equals(guestService.getGuestById(newGuestId)));
	}

	@Test
	public void testCreateGuest_successScenario() {
		//when
		guestService.createGuest(guestId, fName, lName, guestGender);
		//then
		assert (guestService.getGuestById(guestId).equals(testGuest));
	}

	@Test(expected = ItemNotFoundException.class)
	public void testCreateGuest_failScenario() {
		guestService.createGuest(-1, fName, lName, guestGender);
	}

}
