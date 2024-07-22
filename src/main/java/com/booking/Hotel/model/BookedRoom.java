package com.booking.Hotel.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookedRoom {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookingId;
	
	@Column(name="check_In")
	private LocalDate checkInDate;
	
	@Column(name="check_Out")
	private LocalDate checkOutDate;
	
	@Column(name="Guest_FullName")
	private String guestFullName;
	
	@Column(name="Guest_Email")
	private String guestEmail;
	
	@Column(name="children")
	private int NumofChild;
	
	@Column(name="adults")
	private int NumofAdult;
	
	@Column(name="total_guest")
	private int totalNumOfGuest;
	

	
	@Column(name="confirmation_Code")
	private String bookingConfirmationCode;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="room_id")
	private Room room;
	
	public void calculateTotalNumberOfGuest() {
		this.totalNumOfGuest=this.NumofAdult+this.NumofChild;
	}

	public int getNumofChild() {
		return NumofChild;
	}

	public void setNumofChild(int numofChild) {
		NumofChild = numofChild;
		calculateTotalNumberOfGuest();
	}

	public int getNumofAdult() {
		return NumofAdult;
	}

	public void setNumofAdult(int numofAdult) {
		NumofAdult = numofAdult;
		calculateTotalNumberOfGuest();
	}

	public String getBookingConfirmationCode() {
		return bookingConfirmationCode;
	}

	public void setBookingConfirmationCode(String bookingConfirmationCode) {
		this.bookingConfirmationCode = bookingConfirmationCode;
	}
	
	
}
