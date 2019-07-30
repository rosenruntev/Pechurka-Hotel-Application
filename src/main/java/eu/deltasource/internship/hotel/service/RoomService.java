package eu.deltasource.internship.hotel.service;

import eu.deltasource.internship.hotel.domain.Room;
import eu.deltasource.internship.hotel.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Taner Ilyazov - Delta Source Bulgaria on 2019-07-28.
 */
@Service
public class RoomService {

	private final RoomRepository roomRepository;

	/**
	 * Constructor using an already defined object's data to initialize.
	 * @param roomRepository An already created room repository.
	 */
	public RoomService(RoomRepository roomRepository) {
		this.roomRepository = roomRepository;
	}

	/**
	 * Returns a room with the specified id.
	 * @param id Represents the sought after room's id.
	 * @return Returns the sought after room.
	 */
	public Room getRoomById(int id) {
		return roomRepository.findById(id);
	}

	/**
	 *
	 * @return Returns all of the rooms in the repository.
	 */
	public List<Room> findRooms() {
		return roomRepository.findAll();
	}

	/**
	 * Adds a room to the room repository.
	 * @param room Represents the room to be added to the repository.
	 * @return Returns an reference to the added room.
	 */
	public Room saveRoom(Room room) {
		roomRepository.save(room);
		return roomRepository.findById(room.getRoomId());
	}

	/**
	 * Adds a number of rooms to the repository.
	 * @param rooms Room/s to be added.
	 */
	public void saveRooms(Room... rooms) {
		roomRepository.saveAll(rooms);
	}

	/**
	 * Removes a room from the repository.
	 * @param room Represents the room to be removed.
	 * @return Returns boolean answer based on the outcome of the operation.
	 */
	public boolean deleteRoom(Room room) {
		return roomRepository.delete(room);
	}

	/**
	 * Removes the rooms with the specified id from the repository.
	 * @param id Represents the room the be removed's id.
	 * @return Returns boolean answer based on the outcome of the operation.
	 */
	public boolean deleteRoomById(int id) {
		return roomRepository.deleteById(id);
	}

	/**
	 * Updates the data in a room using a passed room's data as a source.
	 * @param room Represents the source room.
	 * @return Returns a copy of the updated room.
	 */
	public Room updateRoom(Room room) {
		return roomRepository.updateRoom(room);
	}
}
