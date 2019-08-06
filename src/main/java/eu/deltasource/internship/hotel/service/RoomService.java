package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Room;
import eu.deltasource.internship.hotel.domain.commodity.AbstractCommodity;
import eu.deltasource.internship.hotel.exception.InvalidItemException;
import eu.deltasource.internship.hotel.exception.ItemNotFoundException;
import eu.deltasource.internship.hotel.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
	public Room getRoomById(int id) {
		validateId(id);
		return roomRepository.findById(id);
	}

	/**
	 * @return Returns all of the rooms in the repository.
	 */
	public List<Room> findRooms() {
		return roomRepository.findAll();
	}

	/**
	 * Adds a room to the room repository.
	 *
	 * @param room Represents the room to be added to the repository.
	 * @return Returns an reference to the added room.
	 */
	public Room saveRoom(Room room) {
		validateRooms(room);
		roomRepository.save(room);
		return roomRepository.findById(room.getRoomId());
	}

	/**
	 * Adds a number of rooms to the repository.
	 *
	 * @param rooms Room/s to be added.
	 */
	public void saveRooms(Room... rooms) {
		validateRooms(rooms);
		roomRepository.saveAll(rooms);
	}

	/**
	 * Removes a room from the repository.
	 *
	 * @param room Represents the room to be removed.
	 * @return Returns boolean answer based on the outcome of the operation.
	 */
	public boolean deleteRoom(Room room) {
		validateRooms(room);
		validateId(room.getRoomId());
		return roomRepository.delete(room);
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
	 * @param room Represents the source room.
	 * @return Returns a copy of the updated room.
	 */
	public Room updateRoom(Room room) {
		validateRooms(room);
		validateId(room.getRoomId());
		return roomRepository.updateRoom(room);
	}

	/**
	 * Asserts if the information held in the room is valid, if not
	 * throws  exception.
	 *
	 * @param rooms Rooms to be validated.
	 */
	private void validateRooms(Room... rooms) {
		for (Room room : rooms) {
			if (room == null) {
				throw new ItemNotFoundException("room has null value !");
			}
			for (AbstractCommodity commodity : room.getCommodities()) {
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


}
