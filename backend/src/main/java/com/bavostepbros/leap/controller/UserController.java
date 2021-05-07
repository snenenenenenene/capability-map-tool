package com.bavostepbros.leap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.bavostepbros.leap.domain.service.roleservice.RoleService;
import com.bavostepbros.leap.domain.service.userservice.UserService;
import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.model.User;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class UserController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;
	
	@PostMapping(path = "/user/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> addUser(
			@ModelAttribute User user,
			UriComponentsBuilder builder) {		
		if (user.getUsername() == null || 
				user.getUsername().isBlank() ||
				user.getUsername().isEmpty() ||
				user.getPassword() == null ||
				user.getPassword().isBlank() ||
				user.getPassword().isEmpty()){
			throw new InvalidInputException("Invalid input.");
		}

		if(!userService.existsByUsername(user.getUsername())){
			throw new DuplicateValueException("Username already exists.");
		}

		boolean flag = userService.save(user);
		if (flag == false) {
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder
				.path("/user/get/{id}")
				.buildAndExpand(user.getUserId()).toUri());
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}
	
	@GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer id) {
		if (id == null || id.equals(0)){
			throw new InvalidInputException("User ID is not valid.");
		}
		if (!userService.existsById(id)){
			throw new IndexDoesNotExistException("User ID does not exist.");
		}
		
		User user = userService.get(id);
        return  new ResponseEntity<User>(user, HttpStatus.OK);
    }
	
	@GetMapping(path = "/user/all")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = userService.getAll();
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
	
	@PutMapping(path = "/user/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<User> updateUser(@ModelAttribute User user) {
		if (user.getUserId() == null ||
				user.getUserId().equals(0) ||
				user.getUsername() == null ||
				user.getUsername().isBlank() ||
				user.getUsername().isEmpty() ||
				user.getPassword() == null ||
				user.getPassword().isBlank() ||
				user.getPassword().isEmpty()){
			throw new InvalidInputException("Invalid input.");
		}
		if (!userService.existsById(user.getUserId())){
			throw new IndexDoesNotExistException("Can not update capability if it does not exist.");
		}
		if (!userService.existsByUsername(user.getUsername())){
			throw new DuplicateValueException("User name already exists.");
		}
		if (!roleService.existsById(user.getRoleId())) {
			throw new IndexDoesNotExistException("Role ID does not exist.");
		}
		
		userService.update(user);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@DeleteMapping("/user/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable("id") Integer id) {
		if (id == null || id.equals(0)){
			throw new InvalidInputException("User ID is not valid.");
		}
		if (!userService.existsById(id)) {
			throw new IndexDoesNotExistException("User ID does not exist.");
		}
		
		userService.delete(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
