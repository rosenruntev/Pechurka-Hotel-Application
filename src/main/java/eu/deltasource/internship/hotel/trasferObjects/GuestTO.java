package eu.deltasource.internship.hotel.trasferObjects;

import eu.deltasource.internship.hotel.domain.Gender;

public class GuestTO {
	private int guestId;
	private Gender gender;
	private String firstName;
	private String lastName;

	public int getGuestId() {
		return guestId;
	}

	public void setGuestId(int newGuestId){
		guestId = newGuestId;
	}

	public Gender getGender() {
		return gender;
	}

	public  void setGender(Gender newGender){
		gender = newGender;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String newFirstName){
		firstName = newFirstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String newLastName){
		lastName = newLastName;
	}
}
