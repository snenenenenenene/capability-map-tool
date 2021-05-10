package com.bavostepbros.leap.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bavostepbros.leap.domain.model.Role;

public interface RoleDAL extends JpaRepository<Role, Integer> {
	List<Role> findByRoleName(String roleName);
};