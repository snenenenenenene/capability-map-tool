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

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.bavostepbros.leap.domain.service.roleservice.RoleService;
import com.bavostepbros.leap.domain.service.userservice.UserService;
import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.model.User;
import com.bavostepbros.leap.domain.model.dto.StatusDto;
import com.bavostepbros.leap.domain.model.dto.UserDto;
import com.bavostepbros.leap.persistence.UserDAL;

import com.bavostepbros.leap.configuration.JWTConfig.JwtTokenProvider;

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

	@Autowired
	private UserDAL userDAL;

	private static Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider tokenProvider;
	
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

	@PostMapping(value = "/authenticate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> authenticate(
				@ModelAttribute("email") String email,
				@ModelAttribute("password") String password) {
		User user = userService.getByEmail(email);

		log.info("UserResourceImpl : authenticate");
		JSONObject jsonObject = new JSONObject();
		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
			if (authentication.isAuthenticated()) {
				jsonObject.put("name", authentication.getName());
				jsonObject.put("authorities", authentication.getAuthorities());
				jsonObject.put("token", tokenProvider.createToken(email, roleService.get(user.getRoleId())));
				return new ResponseEntity<String>(jsonObject.toString(), HttpStatus.OK);
			}
		} catch (JSONException e) {
			try {
				jsonObject.put("exception", e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			return new ResponseEntity<String>(jsonObject.toString(), HttpStatus.UNAUTHORIZED);
		}
		return null;
	}

	@PutMapping(path = "changePassword", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String changePassword(
		@ModelAttribute("password") String password,
		@ModelAttribute("id") Integer id) {

		User user = userService.get(id);
		String username = user.getUsername();
		String email = user.getEmail();
		Integer roleId = user.getRoleId();
		userService.update(id, roleId, username, password, email);
		String result = "Password saved";
		return result;
	}
}
