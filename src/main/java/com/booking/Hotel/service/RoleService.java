package com.booking.Hotel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.booking.Hotel.exception.RoleAlreadyExistException;
import com.booking.Hotel.exception.UserAlreadyExistsException;
import com.booking.Hotel.model.Role;
import com.booking.Hotel.model.User;
import com.booking.Hotel.repository.RoleRepository;
import com.booking.Hotel.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
	
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;

	@Override
	public List<Role> getRoles() {
		return roleRepository.findAll();
	}

	@Override
	public Role createRole(Role theRole) {
		String roleName = "ROLE_" + theRole.getName().toUpperCase();
		if (roleRepository.existsByName(roleName)) {
			throw new RoleAlreadyExistException(theRole.getName() + " role already exists");
		}
		Role role = new Role(roleName);
		return roleRepository.save(role);
	}

	@Override
	public void deleteRole(Long id) {
		this.removeAllUserFromRole(id);
		roleRepository.deleteById(id);
	}

	@Override
	public Role findByName(String name) {
		return roleRepository.findByName(name).get();
	}

	@Override
	public User removeUserFromRole(Long userId, Long roleId) {
		Optional<User> user = userRepository.findById(userId);
		Optional<Role> role = roleRepository.findById(roleId);
		if (role.isPresent() && role.get().getUsers().contains(user.get())) {
			role.get().removeUserFromRole(user.get());
			roleRepository.save(role.get());
			return user.get();
		}
		throw new UsernameNotFoundException("User not found");
	}

	@Override
	public User assignRoleToUser(Long userId, Long roleId) {
		Optional<User> user = userRepository.findById(userId);
		Optional<Role> role = roleRepository.findById(roleId);
		if (user.isPresent() && user.get().getRoles().contains(role.get())) {
			throw new UserAlreadyExistsException(user.get().getFirstName() + " is already assigned to the " + role.get().getName() + " role");
		}
		if (role.isPresent()) {
			role.get().assignRoleToUser(user.get());
			roleRepository.save(role.get());
		}
		return user.get();
	}

	@Override
	public Role removeAllUserFromRole(Long roleId) {
		Optional<Role> role = roleRepository.findById(roleId);
		role.get().removeAllUserFromRole();
		return roleRepository.save(role.get());
	}
}
