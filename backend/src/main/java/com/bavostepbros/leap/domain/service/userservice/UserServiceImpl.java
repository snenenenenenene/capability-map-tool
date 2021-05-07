package com.bavostepbros.leap.domain.service.userservice;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.model.User;
import com.bavostepbros.leap.persistence.UserDAL;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	private final UserDAL userDAL;

	@Autowired
	public UserServiceImpl(UserDAL userDAL) {
		super();
		this.userDAL = userDAL;
	}

	@Override
	public Boolean save(User user) {
		try {
			userDAL.save(user);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public User get(Integer id) {
		User user = null;
		try {
			user = userDAL.findById(id).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	@Override
	public List<User> getAll() {
		List<User> users = new ArrayList<User>();
		try {
			users = userDAL.findAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}
	
	@Override
	public void update(User user) {
		try {
			userDAL.save(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(Integer id) {
		try {
			userDAL.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	@Override
	public boolean existsById(Integer id) {
		boolean result = userDAL.existsById(id);
		return result;
	}

	@Override
	public boolean existsByUsername(String username){
		boolean result = userDAL.findByUsername(username).isEmpty();
		return result;
	}
}