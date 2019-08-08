package eu.deltasource.internship.hotel.transferobject;

import eu.deltasource.internship.hotel.domain.Gender;

public class GuestTO {
	private int guestId;
	private Gender gender;
	private String firstName;
	private String lastName;

	public GuestTO() {
	}

	public GuestTO(int guestId, String firstName, String lastName, Gender gender) {
		this();
		this.guestId = guestId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
	}

	public int getGuestId() {
		return guestId;
	}

	public void setGuestId(int newGuestId) {
		guestId = newGuestId;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender newGender) {
		gender = newGender;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String newFirstName) {
		firstName = newFirstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String newLastName) {
		lastName = newLastName;
	}
}
