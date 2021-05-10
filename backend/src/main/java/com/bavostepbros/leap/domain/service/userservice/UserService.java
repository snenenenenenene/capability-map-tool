package com.bavostepbros.leap.domain.service.userservice;

import java.util.List;

import com.bavostepbros.leap.domain.model.User;

public interface UserService {
	Boolean save(User user);
	User get(Integer id);
	List<User> getAll();
	void update(User user);
	void delete(Integer id);
	boolean existsById(Integer id);
	boolean existsByUsername(String username);
}