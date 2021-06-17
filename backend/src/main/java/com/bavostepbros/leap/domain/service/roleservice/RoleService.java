package com.bavostepbros.leap.domain.service.roleservice;

import java.util.List;
import java.util.Optional;

import com.bavostepbros.leap.domain.model.Role;

public interface RoleService {
	Role save(String roleName, Integer weight);

	Role get(Integer id);

	List<Role> getAll();

	Role update(Integer id, String roleName, Integer weight);

	void delete(Integer id);

	boolean existsById(Integer id);

	boolean existsByRoleName(String roleName);
	
	Role getRoleByRoleName(String roleName);
}