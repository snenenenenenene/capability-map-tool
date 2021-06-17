package com.bavostepbros.leap.domain.service.roleservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
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

	//TODO put in application.properties
	@PostConstruct
	private void init() {
		save("USER_ADMIN", 1);
		save("APP_ADMIN", 2);
		save("CREATING_USER", 3);
		save("VIEWING_USER", 4);
	}

	private final RoleDAL roleDAL;

	@Override
	public Role save(String roleName, Integer weight) {
		return roleDAL.save(new Role(roleName, weight));
	}


	//TODO perform checks
	@Override
	public Role get(Integer id) {
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
	public Role update(Integer roleId, String roleName, Integer weight) {
		Role role = new Role(roleId, roleName, weight);
		Role updated = roleDAL.save(role);
		return updated;
	}

	@Override
	public void delete(Integer id) {
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

	@Override
	public Role getRoleByRoleName(String roleName) {
		Optional<Role> optionalRole = roleDAL.findByRoleName(roleName);
		optionalRole.orElseThrow(() -> new NullPointerException("Role does not exists."));
		return optionalRole.get();
	}
}