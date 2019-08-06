package eu.deltasource.internship.hotel.trasferObjects;

import eu.deltasource.internship.hotel.domain.commodity.BedType;

public class BedTO extends AbstractCommodityTO {
	private BedType bedType;


	public void setBedType(BedType bedType) {
		this.bedType = bedType;
	}

	public BedType getBedType() {
		return bedType;
	}

}
