package eu.deltasource.internship.hotel.transferobject;

import eu.deltasource.internship.hotel.exception.FailedInitializationException;
import eu.deltasource.internship.hotel.transferobject.commodityTOs.AbstractCommodityTO;
import eu.deltasource.internship.hotel.transferobject.commodityTOs.BedTO;

import java.util.HashSet;
import java.util.Set;

public class RoomTO {
	private int roomCapacity;
	private int roomId = 1;
	private Set<AbstractCommodityTO> commodities;

	public RoomTO() {
		commodities = new HashSet<>();
	}

	public RoomTO(int roomId, Set<AbstractCommodityTO> commodities) {
		this();
		setCommodities(commodities);
		setRoomId(roomId);
		roomCapacitySetter();
	}

	public Set<AbstractCommodityTO> getCommodities() {
		return commodities;
	}

	public void setCommodities(Set<AbstractCommodityTO> newCommodities) {
		this.commodities.clear();
		this.commodities.addAll(newCommodities);
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int newRoomId) {
		roomId = newRoomId;
	}

	public int getRoomCapacity() {
		return roomCapacity;
	}

	private void roomCapacitySetter() {
		roomCapacity = 0;
		for (AbstractCommodityTO commodity : commodities) {
			if (commodity instanceof BedTO) {
				roomCapacity += ((BedTO) commodity).getSize();
			}
		}
		if (roomCapacity == 0) {
			throw new FailedInitializationException("Room can not be empty");
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof RoomTO)) {
			return false;
		}
		return roomId == ((RoomTO) obj).roomId;
	}

	@Override
	public int hashCode() {
		return roomId;
	}

}
