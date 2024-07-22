package com.booking.Hotel.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.booking.Hotel.exception.InvalidBookingRequestException;
import com.booking.Hotel.exception.ResourceNotFoundException;
import com.booking.Hotel.model.BookedRoom;
import com.booking.Hotel.model.Room;
import com.booking.Hotel.repository.BookingRepository;
import com.booking.Hotel.response.RoomResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {

	private final BookingRepository bookingRepo;
	
	private final IRoomService roomSer;
	
	@Override
	public List<BookedRoom> getAllBookings() {
		return bookingRepo.findAll();
	}
	
	public List<BookedRoom> getAllBookingsByRoomID(Long roomId) {
		return bookingRepo.findByRoomId(roomId);
	}

	@Override
	public void cancelBooking(Long bookingId) {
		bookingRepo.deleteById(bookingId);
	}

	@Override
	public String saveBooking(Long roomId, BookedRoom bookingRequest) {
		if(bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
			throw new InvalidBookingRequestException("Check-in date must before check-out date");
		}
		Room room=roomSer.getRoomById(roomId).get();
		List<BookedRoom> existingBookings=room.getBookings();
		boolean roomIsAvailable=roomIsAvailable(bookingRequest,existingBookings);
		if(roomIsAvailable)
		{
			room.addBooking(bookingRequest);
			bookingRepo.save(bookingRequest);
		}
		else {
			throw new InvalidBookingRequestException("Sorry, This room is not available for the selected dates;");
		}
		return bookingRequest.getBookingConfirmationCode();
	}

	@Override
	public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
		return bookingRepo.findByBookingConfirmationCode(confirmationCode).orElseThrow(()->new ResourceNotFoundException("No booking found with booking code: " + confirmationCode));
	}

	private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
		return existingBookings.stream()
				.noneMatch(existingBooking->
							bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
							||bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
							||(bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())&&
									bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
							||(bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
									&&bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
							||(bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
									&&bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))
							||(bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
									&&bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))
							||(bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
									&&bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))
							);
	}

	@Override
	public List<BookedRoom> getBookingsByUserEmail(String email) {
		return bookingRepo.findByGuestEmail(email);
	}
	

}
