package com.bavostepbros.leap.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bavostepbros.leap.domain.service.roleservice.RoleService;
import com.bavostepbros.leap.domain.model.Role;
import com.bavostepbros.leap.domain.model.dto.RoleDto;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/api/role/")
public class RoleController {
	
	@Autowired
	private RoleService roleService;

	@PreAuthorize("hasAuthority('USER_ADMIN')")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public RoleDto addRole(
			@ModelAttribute("roleName") String roleName,
			@ModelAttribute("weight") Integer weight) {
		Role role = roleService.save(roleName, weight);
		return new RoleDto(role.getRoleId(), role.getRoleName(), role.getWeight());
	}
	
	@PreAuthorize("hasAuthority('USER_ADMIN')")
	@GetMapping(path = "{roleId}")
    public RoleDto getRoleById(@PathVariable("roleId") Integer roleId) {		
		Role role = roleService.get(roleId);
		return new RoleDto(role.getRoleId(), role.getRoleName(), role.getWeight());
    }
	
	@PreAuthorize("hasAuthority('USER_ADMIN')")
	@GetMapping
	public List<RoleDto> getAllRoles() {
		List<Role> roles = roleService.getAll();
		List<RoleDto> rolesDto = roles.stream()
				.map(role -> new RoleDto(role.getRoleId(), role.getRoleName(), role.getWeight()))
				.collect(Collectors.toList());
		return rolesDto;
	}
	
	@PreAuthorize("hasAuthority('USER_ADMIN')")
	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public RoleDto updateRole(
			@ModelAttribute("roleId") Integer roleId,
			@ModelAttribute("roleName") String roleName,
			@ModelAttribute("weight") Integer weight) {
		Role role = roleService.update(roleId, roleName, weight);
		return new RoleDto(role.getRoleId(), role.getRoleName(), role.getWeight());
	}
	
	@PreAuthorize("hasAuthority('USER_ADMIN')")
	@DeleteMapping(path = "{roleId}")
	public void deleteRole(@PathVariable("roleId") Integer roleId) {
		roleService.delete(roleId);
	}
}