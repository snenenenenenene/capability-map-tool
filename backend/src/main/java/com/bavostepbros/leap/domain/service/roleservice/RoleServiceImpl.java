package com.bavostepbros.leap.domain.service.roleservice;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
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
	public void update(Role role) {
		roleDAL.save(role);
	}

	@Override
	public void delete(Integer id) {
		roleDAL.deleteById(id);
	}
	
	@Override
	public boolean existsById(Integer id){
		boolean result = roleDAL.existsById(id);
		return result;
	}

	@Override
	public boolean existsByRoleName(String roleName){
		boolean result = roleDAL.findByRoleName(roleName).isEmpty();
		return result;
	}
}