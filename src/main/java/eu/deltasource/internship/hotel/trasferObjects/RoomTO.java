package eu.deltasource.internship.hotel.trasferObjects;

import java.util.HashSet;
import java.util.Set;

public class RoomTO {
	private int roomId;
	private Set<AbstractCommodityTO> commodities;

	RoomTO(){
		commodities = new HashSet<>();
	}

}
