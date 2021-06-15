package com.bavostepbros.leap.domain.service.roleservice;

import java.util.ArrayList;
import java.util.List;

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
		save("user");
		save("admin");
	}

	private final RoleDAL roleDAL;

	
	/** 
	 * @param roleName
	 * @return Role
	 */
	@Override
	public Role save(String roleName) {
		if (roleName == null || roleName.isBlank() || roleName.isEmpty()) {
			throw new InvalidInputException("Invalid input.");
		}

		if (!existsByRoleName(roleName)) {
			throw new DuplicateValueException("Role name already exists.");
		}
		return roleDAL.save(new Role(roleName));
	}


	
	/** 
	 * @param id
	 * @return Role
	 */
	//TODO perform checks
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
	
	
	/** 
	 * @return List<Role>
	 */
	@Override
	public List<Role> getAll() {
		List<Role> roles = new ArrayList<Role>();
		roles = roleDAL.findAll();
		return roles;
	}
	
	
	/** 
	 * @param roleId
	 * @param roleName
	 * @return Role
	 */
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

	
	/** 
	 * @param id
	 */
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
	
	
	/** 
	 * @param id
	 * @return boolean
	 */
	@Override
	public boolean existsById(Integer id){
		boolean result = roleDAL.findById(id) == null;
		return !result;
	}

	
	/** 
	 * @param roleName
	 * @return boolean
	 */
	@Override
	public boolean existsByRoleName(String roleName){
		boolean result = roleDAL.findByRoleName(roleName).isEmpty();
		return result;
	}
}