package com.bavostepbros.leap.controller;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.bavostepbros.leap.configuration.jwtconfig.JwtUtility;
import com.bavostepbros.leap.domain.model.Role;
import com.bavostepbros.leap.domain.model.User;
import com.bavostepbros.leap.domain.model.dto.UserDto;
import com.bavostepbros.leap.domain.service.emailservice.EmailService;
import com.bavostepbros.leap.domain.service.roleservice.RoleService;
import com.bavostepbros.leap.domain.service.userservice.UserService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;
	
	@Autowired
    private RoleService roleService;

	@Autowired
	private JwtUtility jwtUtility;

	// private static Logger log = LoggerFactory.getLogger(UserController.class);

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public UserDto addUser(@ModelAttribute("username") String username, @ModelAttribute("email") String email) {
		String password = userService.generatePassword();

		User user = userService.save(username, password, email);
		emailService.sendNewUserMessage(user.getEmail(), password);
		return new UserDto(user.getUserId(), user.getUsername(), user.getEmail(), user.getPassword());
	}

	@PreAuthorize("hasAuthority('USER_ADMIN')")
	@GetMapping("/{id}")
	public UserDto getUserById(@ModelAttribute("id") Integer id) {
		User user = userService.get(id);
		return new UserDto(user.getUserId(), user.getUsername(), user.getEmail(), user.getPassword());
	}

	@PreAuthorize("hasAuthority('USER_ADMIN')")
	@GetMapping("/email/{email}")
	public UserDto getUserByEmail(@ModelAttribute("email") String email) {
		User user = userService.getByEmail(email);
		return new UserDto(user.getUserId(), user.getUsername(), user.getEmail(), user.getPassword());
	}

	@PreAuthorize("hasAuthority('USER_ADMIN')")
	@GetMapping()
	public List<UserDto> getAllUsers() {
		List<User> users = userService.getAll();
		List<UserDto> usersDto = users.stream().map(user -> new UserDto(user.getUserId(),
				user.getUsername(), user.getEmail(), user.getPassword())).collect(Collectors.toList());
		return usersDto;
	}

	@PreAuthorize("hasAuthority('USER_ADMIN')")
	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public UserDto updateUser(@ModelAttribute("userId") Integer userId, @ModelAttribute("username") String username,
			@ModelAttribute("password") String password, @ModelAttribute("email") String email) {

		User user = userService.update(userId, username, password, email);
		return new UserDto(user.getUserId(), user.getUsername(), user.getEmail(), user.getPassword());
	}

	@PreAuthorize("hasAuthority('USER_ADMIN')")
	@DeleteMapping(path = "/{id}")
	public void deleteUser(@PathVariable("id") Integer id) {
		userService.delete(id);
	}

	// TODO remove password from response and userDTO
	@PostMapping(value = "/authenticate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> authenticate(@ModelAttribute("email") String email,
			@ModelAttribute("password") String password) {
		try {
			userService.authenticate(email, password);
			User user = userService.getByEmail(email);
			System.out.println("User in authenticate: " + user.getUsername());
			Iterator<Role> roles = user.getRoles().iterator();
			Role role = roles.hasNext() ? roles.next() : roleService.getRoleByRoleName("CREATING_USER");
			System.out.println("Role in authenticate: " + role.getRoleName());
			return ResponseEntity.ok(jwtUtility.createToken(user.getEmail(), role));
		} catch (AuthenticationException e) {
			return ResponseEntity.ok(e.getMessage());
		}
	}

	@PreAuthorize("hasAuthority('USER_ADMIN') or hasAuthority('APP_ADMIN') or hasAuthority('CREATING_USER') or hasAuthority('VIEWING_USER')")
	@PutMapping(path = "changePassword", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String changePassword(@ModelAttribute("password") String password, @ModelAttribute("id") Integer id) {

		User user = userService.get(id);
		userService.update(id, user.getUsername(), password, user.getEmail());
		return "Password saved";
	}
}
