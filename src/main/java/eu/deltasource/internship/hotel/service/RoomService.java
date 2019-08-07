package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Room;
import eu.deltasource.internship.hotel.domain.commodity.AbstractCommodity;
import eu.deltasource.internship.hotel.domain.commodity.Bed;
import eu.deltasource.internship.hotel.domain.commodity.Shower;
import eu.deltasource.internship.hotel.domain.commodity.Toilet;
import eu.deltasource.internship.hotel.exception.InvalidItemException;
import eu.deltasource.internship.hotel.exception.ItemNotFoundException;
import eu.deltasource.internship.hotel.repository.RoomRepository;
import eu.deltasource.internship.hotel.trasferObjects.RoomTO;
import eu.deltasource.internship.hotel.trasferObjects.commodityTOs.AbstractCommodityTO;
import eu.deltasource.internship.hotel.trasferObjects.commodityTOs.BedTO;
import eu.deltasource.internship.hotel.trasferObjects.commodityTOs.ShowerTO;
import eu.deltasource.internship.hotel.trasferObjects.commodityTOs.ToiletTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * The RoomService class has a singe private final member of RoomRepository type.
 * It represents the service, managing the RoomRepository.
 * <p>
 * Rooms can be created(saved) one by one or as a List.
 * <p>
 * Finding a specific room can be achieved by passing a room id, or
 * you can get the entire list of rooms.
 * <p>
 * Changes to the rooms can only be made via the updateRoom method , all other
 * methods like : findRooms,getRoomById return an unmodifiable version of the
 * object/s we are looking for.
 */
@Service
public class RoomService {

	private final RoomRepository roomRepository;

	/**
	 * Constructor using an already defined object's data to initialize.
	 *
	 * @param roomRepository An already created room repository.
	 */
	@Autowired
	public RoomService(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	/**
	 * Returns a room with the specified id.
	 *
	 * @param id Represents the sought after room's id.
	 * @return Returns the room with roomId = id.
	 */
	public RoomTO getRoomById(int id) {
		validateId(id);
		return convertToRoomTo(roomRepository.findById(id));
	}

	/**
	 * @return Returns all of the rooms in the repository.
	 */
	public List<RoomTO> findRooms() {
		return convertToRoomTOS(roomRepository.findAll());
	}

	/**
	 * Adds a room to the room repository.
	 *
	 * @param roomTO Represents the room to be added to the repository.
	 * @return Returns an reference to the added room.
	 */
	public RoomTO saveRoom(RoomTO roomTO) {
		validateRooms(roomTO);
		roomRepository.save(convertToRoom(roomTO));
		return convertToRoomTo(roomRepository.findById(roomRepository.count()));
	}

	/**
	 * Adds a number of rooms to the repository.
	 *
	 * @param roomTOS Room/s to be added.
	 */
	public void saveRooms(RoomTO... roomTOS) {
		validateRooms(roomTOS);
		roomRepository.saveAll(convertToRooms(roomTOS));
	}

	/**
	 * Removes a room from the repository.
	 *
	 * @param roomTO Represents the room to be removed.
	 * @return Returns boolean answer based on the outcome of the operation.
	 */
	public boolean deleteRoom(RoomTO roomTO) {
		validateRooms(roomTO);
		validateId(roomTO.getRoomId());
		return roomRepository.delete(convertToRoom(roomTO));
	}

	/**
	 * Removes the rooms with the specified id from the repository.
	 *
	 * @param id Represents the room the be removed's id.
	 * @return Returns boolean answer based on the outcome of the operation.
	 */
	public boolean deleteRoomById(int id) {
		validateId(id);
		return roomRepository.deleteById(id);
	}

	/**
	 * Updates the data in a room using a passed room's data as a source.
	 *
	 * @param roomTO Represents the source room.
	 * @return Returns a copy of the updated room.
	 */
	public RoomTO updateRoom(RoomTO roomTO) {
		validateRooms(roomTO);
		validateId(roomTO.getRoomId());
		return convertToRoomTo(roomRepository.updateRoom(convertToRoom(roomTO)));
	}

	/**
	 * Asserts if the information held in the room is valid, if not
	 * throws  exception.
	 *
	 * @param rooms Rooms to be validated.
	 */
	private void validateRooms(RoomTO... rooms) {
		for (RoomTO room : rooms) {
			if (room == null) {
				throw new ItemNotFoundException("room has null value !");
			}
			for (AbstractCommodityTO commodity : room.getCommodities()) {
				if (commodity == null) {
					throw new InvalidItemException("Commodity has null value !");
				}
			}
		}
	}

	private void validateId(int id) {
		if (id < 0 || (!roomRepository.existsById(id))) {
			throw new ItemNotFoundException("id has invalid value !");
		}
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
	 * Converts a set of commodities to a set of commodity transfer objects.
	 *
	 * @param abstractCommodities Transfer objects to be converted.
	 * @return returns new set of abstract commodities.
	 */
	private Set<AbstractCommodityTO> convertToAbstractCommodityTOS(Set<AbstractCommodity> abstractCommodities) {
		Set<AbstractCommodityTO> abstractCommodityTOS = new HashSet<>();
		for (AbstractCommodity commodity : abstractCommodities) {
			if (commodity instanceof Bed) {
				abstractCommodityTOS.add(new BedTO(((Bed) commodity).getBedType()));
			} else if (commodity instanceof Toilet) {
				abstractCommodityTOS.add(new ToiletTO());
			} else if (commodity instanceof Shower) {
				abstractCommodityTOS.add(new ShowerTO());
			}
		}
		return abstractCommodityTOS;
	}

	private RoomTO convertToRoomTo(Room room) {
		return new RoomTO(room.getRoomId(), convertToAbstractCommodityTOS(room.getCommodities()));
	}

	private List<RoomTO> convertToRoomTOS(List<Room> rooms) {
		List<RoomTO> toList = new ArrayList<>();
		for (Room room : rooms) {
			toList.add(convertToRoomTo(room));
		}
		return toList;
	}
}
