package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Room;
import eu.deltasource.internship.hotel.domain.commodity.*;
import eu.deltasource.internship.hotel.exception.InvalidItemException;
import eu.deltasource.internship.hotel.exception.ItemNotFoundException;
import eu.deltasource.internship.hotel.repository.RoomRepository;
import eu.deltasource.internship.hotel.trasferObjects.RoomTO;
import eu.deltasource.internship.hotel.trasferObjects.commodityTOs.AbstractCommodityTO;
import eu.deltasource.internship.hotel.trasferObjects.commodityTOs.BedTO;
import eu.deltasource.internship.hotel.trasferObjects.commodityTOs.ShowerTO;
import eu.deltasource.internship.hotel.trasferObjects.commodityTOs.ToiletTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static eu.deltasource.internship.hotel.domain.commodity.BedType.DOUBLE;
import static eu.deltasource.internship.hotel.domain.commodity.BedType.SINGLE;
import static org.junit.jupiter.api.Assertions.*;

class RoomServiceTest {


	private RoomService roomService;

	private RoomTO doubleRoom;
	private RoomTO singleRoom;

	/**
	 * Creates an empty room repository .
	 * In addition it creates  3 rooms : a double , a single , king size and
	 * adds them to the room repository.
	 * Finally it adds the room repository to a room service.
	 */
	@BeforeEach
	void setUp() {

		// Initialize Repositories

		RoomRepository roomRepository = new RoomRepository();

		// Initialize Services
		roomService = new RoomService(roomRepository);
		// Commodities for a double room
		AbstractCommodityTO doubleBed = new BedTO(BedType.DOUBLE);
		AbstractCommodityTO toilet = new ToiletTO();
		AbstractCommodityTO shower = new ShowerTO();
		Set<AbstractCommodityTO> doubleSet = new HashSet<>(Arrays.asList(doubleBed, toilet, shower));

		// commodities for a single room
		final Set<AbstractCommodityTO> singleSet = new HashSet<>(Arrays.asList(new BedTO(SINGLE), new ToiletTO(), new ShowerTO()));

		// commodities for a double room with king size bed
		final Set<AbstractCommodityTO> kingSizeSet = new HashSet<>(Arrays.asList(new BedTO(BedType.KING_SIZE), new ToiletTO(), new ShowerTO()));

		// create some rooms
		doubleRoom = new RoomTO(1, doubleSet);
		singleRoom = new RoomTO(1, singleSet);
		RoomTO doubleRoomWithKingSize = new RoomTO(1, kingSizeSet);

		// adds the rooms to the repository, which then can be accesses from the RoomService
		roomService.saveRooms(doubleRoom, singleRoom, doubleRoomWithKingSize);
	}

	@Test
	void getRoomById_ShouldReturnTheSoughtAfterRoom() {
		//given
		RoomTO actualRoom;
		int doubleRoomCapacity = 2;
		//when
		actualRoom = roomService.getRoomById(doubleRoom.getRoomId());
		//then
		assertEquals(doubleRoom, actualRoom);

		assertEquals(actualRoom.getRoomCapacity(), doubleRoomCapacity);
		assertEquals(getNumberOfShowersFrom(doubleRoom), getNumberOfShowersFrom(actualRoom));
		assertEquals(getNumberOfToiletsFrom(doubleRoom), getNumberOfToiletsFrom(actualRoom));
		assertTrue(getBedsFrom(actualRoom).contains(DOUBLE));
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
		RoomTO testRoom = new RoomTO(1, singleRoom.getCommodities());
		RoomTO actualValue;
		RoomTO lastAddedRoom ;
		//when
		actualValue = roomService.saveRoom(testRoom);
		lastAddedRoom = roomService.getRoomById(roomService.findRooms().size());
		//then
		assertEquals(roomService.findRooms().size(), actualValue.getRoomId());
		assertEquals(roomService.getRoomById(roomService.findRooms().size()).getRoomCapacity(),actualValue.getRoomCapacity());
		assertEquals(getNumberOfShowersFrom(lastAddedRoom),getNumberOfShowersFrom(actualValue));
		assertEquals(getNumberOfShowersFrom(lastAddedRoom),getNumberOfShowersFrom(actualValue));
	}

	@Test
	void saveRoom_ShouldThrowException_WhenRoomIsNull() {
		//given
		RoomTO nullRoom = null;
		//then
		assertThrows(ItemNotFoundException.class, () -> {
			roomService.saveRoom(nullRoom);
		});
	}

	@Test
	void saveRooms_ShouldCreateMultipleRoomsAtOnce() {
		//given
		int oldRoomServiceNumberOfRooms = roomService.findRooms().size();
		RoomTO testRoom1 = new RoomTO(1, singleRoom.getCommodities());
		RoomTO testRoom2 = new RoomTO(1, singleRoom.getCommodities());
		RoomTO testRoom3 = new RoomTO(1, singleRoom.getCommodities());
		//when
		roomService.saveRooms(testRoom1, testRoom2, testRoom3);
		//then
		assertEquals(oldRoomServiceNumberOfRooms, (roomService.findRooms().size() - 3));
	}

