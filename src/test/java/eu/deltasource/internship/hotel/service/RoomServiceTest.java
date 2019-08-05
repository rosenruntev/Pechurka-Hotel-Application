package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Room;
import eu.deltasource.internship.hotel.domain.commodity.*;
import eu.deltasource.internship.hotel.exception.InvalidItemException;
import eu.deltasource.internship.hotel.exception.ItemNotFoundException;
import eu.deltasource.internship.hotel.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static eu.deltasource.internship.hotel.domain.commodity.BedType.SINGLE;
import static org.junit.jupiter.api.Assertions.*;

class RoomServiceTest {


	private RoomService roomService;
	private RoomRepository roomRepository;
	private Room doubleRoom;
	private Room threePeopleKingSizeRoom;

	// commodities for a single room
	private static final Set<AbstractCommodity> singleSet = new HashSet<>(Arrays.asList(new Bed(SINGLE), new Toilet(), new Shower()));

	// commodities for a double room with king size bed
	private static final Set<AbstractCommodity> kingSizeSet = new HashSet<>(Arrays.asList(new Bed(BedType.KING_SIZE), new Toilet(), new Shower()));

	// commodities for a 3 person room with a king size and a single
	private static final Set<AbstractCommodity> threePeopleKingSizeSet = new HashSet<>(Arrays.asList(new Bed(BedType.KING_SIZE), new Bed(SINGLE), new Toilet(), new Shower()));

	// commodities for a 4 person room with 2 doubles
	private static final Set<AbstractCommodity> fourPersonSet = new HashSet<>(Arrays.asList(new Bed(BedType.DOUBLE), new Bed(BedType.DOUBLE), new Toilet(), new Shower()));

	// commodities for a 4 person room with 2 doubles
	private static final Set<AbstractCommodity> fivePersonSet = new HashSet<>(Arrays.asList(new Bed(BedType.KING_SIZE), new Bed(BedType.DOUBLE), new Bed(SINGLE), new Toilet(), new Toilet(), new Shower()));

	/**
	 * Creates an empty room repository .
	 * In addition it creates  6 rooms : a double , a single , king size ,three people king size
	 * ,four people room ,a 5 people room and adds them to the room repository.
	 * Finally it adds the room repository to a room service.
	 */
	@BeforeEach
	void setUp() {

		// Initialize Repositories

		roomRepository = new RoomRepository();

		// Initialize Services
		roomService = new RoomService(roomRepository);
		// Commodities for a double room
		AbstractCommodity doubleBed = new Bed(BedType.DOUBLE);
		AbstractCommodity toilet = new Toilet();
		AbstractCommodity shower = new Shower();
		Set<AbstractCommodity> doubleSet = new HashSet<>(Arrays.asList(doubleBed, toilet, shower));


		// create some rooms
		doubleRoom = new Room(1, doubleSet);
		Room singleRoom = new Room(1, singleSet);
		Room kingSizeRoom = new Room(1, kingSizeSet);
		threePeopleKingSizeRoom = new Room(1, threePeopleKingSizeSet);
		Room fourPersonRoom = new Room(1, fourPersonSet);
		Room fivePersonRoom = new Room(1, fivePersonSet);

		// adds the rooms to the repository, which then can be accesses from the RoomService
		roomService.saveRooms(doubleRoom, singleRoom, kingSizeRoom, threePeopleKingSizeRoom, fourPersonRoom, fivePersonRoom);
	}

	@Test
	void getRoomByIdShouldReturnAReferenceToTheRoomWeAreLookingFor() {
		//given
		Room actualRoom;
		int doubleRoomCapacity = 2;
		Set<AbstractCommodity> doubleRoomCommodities = doubleRoom.getCommodities();
		//when
		actualRoom = roomService.getRoomById(doubleRoom.getRoomId());
		//then
		assertEquals(doubleRoom, actualRoom);
		assertEquals(doubleRoom.getCommodities(), actualRoom.getCommodities());
		assertEquals(actualRoom.getRoomCapacity(), doubleRoomCapacity);
	}

	@Test
	void getRoomByIdShouldThrowExceptionWhenTheProvidedIndexIsOutOfBounds() {
		//given
		int nonExistingId = 7;
		//then
		assertThrows(ItemNotFoundException.class, () -> {
			roomService.getRoomById(nonExistingId);
		});
	}

