package com.bavostepbros.leap.domain.service.userservice;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.model.User;
import com.bavostepbros.leap.domain.service.roleservice.RoleService;
import com.bavostepbros.leap.persistence.RoleDAL;
import com.bavostepbros.leap.persistence.UserDAL;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.ForeignKeyException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.UserException;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	private final UserDAL userDAL;

	@Autowired
	public UserServiceImpl(UserDAL userDAL) {
		super();
		this.userDAL = userDAL;
	}

	@Autowired
	private RoleService roleService;

	@Override
	public User save(Integer roleId, String username, String password, String email) {
		if (username == null || username.isBlank() || username.isEmpty() ||
			email == null || email.isEmpty() || email.isBlank() ||
			password == null || password.isEmpty() || password.isBlank()) {
			throw new InvalidInputException("Invalid input.");
		}
    	if (existsByUsername(username)) {
			throw new DuplicateValueException("Username already exists.");
		}
		if(existsByEmail(email)) {
			throw new DuplicateValueException("Email already exists.");
		}
		if(!roleService.existsById(roleId)) {
			throw new ForeignKeyException("Role ID is invalid.");
		}
		
    	User user = new User(username, roleId, password, email);
        return userDAL.save(user);
	}

	@Override
	public User get(Integer id) {
		if (id == null || id.equals(0)){
			throw new InvalidInputException("User ID is not valid.");
		}
		if (!existsById(id)){
			throw new IndexDoesNotExistException("User ID does not exist.");
		}
		User user = userDAL.findById(id).get();
		return user;
	}

	
	@Override
	public User getByEmail(String email) {
		if (email == null || email.isBlank() || email.isEmpty() || !existsByEmail(email)) {
			throw new InvalidInputException("Invalid input.");
		}
		if(!existsByEmail(email)){
			throw new UserException("Email does not exist.");
		}
		
		User user = userDAL.findByEmail(email);
		return user;
	}
	
	@Override
	public List<User> getAll() {
		List<User> users = new ArrayList<User>();
		users = userDAL.findAll();
		return users;
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
//		if (!userService.existsByUsername(user.getUsername())){
//			throw new DuplicateValueException("User name already exists.");
//		}
//		if (!userService.existsByEmail(user.getEmail())) {
//			throw new DuplicateValueException("Email already exists");
//		}
		if (!roleService.existsById(roleId)) {
			throw new ForeignKeyException("Role ID does not exist.");
		}

		User user = new User(userId, roleId, username, password, email);
		User updatedUser = userDAL.save(user);
		return updatedUser;
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

	@Override
	public boolean existsById(Integer id) {
		boolean result = userDAL.existsById(id);
		return result;
	}

	@Override
	public boolean existsByUsername(String username){
		boolean result = userDAL.findByUsername(username) == null;
		return !result;
	}

	@Override
	public boolean existsByEmail(String email){
		boolean result = userDAL.findByEmail(email) == null;
		return !result;
	}

}