package com.bavostepbros.leap.domain.service.roleservice;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.customexceptions.RoleException;
import com.bavostepbros.leap.domain.model.Role;
import com.bavostepbros.leap.persistence.RoleDAL;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

	private final RoleDAL roleDAL;

	@Override
	public Role save(String roleName) {
		if (roleName == null || roleName.isBlank() || roleName.isEmpty()) {
			throw new InvalidInputException("Invalid input.");
		}

		if (!existsByRoleName(roleName)) {
			throw new DuplicateValueException("Role name already exists.");
		}

		Role role = new Role(roleName);
		Role savedRole = roleDAL.save(role);
		return savedRole;
	}

	@Override
	public Role get(Integer id) {
		if (id == null || id.equals(0)) {
			throw new InvalidInputException("Role ID is not valid.");
		}
		if (!existsById(id)) {
			throw new IndexDoesNotExistException("Role ID does not exist.");
		}

		Role role = roleDAL.findById(id).get();
		return role;
	}
	
	@Override
	public List<Role> getAll() {
		List<Role> roles = new ArrayList<Role>();
		roles = roleDAL.findAll();
		return roles;
	}
	
	@Override
	public Role update(Integer roleId, String roleName) {
		if (roleId == null || roleId.equals(0) || 
				roleName == null ||  roleName.isBlank() || roleName.isEmpty()) {
			throw new InvalidInputException("Invalid input.");
		}
		if (!existsById(roleId)) {
			throw new IndexDoesNotExistException("Can not update role if it does not exist.");
		}
		if (existsByRoleName(roleName)) {
			throw new RoleException("Rolename already exists.");
		}
		
		Role role = new Role(roleId, roleName);
		Role updated = roleDAL.save(role);
		return updated;
	}

	@Override
	public void delete(Integer id) {
		if (id == null || id.equals(0)) {
			throw new InvalidInputException("role ID is not valid.");
		}
		if (!existsById(id)) {
			throw new IndexDoesNotExistException("Can not delete role if it does not exist.");
		}
		roleDAL.deleteById(id);
	}
	
	@Override
	public boolean existsById(Integer id){
		boolean result = roleDAL.findById(id) == null;
		return !result;
	}

	@Override
	public boolean existsByRoleName(String roleName){
		boolean result = roleDAL.findByRoleName(roleName).isEmpty();
		return result;
	}
}