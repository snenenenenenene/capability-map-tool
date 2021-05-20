package com.bavostepbros.leap.domain.service.roleservice;

import java.util.List;

import com.bavostepbros.leap.domain.model.Role;

public interface RoleService {
	Role save(String roleName);
	Role get(Integer id);
	List<Role> getAll();
	Role update(Integer id, String roleName);
	void delete(Integer id);
	boolean existsById(Integer id);
	boolean existsByRoleName(String roleName);
}