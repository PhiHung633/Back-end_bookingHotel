package com.booking.Hotel.service;

import java.util.List;

import com.booking.Hotel.model.Role;
import com.booking.Hotel.model.User;

public interface IRoleService {
	List<Role>getRoles();
	Role createRole(Role theRole);
	void deleteRole(Long id);
	Role findByName(String name);
	User removeUserFromRole(Long userId, Long roleId);
	User assignRoleToUser(Long userId, Long roleId);
	Role removeAllUserFromRole(Long roleId);
}
