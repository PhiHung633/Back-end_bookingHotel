package com.booking.Hotel.service;

import java.util.List;

import com.booking.Hotel.model.User;

public interface IUserService {
	User registerUser(User user);
	List<User> getUsers();
	void deleteUser(String email);
	User getUser(String email);
}
