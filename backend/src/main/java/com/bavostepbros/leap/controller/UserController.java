package com.bavostepbros.leap.controller;

import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.bavostepbros.leap.domain.service.roleservice.RoleService;
import com.bavostepbros.leap.domain.service.userservice.UserService;
import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.model.User;
import com.bavostepbros.leap.domain.model.dto.StatusDto;
import com.bavostepbros.leap.domain.model.dto.UserDto;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/")
public class UserController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;
	
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public UserDto addUser(
			@ModelAttribute("username") String username,
			@ModelAttribute("password") String password,
			@ModelAttribute("email") String email,
			@ModelAttribute("roleId") Integer roleId) {		
		User user = userService.save(roleId, username, password, email);
		return new UserDto(user.getUserId(), user.getRoleId(), user.getUsername(), user.getPassword(), user.getEmail());
	}
	
	@GetMapping("{id}")
    public UserDto getUserById(
			@ModelAttribute("id") Integer id) {
		User user = userService.get(id);
        return  new UserDto(user.getUserId(), user.getRoleId(), user.getUsername(), user.getPassword(), user.getEmail());
    }
	
	@GetMapping()
	public List<UserDto> getAllUsers() {
		List<User> users = userService.getAll();
		List<UserDto> usersDto = users.stream()
				.map(user -> new UserDto(user.getUserId(), user.getRoleId(), user.getUsername(), user.getPassword(), user.getEmail()))
				.collect(Collectors.toList());
		return usersDto;
	}
	
	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public UserDto updateUser(
			@ModelAttribute("userId") Integer userId,
			@ModelAttribute("username") String username,
			@ModelAttribute("password") String password,
			@ModelAttribute("email") String email,
			@ModelAttribute("roleId") Integer roleId) {
		
		User user = userService.update(userId, roleId, username, password, email);
		return new UserDto(user.getUserId(), user.getRoleId(), user.getUsername(), user.getPassword(), user.getEmail());
	}
	
	@DeleteMapping(path = "{id}")
	public void deleteUser(@PathVariable("id") Integer id) {		
		userService.delete(id);
	}

	@PostMapping(path = "authenticate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public User login(
		@ModelAttribute("email") String email) {
		User user = userService.getByEmail(email);
//		return new UserDto(user.getUserId(), user.getRoleId(), user.getUsername(), user.getPassword(), user.getEmail());
		return user;
	}

	@PutMapping(path = "changePassword", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String changePassword(
		@ModelAttribute("password") String password,
		@ModelAttribute("id") Integer id) {

		User user = userService.get(id);
		String username = user.getUsername();
		String email = user.getEmail();
		Integer roleId = user.getRoleId();
		userService.save(roleId, username, password, email);
		String result = "Password saved";
		return result;
	}
}
