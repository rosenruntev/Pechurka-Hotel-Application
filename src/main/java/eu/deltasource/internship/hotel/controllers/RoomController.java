package eu.deltasource.internship.hotel.controllers;

import eu.deltasource.internship.hotel.domain.Room;
import eu.deltasource.internship.hotel.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("RoomService/")
public class RoomController {
	private RoomService roomService;

	@Autowired
	public RoomController(RoomService roomService) {
		this.roomService = roomService;
	}

	@RequestMapping(value = "findRooms/", method = RequestMethod.GET)
	public List<Room> findRooms() {
		return Collections.unmodifiableList(roomService.findRooms());
	}

	@RequestMapping(value = "findById/{id}", method = RequestMethod.GET)
	public Room findRoomById(@PathVariable int id) {
		return roomService.getRoomById(id);
	}

	@RequestMapping(value = "createRoom/", method = RequestMethod.POST)
	public Room createRoom(@RequestBody Room room) {
		return roomService.saveRoom(room);
	}

	@RequestMapping(value = "createRooms/", method = RequestMethod.POST)
	public void createRooms(@RequestBody Room... rooms) {
		roomService.saveRooms(rooms);
	}

	@RequestMapping(value = "deleteRoom/", method = RequestMethod.DELETE)
	public boolean removeRoom(@RequestBody Room room) {
		return roomService.deleteRoom(room);
	}

	@RequestMapping(value = "deleteRoomById/{id}", method = RequestMethod.DELETE)
	public boolean removeRoomById(@PathVariable int id) {
		return roomService.deleteRoomById(id);
	}

	@RequestMapping(value = "updateRoom/", method = RequestMethod.PUT,
		consumes = MediaType.APPLICATION_JSON_VALUE)
	public Room updateRoom(@RequestBody Room room) {
		return roomService.updateRoom(room);
	}
}
