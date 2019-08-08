package eu.deltasource.internship.hotel.transferobject.commodityTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
	@JsonSubTypes.Type(value = BedTO.class, name = "BedTO"),
	@JsonSubTypes.Type(value = ToiletTO.class, name = "ToiletTO"),
	@JsonSubTypes.Type(value = ShowerTO.class, name = "ShowerTO")})
public class AbstractCommodityTO {
	protected int inventoryId;

	AbstractCommodityTO() {
	}

	public void setId(int id) {
		inventoryId = id;
	}

	public int getId() {
		return inventoryId;
	}

}
