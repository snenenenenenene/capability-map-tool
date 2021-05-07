package com.bavostepbros.leap.domain.service.roleservice;

import java.util.List;

import com.bavostepbros.leap.domain.model.Role;

public interface RoleService {
	boolean save(Role role);
	Role get(Integer id);
	List<Role> getAll();
	void update(Role role);
	void delete(Integer id);
	boolean existsById(Integer id);
	boolean existsByRoleName(String roleName);
}