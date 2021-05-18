package com.bavostepbros.leap.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bavostepbros.leap.domain.model.User;

public interface UserDAL extends JpaRepository<User, Integer> {
	List<User> findByUsername(String username);
	List<User> findByEmail(String email);
}