	@Test
	void saveRooms_ShouldThrowException_WhenTheRoomToSaveHasInvalidValues() {
		//given
		Set<AbstractCommodityTO> commoditiesSetWithNullMembers = new HashSet<>(Arrays.asList(new BedTO(SINGLE), null, new ToiletTO()));
		RoomTO roomWithNullCommodity = new RoomTO(1, commoditiesSetWithNullMembers);
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
		RoomTO nullRoom = null;
		RoomTO nonExistentRoom = new RoomTO(236, singleRoom.getCommodities());
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
		RoomTO testRoomTO = new RoomTO(roomService.getRoomById(roomId).getRoomId(), singleRoom.getCommodities());
		List<BedType> testRoomBedTypes = getBedsFrom(testRoomTO);
		//when
		roomService.updateRoom(testRoomTO);
		//then
		assertNotEquals(oldRoomCapacity, roomService.getRoomById(roomId).getCommodities());
		assertEquals(1, testRoomBedTypes.size());
		assertTrue(testRoomBedTypes.contains(SINGLE));
		assertEquals(getNumberOfShowersFrom(testRoomTO), getNumberOfShowersFrom(roomService.getRoomById(roomId)));
		assertEquals(getNumberOfToiletsFrom(testRoomTO), getNumberOfToiletsFrom(roomService.getRoomById(roomId)));

	}

	@Test
	void updateRoom_ShouldThrowException_WhenThePassedRoomObjectHasInvalidValues() {
		//given
		Set<AbstractCommodityTO> commodities = new HashSet<>(Arrays.asList(new BedTO(SINGLE), null, new ToiletTO()));
		RoomTO nullRoom = null;
		RoomTO notPresentRoom = new RoomTO(234, singleRoom.getCommodities());
		//then
		assertThrows(ItemNotFoundException.class, () -> {
			roomService.updateRoom(nullRoom);
		});
		assertThrows(ItemNotFoundException.class, () -> {
			roomService.updateRoom(notPresentRoom);
		});
	}


	/**
	 * Converts a set of commodity transfer objects to a set of commodities.
	 *
	 * @param abstractCommodityTOS Transfer objects to be converted.
	 * @return returns new set of abstract commodities.
	 */
	private Set<AbstractCommodity> convertToAbstractCommodities(Set<AbstractCommodityTO> abstractCommodityTOS) {
		Set<AbstractCommodity> abstractCommodities = new HashSet<>();
		for (AbstractCommodityTO commodityTO : abstractCommodityTOS) {
			if (commodityTO instanceof BedTO) {
				abstractCommodities.add(new Bed(((BedTO) commodityTO).getBedType()));
			} else if (commodityTO instanceof ToiletTO) {
				abstractCommodities.add(new Toilet());
			} else if (commodityTO instanceof ShowerTO) {
				abstractCommodities.add(new Shower());
			}
		}
		return abstractCommodities;
	}

	/**
	 * Converts a room transfer object to a room object.
	 *
	 * @param roomTO transfer object to be converted.
	 * @return returns the newly generated room object.
	 */
	private Room convertToRoom(RoomTO roomTO) {
		return new Room(roomTO.getRoomId(), convertToAbstractCommodities(roomTO.getCommodities()));
	}

	/**
	 * Converts a number of room transfer objects into a list of room objects.
	 *
	 * @param roomTOS room transfer objects to be converted.
	 * @return returns the newly generated list of rooms.
	 */
	private List<Room> convertToRooms(RoomTO... roomTOS) {
		List<Room> roomList = new ArrayList<>();
		for (RoomTO roomTO : roomTOS) {
			roomList.add(convertToRoom(roomTO));
		}
		return roomList;
	}

	/**
	 * Used to extract the beds from a room.
	 *
	 * @param room A room that is used for a source of beds.
	 * @return returns the newly generated list of beds.
	 */
	private List<BedType> getBedsFrom(RoomTO room) {
		List<BedType> bedList = new ArrayList<>();
		for (AbstractCommodityTO commodity : room.getCommodities()) {
			if (commodity instanceof BedTO) {
				bedList.add(((BedTO) commodity).getBedType());
			}
		}
		return bedList;
	}

	private int getNumberOfToiletsFrom(RoomTO room) {
		int numberOfToilets = 0;
		for (AbstractCommodityTO commodity : room.getCommodities()) {
			if (commodity instanceof ToiletTO) {
				numberOfToilets++;
			}
		}
		return numberOfToilets;
	}

	private int getNumberOfShowersFrom(RoomTO room) {
		int numberOfShowers = 0;
		for (AbstractCommodityTO commodity : room.getCommodities()) {
			if (commodity instanceof ShowerTO) {
				numberOfShowers++;
			}
		}
		return numberOfShowers;
	}
}

