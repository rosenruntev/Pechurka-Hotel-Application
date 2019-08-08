package eu.deltasource.internship.hotel.trasferObjects;

import java.time.LocalDate;

public class BookingTO {
	private int bookingId;
	private int guestId;
	private LocalDate fromDate;
	private LocalDate toDate;

	public BookingTO(int bookingId, int guestId, LocalDate fromDate, LocalDate toDate) {
		this.bookingId = bookingId;
		this.guestId = guestId;
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
