package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Room;
import eu.deltasource.internship.hotel.domain.commodity.*;
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
	private Room kingSizeRoom;
	private Room threePeopleKingSizeRoom;
	private Room fourPersonRoom;
	private Room fivePersonRoom;

	private AbstractCommodity doubleBed;
	private AbstractCommodity toilet;
	private AbstractCommodity shower;

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


	@BeforeEach
	void setUp() {

		// Initialize Repositories


		roomRepository = new RoomRepository();

		// Initialize Services
		roomService = new RoomService(roomRepository);
		// Comodities for a double room
		doubleBed = new Bed(BedType.DOUBLE);
		toilet = new Toilet();
		shower = new Shower();
		Set<AbstractCommodity> doubleSet = new HashSet<>(Arrays.asList(doubleBed, toilet, shower));


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
	void getRoomById() {
		//then
		assertEquals(roomService.getRoomById(doubleRoom.getRoomId()), doubleRoom);
		assertThrows(ItemNotFoundException.class, () -> {
			roomService.getRoomById(7);
		});
	}

	@Test
	void saveRoom() {
		//given
		Room testRoom = new Room(1, singleSet);
		Room expectedReturnValue;
		//when
		expectedReturnValue = roomService.saveRoom(testRoom);
		//then
		assertEquals(testRoom, expectedReturnValue);
	}

	@Test
	void saveRooms() {
		//given
		Room testRoom1 = new Room(1, singleSet);
		Room testRoom2 = new Room(1, singleSet);
		Room testRoom3 = new Room(1, singleSet);
		//when
		roomService.saveRooms(testRoom1, testRoom2, testRoom3);
		//then
		assertTrue(roomService.findRooms().contains(testRoom1));
		assertTrue(roomService.findRooms().contains(testRoom2));
		assertTrue(roomService.findRooms().contains(testRoom3));
	}

	@Test
	void deleteRoom() {
		//then
		assertTrue(roomService.deleteRoom(doubleRoom));
		assertFalse(roomService.findRooms().contains(doubleRoom));
	}

	@Test
	void deleteRoomById() {
		//when
		roomService.deleteRoomById(threePeopleKingSizeRoom.getRoomId());
		//then
		assertFalse(roomService.findRooms().contains(threePeopleKingSizeRoom));
	}

	@Test
	void updateRoom() {
		//	roomService.updateRoom()
	}
}

