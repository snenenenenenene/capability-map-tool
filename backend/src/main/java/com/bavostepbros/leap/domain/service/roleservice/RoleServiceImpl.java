package com.bavostepbros.leap.domain.service.roleservice;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.bavostepbros.leap.domain.model.Role;
import com.bavostepbros.leap.persistence.RoleDAL;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

	private final RoleDAL roleDAL;

	@Override
	public boolean save(Role role) {
		try {
            roleDAL.save(role);
            return true;
        } catch (Exception e) {
            return false;
        }
	}

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