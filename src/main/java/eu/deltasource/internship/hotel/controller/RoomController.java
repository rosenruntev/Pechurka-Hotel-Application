package eu.deltasource.internship.hotel.controllers;

import eu.deltasource.internship.hotel.domain.Room;
import eu.deltasource.internship.hotel.domain.commodity.AbstractCommodity;
import eu.deltasource.internship.hotel.domain.commodity.Bed;
import eu.deltasource.internship.hotel.domain.commodity.Shower;
import eu.deltasource.internship.hotel.domain.commodity.Toilet;
import eu.deltasource.internship.hotel.service.RoomService;
import eu.deltasource.internship.hotel.transferobject.RoomTO;
import eu.deltasource.internship.hotel.transferobject.commodityTOs.AbstractCommodityTO;
import eu.deltasource.internship.hotel.transferobject.commodityTOs.BedTO;
import eu.deltasource.internship.hotel.transferobject.commodityTOs.ShowerTO;
import eu.deltasource.internship.hotel.transferobject.commodityTOs.ToiletTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The RoomController class has a singe private member of RoomService type with a room repository inside.
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
@RestController
@RequestMapping("RoomService/")
public class RoomController {
	private RoomService roomService;

	/**
	 * A RoomController constructor used to initialize the roomService property.
	 *
	 * @param roomService The roomService that is used as a data source for the initialization.
	 */
	@Autowired
	public RoomController(RoomService roomService) {
		this.roomService = roomService;
	}

	/**
	 * Returns the entire list of rooms.
	 *
	 * @return Returns a list of Room transfer objects.
	 */
	@RequestMapping(value = "findRooms/", method = RequestMethod.GET)
	public List<RoomTO> findRooms() {
		return convertToRoomTOS(roomService.findRooms());
	}

	/**
	 * Returns a room with roomId = id.
	 *
	 * @param id The sought after room's id.
	 * @return Returns a room transfer object,representing the sought after toom.
	 */
	@RequestMapping(value = "findRoom/{id}", method = RequestMethod.GET)
	public RoomTO findRoomById(@PathVariable int id) {
		return convertToRoomTO(roomService.getRoomById(id));
	}

	/**
	 * Creates a new room.
	 *
	 * @param roomTO A room transfer object,used as a data source for the new room.
	 * @return Returns a transfer object, representing the newly created room.
	 */
	@RequestMapping(value = "createRoom/", method = RequestMethod.POST)
	public RoomTO createRoom(@RequestBody RoomTO roomTO) {
		return convertToRoomTO(roomService.saveRoom(convertToRoom(roomTO)));
	}

	/**
	 * Creates a number of rooms.
	 *
	 * @param roomTOS Represents the rooms to be created.
	 */
	@RequestMapping(value = "createRooms/", method = RequestMethod.POST)
	public void createRooms(@RequestBody RoomTO... roomTOS) {
		roomService.saveRooms(convertToRooms(roomTOS));
	}

	/**
	 * Removes a room .
	 *
	 * @param roomTO A transfer object representing the room to be removed.
	 * @return Returns a boolean answer based on the outcome of the operation.
	 */
	@RequestMapping(value = "removeRoom/", method = RequestMethod.DELETE)
	public boolean removeRoom(@RequestBody RoomTO roomTO) {
		return roomService.deleteRoom(convertToRoom(roomTO));
	}

	/**
	 * Removes a room with roomId = id.
	 *
	 * @param id Represents the id of the room to be removed.
	 * @return Returns a boolean answer based on the outcome of the operation.
	 */
	@RequestMapping(value = "removeRoomById/{id}", method = RequestMethod.DELETE)
	public boolean removeRoomById(@PathVariable int id) {
		return roomService.deleteRoomById(id);
	}

	/**
	 * Updates the data of a room.
	 *
	 * @param roomTO A room transfer object, used as a data source for the room to be updated.
	 * @return Returns a copy of the room to be updated.
	 */
	@RequestMapping(value = "updateRoom/", method = RequestMethod.PUT,
		consumes = MediaType.APPLICATION_JSON_VALUE)
	public RoomTO updateRoom(@RequestBody RoomTO roomTO) {
		return convertToRoomTO(roomService.updateRoom(convertToRoom(roomTO)));
	}

	/**
	 * Converts a set of abstract commodity transfer objects to abstract commodity objects.
	 *
	 * @param abstractCommodityTOS The set to be converted.
	 * @return Returns the newly generated set of commodities.
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
	private Room[] convertToRooms(RoomTO... roomTOS) {
		Room[] roomArray;
		roomArray = new Room[roomTOS.length];
		for (int tosCounter = 0; tosCounter < roomTOS.length; tosCounter++) {
			roomArray[tosCounter] = convertToRoom(roomTOS[tosCounter]);
		}
		return roomArray;
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
				abstractCommodityTOS.add(new BedTO((((Bed) commodity).getBedType()), commodity.getInventoryId()));
			} else if (commodity instanceof Toilet) {
				abstractCommodityTOS.add(new ToiletTO(commodity.getInventoryId()));
			} else if (commodity instanceof Shower) {
				abstractCommodityTOS.add(new ShowerTO(commodity.getInventoryId()));
			}
		}
		return abstractCommodityTOS;
	}

	private List<RoomTO> convertToRoomTOS(List<Room> rooms) {
		List<RoomTO> toList = new ArrayList<>();
		for (eu.deltasource.internship.hotel.domain.Room room : rooms) {
			toList.add(convertToRoomTO(room));
		}
		return toList;
	}

	private RoomTO convertToRoomTO(Room room) {
		return new RoomTO(room.getRoomId(), convertToAbstractCommodityTOS(room.getCommodities()));
	}

}
