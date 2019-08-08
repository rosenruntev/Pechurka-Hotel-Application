package eu.deltasource.internship.hotel.trasferObjects;

import java.time.LocalDate;

public class BookingTO {
	private int bookingId;
	private int guestId;
	private int numberOfPeople;
	private LocalDate fromDate;
	private LocalDate toDate;

	public BookingTO() {
	}

	public BookingTO(int bookingId, int guestId, LocalDate fromDate, LocalDate toDate) {
		this.bookingId = bookingId;
		this.guestId = guestId;
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	public BookingTO(int bookingId, int guestId, int numberOfPeople, LocalDate fromDate, LocalDate toDate) {
		this.guestId = guestId;
		this.numberOfPeople = numberOfPeople;
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public int getGuestId() {
		return guestId;
	}

	public void setGuestId(int guestId) {
		this.guestId = guestId;
	}

	public int getNumberOfPeople() {
		return numberOfPeople;
	}

	public void setNumberOfPeople(int numberOfPeople) {
		this.numberOfPeople = numberOfPeople;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}
}
