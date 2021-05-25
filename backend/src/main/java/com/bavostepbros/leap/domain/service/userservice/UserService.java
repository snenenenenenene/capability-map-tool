package com.bavostepbros.leap.domain.service.userservice;

import java.util.List;

import com.bavostepbros.leap.domain.model.User;

public interface UserService {
	User save(Integer roleId, String username, String password, String email);
	User get(Integer id);
	User getByEmail(String email);
	List<User> getAll();
	User update(Integer userId, Integer roleId, String username, String password, String email);
	void delete(Integer id);
	boolean existsById(Integer id);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
}