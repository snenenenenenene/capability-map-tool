package com.bavostepbros.leap.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.bavostepbros.leap.domain.model.Role;
import com.bavostepbros.leap.domain.service.roleservice.RoleService;
import com.bavostepbros.leap.persistence.RoleDAL;

@AutoConfigureMockMvc
@SpringBootTest
public class RoleServiceTest {
	
	@Autowired
	private RoleService roleService;
	
	@MockBean
	private RoleDAL roleDAL;
	
	private Role roleFirst;
	private Role roleSecond;
	private List<Role> roles;
	private Optional<Role> optionalRoleFirst;
	
	private Integer roleId;
	private String roleName;
	private Integer roleWeight;
	
	@BeforeEach
	void init() {
		roleFirst = new Role(1, "Super admin", 1);
		roleSecond = new Role(2, "Super user", 2);
		roles = List.of(roleFirst, roleSecond);
		optionalRoleFirst = Optional.of(roleFirst);
		
		roleId = roleFirst.getRoleId();
		roleName = roleFirst.getRoleName();
		roleWeight = roleFirst.getWeight();
	}
	
	@Test
	void shouldNotBeNull() {
		assertNotNull(roleService);
		assertNotNull(roleDAL);
		
		assertNotNull(roleFirst);
		assertNotNull(roleSecond);
		assertNotNull(roles);
		assertNotNull(optionalRoleFirst);
		
		assertNotNull(roleId);
		assertNotNull(roleName);
		assertNotNull(roleWeight);
	}
	
//	@Test 
//	void should_returnBusinessProcess_whenSaveBusinessProcess() {
//		BDDMockito.given(roleDAL.save(BDDMockito.any(Role.class))).willReturn(roleFirst);
//		
//		Role role = roleService.save(roleName, roleWeight);
//		
//		assertNotNull(role);
//		assertTrue(role instanceof Role);
//		testRole(roleFirst, role);
//	}
	
	private void testRole(Role expectedObject, Role actualObject) {
		assertEquals(expectedObject.getRoleId(), actualObject.getRoleId());
		assertEquals(expectedObject.getRoleName(), actualObject.getRoleName());
		assertEquals(expectedObject.getWeight(), actualObject.getWeight());
	}
}
