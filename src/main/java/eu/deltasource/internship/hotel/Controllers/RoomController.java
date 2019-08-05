package eu.deltasource.internship.hotel.Controllers;

import eu.deltasource.internship.hotel.domain.Room;
import eu.deltasource.internship.hotel.service.RoomService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class RoomController {
	private RoomService roomService;

	private RoomController() {
	}

	public RoomController(RoomService roomService) {
		this.roomService = roomService;
	}

	@RequestMapping("RoomService/findRooms")
	public List<Room> findRooms() {
		return Collections.unmodifiableList(roomService.findRooms());
	}

	@RequestMapping("RoomService/findById/{id}")
	public Room findById(@PathVariable int id) {
		return roomService.getRoomById(id);
	}

	@RequestMapping(value = "RoomService/updateGuest", method = RequestMethod.POST)
	public Room CreateRoom(Room room) {
		return roomService.saveRoom(room);
	}

	@RequestMapping(value = "RoomService/updateGuest", method = RequestMethod.POST)
	public void CreateRooms(Room... rooms) {
		roomService.saveRooms(rooms);
	}

	@RequestMapping("RoomService/deleteRoom")
	public boolean removeRoom(Room room) {
		return roomService.deleteRoom(room);
	}

	@RequestMapping("RoomService/deleteRoomById/{id}")
	public boolean removeRoomById(@PathVariable int id) {
		return roomService.deleteRoomById(id);
	}

	@RequestMapping("RoomService/updateRoom")
	public Room updateRoom(Room room) {
		return roomService.updateRoom(room);
	}
}
