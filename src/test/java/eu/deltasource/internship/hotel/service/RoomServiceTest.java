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
	private Room singleRoom;

	/**
	 * Creates an empty room repository .
	 * In addition it creates  3 rooms : a double , a single , king size and
	 * adds them to the room repository.
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

		// commodities for a single room
		final Set<AbstractCommodity> singleSet = new HashSet<>(Arrays.asList(new Bed(SINGLE), new Toilet(), new Shower()));

		// commodities for a double room with king size bed
		final Set<AbstractCommodity> kingSizeSet = new HashSet<>(Arrays.asList(new Bed(BedType.KING_SIZE), new Toilet(), new Shower()));

		// create some rooms
		doubleRoom = new Room(1, doubleSet);
		singleRoom = new Room(1, singleSet);
		Room doubleRoomWithKingSize = new Room(1, kingSizeSet);

		// adds the rooms to the repository, which then can be accesses from the RoomService
		roomService.saveRooms(doubleRoom, singleRoom, doubleRoomWithKingSize);
	}

	@Test
	void getRoomById_ShouldReturnTheSoughtAfterRoom() {
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
	void getRoomById_ShouldThrowException_WhenIdIsNoneExistent() {
		//given
		int noneExistingId = 7;
		//then
		assertThrows(ItemNotFoundException.class, () -> {
			roomService.getRoomById(noneExistingId);
		});
	}

	@Test
	void saveRoom_ShouldReturnTheCurrentlyAddedRoom() {
		//given
		Room testRoom = new Room(1, singleRoom.getCommodities());
		Room actualValue;
		//when
		actualValue = roomService.saveRoom(testRoom);
		//then
		assertEquals(testRoom, actualValue);
	}

	@Test
	void saveRoom_ShouldThrowException_WhenRoomIsNull() {
		//given
		Room nullRoom = null;
		//then
		assertThrows(ItemNotFoundException.class, () -> {
			roomService.saveRoom(nullRoom);
		});
	}

	@Test
	void saveRooms_ShouldCreateMultipleRoomsAtOnce() {
		//given
		Room testRoom1 = new Room(1, singleRoom.getCommodities());
		Room testRoom2 = new Room(1, singleRoom.getCommodities());
		Room testRoom3 = new Room(1, singleRoom.getCommodities());
		//when
		roomService.saveRooms(testRoom1, testRoom2, testRoom3);
		//then
		assertTrue(roomService.findRooms().containsAll(Arrays.asList(testRoom1, testRoom2, testRoom3)));
	}

	@Test
	void saveRooms_ShouldThrowException_WhenTheRoomToSaveHasInvalidValues() {
		//given
		Set<AbstractCommodity> commoditiesSetWithNullMembers = new HashSet<>(Arrays.asList(new Bed(SINGLE), null, new Toilet()));
		Room roomWithNullCommodity = new Room(1, commoditiesSetWithNullMembers);
		//then
		assertThrows(InvalidItemException.class, () -> {
			roomService.saveRooms(roomWithNullCommodity);
		});
	}

	@Test
	void deleteRoom_ShouldReturnTrue_WhenDeletionIsSuccessful() {
		//then
		assertTrue(roomService.deleteRoom(doubleRoom));
		assertFalse(roomService.findRooms().contains(doubleRoom));
	}

	@Test
	void deleteRoom_ShouldThrowException_WhenRoomIsNull_Or_DoesNotExist() {
		//given
		Room nullRoom = null;
		Room nonExistentRoom = new Room(236, singleRoom.getCommodities());
		//then
		assertThrows(ItemNotFoundException.class, () -> {
			roomService.deleteRoom(nullRoom);
		});
		assertThrows(ItemNotFoundException.class, () -> {
			roomService.deleteRoom(nonExistentRoom);
		});
	}

	@Test
	void deleteRoomById_ShouldReturnTrue_WhenDeletionIsSuccessful() {
		//given
		//when
		//then
		assertTrue(roomService.deleteRoomById(doubleRoom.getRoomId()));
		assertFalse(roomService.findRooms().contains(doubleRoom));
	}

	@Test
	void deleteRoomById_ShouldThrowException_WhenIdIsOutOfBounds_Or_DoesNotExist() {
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
	void updateRoom_ShouldMakeChangesToTheSpecifiedRoom() {
		//given
		int roomId = 1;
		int oldRoomCapacity = roomService.getRoomById(roomId).getRoomCapacity();
		Room testRoom = new Room(roomService.getRoomById(roomId).getRoomId(), singleRoom.getCommodities());
		//when
		roomService.updateRoom(testRoom);
		//then
		assertNotEquals(oldRoomCapacity, roomService.getRoomById(roomId).getCommodities());
		assertEquals(testRoom.getCommodities(), roomService.getRoomById(roomId).getCommodities());

	}

	@Test
	void updateRoom_ShouldThrowException_WhenThePassedRoomObjectHasInvalidValues() {
		//given
		Set<AbstractCommodity> commodities = new HashSet<>(Arrays.asList(new Bed(SINGLE), null, new Toilet()));
		Room nullRoom = null;
		Room notPresentRoom = new Room(234, singleRoom.getCommodities());
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

