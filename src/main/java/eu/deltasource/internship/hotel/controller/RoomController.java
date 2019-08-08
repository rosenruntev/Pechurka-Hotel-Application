package eu.deltasource.internship.hotel.controller;

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

@RestController
@RequestMapping("RoomService/")
public class RoomController {
	private RoomService roomService;

	@Autowired
	public RoomController(RoomService roomService) {
		this.roomService = roomService;
	}

	@RequestMapping(value = "findRooms/", method = RequestMethod.GET)
	public List<RoomTO> findRooms() {
		return convertToRoomTOS(roomService.findRooms());
	}

	@RequestMapping(value = "findRoom/{id}", method = RequestMethod.GET)
	public RoomTO findRoomById(@PathVariable int id) {
		return convertToRoomTO(roomService.getRoomById(id));
	}

	@RequestMapping(value = "createRoom/", method = RequestMethod.POST)
	public RoomTO createRoom(@RequestBody RoomTO roomTO) {
		return convertToRoomTO(roomService.saveRoom(convertToRoom(roomTO)));
	}

	@RequestMapping(value = "createRooms/", method = RequestMethod.POST)
	public void createRooms(@RequestBody RoomTO... roomTOS) {
		roomService.saveRooms(convertToRooms(roomTOS));
	}

	@RequestMapping(value = "removeRoom/", method = RequestMethod.DELETE)
	public boolean removeRoom(@RequestBody RoomTO roomTO) {
		return roomService.deleteRoom(convertToRoom(roomTO));
	}

	@RequestMapping(value = "removeRoomById/{id}", method = RequestMethod.DELETE)
	public boolean removeRoomById(@PathVariable int id) {
		return roomService.deleteRoomById(id);
	}

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
				abstractCommodityTOS.add(new BedTO(((Bed) commodity).getBedType()));
			} else if (commodity instanceof Toilet) {
				abstractCommodityTOS.add(new ToiletTO());
			} else if (commodity instanceof Shower) {
				abstractCommodityTOS.add(new ShowerTO());
			}
		}
		return abstractCommodityTOS;
	}

	private RoomTO convertToRoomTo(eu.deltasource.internship.hotel.domain.Room room) {
		return new RoomTO(room.getRoomId(), convertToAbstractCommodityTOS(room.getCommodities()));
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
