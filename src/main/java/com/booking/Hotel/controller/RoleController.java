package com.booking.Hotel.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.booking.Hotel.exception.RoleAlreadyExistException;
import com.booking.Hotel.model.Role;
import com.booking.Hotel.model.User;
import com.booking.Hotel.service.IRoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
	private final IRoleService roleSer;
	
	@GetMapping("/all-roles")
	public ResponseEntity<List<Role>>getAllRoles(){
		return new ResponseEntity<>(roleSer.getRoles(),HttpStatus.FOUND);
	}
	@PostMapping("/create-new-role")
	public ResponseEntity<String>createRole(@RequestBody Role theRole){
		try {
			roleSer.createRole(theRole);
			return ResponseEntity.ok("New role created successfully");
		} catch (RoleAlreadyExistException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		}
	}
	@DeleteMapping("/delete/{roleId}")
	public void deleteRole(@PathVariable("roleId")Long roleId) {
		roleSer.deleteRole(roleId);
	}
	@PostMapping("/remove-all-users-from-role/{roleId}")
	public Role removeAllUsersFromRole(@PathVariable("roleId")Long roleId) {
		return roleSer.removeAllUserFromRole(roleId);
	}
	@PostMapping("/remove-user-from-role")
	public User removeUserFromRole(@RequestParam("userId") Long userId,@RequestParam("roleId") Long roleId) {
		return roleSer.removeUserFromRole(userId, roleId);
	}
	@PostMapping("/assign-user-to-role")
	public User assignRoleToUser(@RequestParam("userId") Long userId,@RequestParam("roleId") Long roleId) {
		return roleSer.assignRoleToUser(userId, roleId);
	}
}
