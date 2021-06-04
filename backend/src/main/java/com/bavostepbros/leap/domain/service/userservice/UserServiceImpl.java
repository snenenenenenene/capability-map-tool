package com.bavostepbros.leap.domain.service.userservice;

import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.model.User;
import com.bavostepbros.leap.domain.service.roleservice.RoleService;
import com.bavostepbros.leap.persistence.UserDAL;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.ForeignKeyException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.UserException;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDAL userDAL;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleService roleService;

	@PostConstruct
	private void init() {
		save(2, "super_admin", "super_admin", "super_admin");
	}

	@Override
	public User save(Integer roleId, String username, String password, String email) {
		if (username == null 
			|| username.isBlank() 
			|| username.isEmpty() 
			|| email == null 
			|| email.isEmpty() 
			|| email.isBlank() 
			|| password == null 
			|| password.isEmpty() 
			|| password.isBlank()) {
			throw new InvalidInputException("Invalid input.");
		}
    	if (!existsByUsername(username)) {
			throw new DuplicateValueException("Username already exists.");
		}
		if(existsByEmail(email)) {
			throw new DuplicateValueException("Email already exists.");
		}
		if(!roleService.existsById(roleId)) {
			throw new ForeignKeyException("Role ID is invalid.");
		}
    	return userDAL.save(new User(username, roleId, passwordEncoder.encode(password), email));
	}

	@Override
	public User get(Integer id) {
		if (id == null || id.equals(0)){
			throw new InvalidInputException("User ID is not valid.");
		}
		if (!existsById(id)){
			throw new IndexDoesNotExistException("User ID does not exist.");
		}
		return userDAL.findById(id).get();
	}

	
	@Override
	public User getByEmail(String email) {
		if (email == null || email.isBlank() || email.isEmpty() || !existsByEmail(email)) {
			throw new InvalidInputException("Invalid input.");
		}
		if(!existsByEmail(email)){
			throw new UserException("Email does not exist.");
		}
		return userDAL.findByEmail(email);
	}
	
	@Override
	public List<User> getAll() {
		return userDAL.findAll();
	}
	
	@Override
	public User update(Integer userId, Integer roleId, String username, String password, String email) {
		if (userId == null ||
				userId.equals(0) ||
				username == null ||
				username.isBlank() ||
				username.isEmpty() ||
				password == null ||
				password.isBlank() ||
				password.isEmpty() ||
				email == null ||
				email.isBlank() ||
				email.isEmpty()){
			throw new InvalidInputException("Invalid input.");
		}
		if (!existsById(userId)){
			throw new UserException("Can not update user if it does not exist.");
		}

		if (!roleService.existsById(roleId)) {
			throw new ForeignKeyException("Role ID does not exist.");
		}
		return userDAL.save(new User(userId, roleId, username, passwordEncoder.encode(password), email));
	}

	@Override
	public void delete(Integer id) {
		if (id == null || id.equals(0)) {
			throw new InvalidInputException("User ID does not exist.");
		}
		if (!existsById(id)) {
			throw new IndexDoesNotExistException("User ID does not exist.");
		}

		userDAL.deleteById(id);
	}	

	//TODO fix exception handling here or in controller?
	@Override
	public Authentication authenticate(String email, String password) throws AuthenticationException {
		return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
	}

	@Override
	public boolean existsById(Integer id) {
		return userDAL.findById(id).isPresent();
	}

	@Override
	public boolean existsByUsername(String username){
		return !(userDAL.findByUsername(username) == null);
	}

	@Override
	public boolean existsByEmail(String email){
		return !(userDAL.findByEmail(email) == null);
	}

	@Override
	public String generatePassword(){
		String generatedPassword;

		int lowerLimit = 48; // numeral '0'
  	    int higherLimit = 122; // letter 'z'
	    int passwordLength = 10;
	    Random random = new Random();

	    generatedPassword = random.ints(lowerLimit, higherLimit + 1)
	      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
	      .limit(passwordLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();
		
		return generatedPassword;
	}

}