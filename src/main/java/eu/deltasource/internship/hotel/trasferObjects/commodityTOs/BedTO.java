package eu.deltasource.internship.hotel.trasferObjects.commodityTOs;

import eu.deltasource.internship.hotel.domain.commodity.BedType;

public class BedTO extends AbstractCommodityTO {
	private BedType bedType;

	public BedTO() {
		super();
	}
	public BedTO(BedType bedType){
		this();
		setBedType(bedType);
	}

	public BedType getBedType() {
		return bedType;
	}
	public int getSize() {
		return bedType.getSize();
	}

	public void setBedType(BedType bedType) {
		this.bedType = bedType;
	}
}
