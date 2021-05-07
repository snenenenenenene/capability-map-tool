package com.bavostepbros.leap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.service.roleservice.RoleService;
import com.bavostepbros.leap.domain.model.Role;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class RoleController {
	
	@Autowired
	private RoleService roleService;

	@PostMapping(path = "/role/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Void> addRole(
			@ModelAttribute Role role, 
			UriComponentsBuilder builder) {
		if (role.getRoleName() == null) {
			throw new InvalidInputException("Invalid input.");
		}
		if (!roleService.existsByRoleName(role.getRoleName())) {
			throw new DuplicateValueException("Role name already exists.");
		}
		
		boolean flag = roleService.save(role);
		if (flag == false) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder
				.path("/role/{id}")
				.buildAndExpand(role.getRoleId()).toUri());
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}
	
	@GetMapping("/role/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable("id") Integer id) {
		if (id == null || id.equals(0)){
			throw new InvalidInputException("Role ID is not valid");
		}
		if (!roleService.existsById(id)){
			throw new IndexDoesNotExistException("Role ID does not exist.");
		}

		Role role = roleService.get(id);
		return new ResponseEntity<Role>(role, HttpStatus.OK);
    }
	
	@GetMapping("/role/all")
	public ResponseEntity<List<Role>> getAllRoles() {
		List<Role> roles = roleService.getAll();
		return new ResponseEntity<List<Role>>(roles, HttpStatus.OK);
	}
	
	@PutMapping(path = "/role/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Role> updateRole(@ModelAttribute Role role) {
		if (role.getRoleId() == null || role.getRoleId().equals(0) ||
				role.getRoleName() == null || role.getRoleName().equals(0)){
			throw new InvalidInputException("Invalid input.");
		}
		if (!roleService.existsById(role.getRoleId())){
			throw new IndexDoesNotExistException("Can not update role if does not exist.");
		}
		if (!roleService.existsByRoleName(role.getRoleName())){
			throw new DuplicateValueException("Rolename already exists.");
		}
		
		
		roleService.update(role);
		return new ResponseEntity<Role>(role, HttpStatus.OK);
	}
	
	@DeleteMapping("/role/{id}")
	public ResponseEntity<Void> deleteRole(@PathVariable("id") Integer id) {
		if (id == null || id.equals(0)){
			throw new InvalidInputException("Role ID is not valid.");
		}
		if (!roleService.existsById(id)){
			throw new IndexDoesNotExistException("Role ID does not exist.");
		}

		roleService.delete(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}