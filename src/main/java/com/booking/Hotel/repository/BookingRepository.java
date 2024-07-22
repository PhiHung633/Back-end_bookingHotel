package com.booking.Hotel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.booking.Hotel.model.BookedRoom;

@Repository
public interface BookingRepository extends JpaRepository<BookedRoom, Long> {
	
	Optional<BookedRoom>findByBookingConfirmationCode(String confirmationCode);

	List<BookedRoom> findByRoomId(Long roomId);

	List<BookedRoom> findByGuestEmail(String email);

}
