package com.bavostepbros.leap.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.bavostepbros.leap.domain.customexceptions.ValidationErrorResponse;
import com.bavostepbros.leap.domain.model.Role;
import com.bavostepbros.leap.domain.model.dto.RoleDto;
import com.bavostepbros.leap.domain.service.roleservice.RoleService;
import com.bavostepbros.leap.persistence.RoleDAL;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoleControllerTest extends ApiIntegrationTest {
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private RoleDAL roleDAL;
	
	@Autowired
	private RoleService roleService;
	
	static final String PATH = "/api/role/";
	
	private Role roleFirst;
	private Role roleSecond;
	
	private Integer roleId;
	private String roleName;
	private Integer roleWeight;
	
	@BeforeAll
	public void authenticate() throws Exception { 
		super.authenticate(); 
	}
	
	@BeforeEach
	public void init() {
		roleFirst = roleDAL.save(new Role(1, "bad admin", 1));
		roleSecond = roleDAL.save(new Role(2, "bad user", 2));
		
		roleId = roleFirst.getRoleId();
		roleName = roleFirst.getRoleName();
		roleWeight = roleFirst.getWeight();
	}
	
	@AfterEach
	public void close() {
		roleDAL.delete(roleFirst);
		roleDAL.delete(roleSecond);
	}
	
	@Test
	void should_notBeNull() {
		assertNotNull(roleDAL);
		
		assertNotNull(roleFirst);
		assertNotNull(roleSecond);
		
		assertNotNull(roleId);
		assertNotNull(roleName);
		assertNotNull(roleWeight);
	}
	
	@Test
	@WithMockUser(username = "super_admin", password = "super_admin", authorities = "USER_ADMIN")
	public void should_throwInvalidInput_whenSaveRoleInvalidName() throws Exception {
		String roleName = "";
		String exceptionMessage = "Role name is required.";
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("roleName", roleName)
				.param("weight", roleWeight.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException))
			    .andReturn();
		
		ValidationErrorResponse violations = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), 
				ValidationErrorResponse.class);
		
		assertEquals(exceptionMessage, violations.getViolations().get(0).getMessage());
	}
	
//	@Test
//	@WithMockUser(username = "super_admin", password = "super_admin", authorities = "USER_ADMIN")
//	public void should_postRole_whenSaveRole() throws Exception {
//		String roleName = "A test";
//		
//		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
//				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
//				.param("roleName", roleName)
//				.param("weight", roleWeight.toString())
//				.accept(MediaType.APPLICATION_JSON))
//				.andExpect(MockMvcResultMatchers.status().isOk())
//			    .andReturn();
//		
//		RoleDto roleDto = objectMapper.readValue(
//				mvcResult.getResponse().getContentAsString(), RoleDto.class);
//		
//		Role role = roleService.getRoleByRoleName(roleName);
//		
//		assertNotNull(roleDto);
//		testRole(role, roleDto);
//	}
	
	private void testRole(Role expectedObject, RoleDto actualObject) {
		assertEquals(expectedObject.getRoleId(), actualObject.getRoleId());
		assertEquals(expectedObject.getRoleName(), actualObject.getRoleName());
		assertEquals(expectedObject.getWeight(), actualObject.getWeight());
	}
}