	@Test
	void saveRoomShouldReturnTheCurrentlyAddedRoom() {
		//given
		Room testRoom = new Room(1, singleSet);
		Room actualValue;
		//when
		actualValue = roomService.saveRoom(testRoom);
		//then
		assertEquals(testRoom, actualValue);
	}

	@Test
	void saveRoomShouldThrowExcWhenRoomIsNull() {
		//given
		Room nullRoom = null;
		//then
		assertThrows(ItemNotFoundException.class, () -> {
			roomService.saveRoom(nullRoom);
		});
	}

	@Test
	void saveRooms_SuccessScenario() {
		//given
		Room testRoom1 = new Room(1, singleSet);
		Room testRoom2 = new Room(1, singleSet);
		Room testRoom3 = new Room(1, singleSet);
		//when
		roomService.saveRooms(testRoom1, testRoom2, testRoom3);
		//then
		assertTrue(roomService.findRooms().containsAll(Arrays.asList(testRoom1, testRoom2, testRoom3)));
	}

	@Test
	void saveRoomsShouldThrowExcWhenTheRoomToBeSavedHasInvalidValues() {
		//given
		Set<AbstractCommodity> commoditiesSetWithNullMembers = new HashSet<>(Arrays.asList(new Bed(SINGLE), null, new Toilet()));
		Room roomWithNullCommodity = new Room(1, commoditiesSetWithNullMembers);
		//then
		assertThrows(InvalidItemException.class, () -> {
			roomService.saveRooms(roomWithNullCommodity);
		});
	}

	@Test
	void deleteRoomShouldReturnTrueWhenDeletionIsSuccessful() {
		//then
		assertTrue(roomService.deleteRoom(doubleRoom));
		assertFalse(roomService.findRooms().contains(doubleRoom));
	}

	@Test
	void deleteRoomShouldThrowExcWhenRoomIsNullOrDoesNotExist() {
		//given
		Room nullRoom = null;
		Room nonExistentRoom = new Room(236, singleSet);
		//when + then
		assertThrows(ItemNotFoundException.class, () -> {
			roomService.deleteRoom(nullRoom);
		});
		assertThrows(ItemNotFoundException.class, () -> {
			roomService.deleteRoom(nonExistentRoom);
		});
	}

	@Test
	void deleteRoomByIdShouldReturnTrueWhenDeletionIsSuccessful() {
		//given
		//when
		//then
		assertTrue(roomService.deleteRoomById(threePeopleKingSizeRoom.getRoomId()));
		assertFalse(roomService.findRooms().contains(threePeopleKingSizeRoom));
	}

	@Test
	void deleteRoomByIdShouldThrowExcWhenIdIsOutOfBoundsOrDoesNotExist() {
		//given
		//when
		//then
		assertThrows(ItemNotFoundException.class, () -> {
			roomService.deleteRoomById(-1);
		});
		assertThrows(ItemNotFoundException.class, () -> {
			roomService.deleteRoomById(250);
		});
	}

	@Test
	void updateRoomShouldMakeChangesToTheRoomWithTheSpecifiedRoomId() {
		//given
		int roomId = 1;
		int oldRoomCapacity = roomService.getRoomById(roomId).getRoomCapacity();
		Room testRoom = new Room(roomService.getRoomById(roomId).getRoomId(), singleSet);
		//when
		roomService.updateRoom(testRoom);
		//then
		assertNotEquals(oldRoomCapacity, roomService.getRoomById(roomId).getCommodities());
		assertEquals(testRoom.getCommodities(), roomService.getRoomById(roomId).getCommodities());

	}

	@Test
	void updateRoomShouldThrowExceptionWhenThePassedRoomObjectHasInvalidValues() {
		//given
		Set<AbstractCommodity> commodities = new HashSet<>(Arrays.asList(new Bed(SINGLE), null, new Toilet()));
		Room nullRoom = null;
		Room notPresentRoom = new Room(234, singleSet);
		Room roomWithNullCommodity = new Room(doubleRoom.getRoomId(), commodities); // doubleRoom already exists in the room service's repository.
		roomRepository.save(roomWithNullCommodity);
		//then
		assertThrows(ItemNotFoundException.class, () -> {
			roomService.updateRoom(nullRoom);
		});
		assertThrows(ItemNotFoundException.class, () -> {
			roomService.updateRoom(notPresentRoom);
		});
		assertThrows(InvalidItemException.class, () -> {
			roomService.updateRoom(roomWithNullCommodity);
		});
	}
}

