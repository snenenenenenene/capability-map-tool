package com.bavostepbros.leap.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bavostepbros.leap.domain.service.roleservice.RoleService;
import com.bavostepbros.leap.domain.service.userservice.UserService;
import com.bavostepbros.leap.domain.model.User;
import com.bavostepbros.leap.domain.model.dto.UserDto;
import com.bavostepbros.leap.domain.service.emailservice.EmailService;

import com.bavostepbros.leap.configuration.jwtconfig.JwtUtility;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;


	@Autowired
	private EmailService emailService;

	@Autowired
	private JwtUtility jwtUtility;

	private static Logger log = LoggerFactory.getLogger(UserController.class);


	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public UserDto addUser(
			@ModelAttribute("username") String username,
			@ModelAttribute("password") String password,
			@ModelAttribute("email") String email,
			@ModelAttribute("roleId") Integer roleId) {		
		User user = userService.save(roleId, username, password, email);
		emailService.sendNewUserMessage(user.getEmail(), user.getPassword());
		return new UserDto(user.getUserId(), user.getRoleId(), user.getUsername(), user.getEmail(), user.getPassword());
	}
	
	@GetMapping("/{id}")
    public UserDto getUserById(
			@ModelAttribute("id") Integer id) {
		User user = userService.get(id);
		return new UserDto(user.getUserId(), user.getRoleId(), user.getUsername(), user.getEmail(), user.getPassword());
    }

    @GetMapping("/email")
	public UserDto getUserByEmail(
			@ModelAttribute("email") String email) {
		User user = userService.getByEmail(email);
		return new UserDto(user.getUserId(), user.getRoleId(), user.getUsername(), user.getEmail(), user.getPassword());
	}


	@GetMapping()
	public List<UserDto> getAllUsers() {
		List<User> users = userService.getAll();
		List<UserDto> usersDto = users.stream()
				.map(user -> new UserDto(user.getUserId(), user.getRoleId(), user.getUsername(), user.getEmail(), user.getPassword()))
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
		return new UserDto(user.getUserId(), user.getRoleId(), user.getUsername(), user.getEmail(), user.getPassword());
	}
	
	@DeleteMapping(path = "/{id}")
	public void deleteUser(@PathVariable("id") Integer id) {		
		userService.delete(id);
	}


	//TODO remove password from response and userDTO
	@PostMapping(value = "/authenticate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> authenticate(
				@ModelAttribute("email") String email,
				@ModelAttribute("password") String password) {
		try {
			userService.authenticate(email, password);
			User user = userService.getByEmail(email);
			return ResponseEntity.ok(jwtUtility.createToken(user.getEmail(), roleService.get(user.getRoleId())));
		} catch (AuthenticationException e) {
			return ResponseEntity.ok(e.getMessage() + " " + email + " " + password);
		}
	}

	@PutMapping(path = "changePassword", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String changePassword(
		@ModelAttribute("password") String password,
		@ModelAttribute("id") Integer id) {

		User user = userService.get(id);
		userService.update(id, user.getRoleId(), user.getUsername(), password, user.getEmail());
		return "Password saved";
	}
}
