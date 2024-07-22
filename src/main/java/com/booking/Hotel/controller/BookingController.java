package com.booking.Hotel.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booking.Hotel.exception.InvalidBookingRequestException;
import com.booking.Hotel.exception.ResourceNotFoundException;
import com.booking.Hotel.model.BookedRoom;
import com.booking.Hotel.model.Room;
import com.booking.Hotel.response.BookingResponse;
import com.booking.Hotel.response.RoomResponse;
import com.booking.Hotel.service.IBookingService;
import com.booking.Hotel.service.IRoomService;

import lombok.RequiredArgsConstructor;

@CrossOrigin("http://localhost:5173")
@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {
	
	private final IBookingService bookingSer;
	
	private final IRoomService roomSer;
	
	@GetMapping("/all-bookings")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<BookingResponse>> getAllBookings()
	{
		List<BookedRoom> bookings=bookingSer.getAllBookings();
		List<BookingResponse> bookingResponses=new ArrayList<>();
		for(BookedRoom booking:bookings)
		{
			BookingResponse bookingResponse=getBookingResponse(booking);
			bookingResponses.add(bookingResponse);
		}
		return ResponseEntity.ok(bookingResponses);
	}
	@GetMapping("/confirmation/{confirmationCode}")
	public ResponseEntity<?>getBookingByConfirmationCode(@PathVariable String confirmationCode)
	{
		try {
			BookedRoom  booking=bookingSer.findByBookingConfirmationCode(confirmationCode);
			BookingResponse bookingResponse=getBookingResponse(booking);
			return ResponseEntity.ok(bookingResponse);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	@GetMapping("/user/{email}/bookings")
	public ResponseEntity<List<BookingResponse>> getBookingsByUserEmail(@PathVariable String email){
		List<BookedRoom> bookings=bookingSer.getBookingsByUserEmail(email);
		List<BookingResponse>bookingResponses=new ArrayList<>();
		for(BookedRoom booking:bookings) {
			BookingResponse bookingResponse=getBookingResponse(booking);
			bookingResponses.add(bookingResponse);
		}
		return ResponseEntity.ok(bookingResponses);
	}
	
	@PostMapping("/room/{roomId}/booking")
	public ResponseEntity<?> saveBooking(@PathVariable Long roomId,@RequestBody BookedRoom bookingRequest)
	{
		try {
			String confiramationCode=bookingSer.saveBooking(roomId,bookingRequest);
			return ResponseEntity.ok("Room booked successfully ! Your booking confiramation code is:"+confiramationCode);
		} catch (InvalidBookingRequestException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@DeleteMapping("/booking/{bookingId}/delete")
	public void cancelBooking(@PathVariable Long bookingId) {
		bookingSer.cancelBooking(bookingId);
	}
	
	private BookingResponse getBookingResponse(BookedRoom booking) {
		Room theRoom=roomSer.getRoomById(booking.getRoom().getId()).get();
		RoomResponse room=new RoomResponse(theRoom.getId(),theRoom.getRoomType(),theRoom.getRoomPrice());
		return new BookingResponse(booking.getBookingId(),booking.getCheckInDate(),booking.getCheckOutDate(),
				booking.getGuestFullName(),booking.getGuestEmail(),
				booking.getNumofAdult(),booking.getNumofChild(),booking.getTotalNumOfGuest(),
				booking.getBookingConfirmationCode(),room);
	}
}
