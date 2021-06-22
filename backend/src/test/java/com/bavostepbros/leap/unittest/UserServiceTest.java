package com.bavostepbros.leap.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.bavostepbros.leap.domain.model.Role;
import com.bavostepbros.leap.domain.model.User;
import com.bavostepbros.leap.domain.service.userservice.UserService;
import com.bavostepbros.leap.domain.service.roleservice.RoleService;
import com.bavostepbros.leap.persistence.UserDAL;
import com.bavostepbros.leap.persistence.RoleDAL;

@AutoConfigureMockMvc
@SpringBootTest
public class UserServiceTest {
	@Autowired
	private UserService userService;

	@Autowired
    private RoleService roleService;

    @MockBean
    private RoleDAL roleDAL;
	
	@SpyBean
	private UserService spyUserService;

    @SpyBean
    private RoleService spyRoleService;
	
	private User user;
    private Role role;
	private List<User> userList;
	private Optional<User> optionalUser;
    private Optional<Role> optionalRole;

	private Set<Role> roles;
	private Integer userId;
	private String username;
	private String email;
	private String password;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

    @BeforeEach
	public void init() {
        role = new Role("Admin", 1);
		user = new User(1, "Wannes", " password", "wannes@gmail.com");
		userList = List.of(
                new User(1, "Wannes", "Test", "wannes@gmail.com"),
				new User(2, "Bavo", "Wachtwoord", "bavo@gmail.com"));
		optionalUser = Optional.of(user);
        optionalRole = Optional.of(role);

		roles = user.getRoles();
		userId = user.getUserId();
		username = user.getUsername();
		email = user.getEmail();
		password = user.getPassword();
	}

    @Test
	void should_notBeNull() {
		assertNotNull(roleService);
		assertNotNull(userService);
		assertNotNull(roleDAL);
		assertNotNull(userDAL);
		assertNotNull(user);
		assertNotNull(role);
		assertNotNull(userList);
		assertNotNull(optionalRole);
		assertNotNull(optionalUser);
	}

    @Test
	void should_saveUser_whenUserIsSaved() {
		Integer roleId = role.getRoleId();
		String username = user.getUsername();
		String password = user.getPassword();
        String email = user.getEmail();
		
		BDDMockito.doReturn(false).when(spyUserService).existsByUsername(username);
		BDDMockito.doReturn(true).when(spyRoleService).existsById(roleId);
		
		BDDMockito.given(roleDAL.findById(BDDMockito.anyInt())).willReturn(optionalRole);
		BDDMockito.given(userDAL.save(BDDMockito.any(User.class))).willReturn(user);
		
		User result = userService.save(username, password, email, roleId);
		
		assertNotNull(result);
		assertTrue(result instanceof User);
		assertEquals(user.getUserId(), result.getUserId());
		assertEquals(user.getUsername(), result.getUsername());
		assertEquals(user.getPassword(), result.getPassword());
		assertEquals(user.getEmail(), result.getEmail());
		assertEquals(user.getRoles(), result.getRoles());
	} 

    @Test
	void should_throwNullPointerException_whenGetUserByInvalidId() {
		String expectedErrorMessage = "User does not exist.";
		
		Exception exception = assertThrows(NullPointerException.class, 
				() -> userService.get(userId));	
		
		assertEquals(expectedErrorMessage, exception.getMessage());
	}

    @Test
	void should_retrieveValidUser_whenIdIsValidAndIdExists() {
		Integer userId = user.getUserId();
		
		BDDMockito.doReturn(true).when(spyUserService).existsById(userId);
		BDDMockito.given(userDAL.findById(userId)).willReturn(optionalUser);
		User fetchedUser = userService.get(userId);
		
		assertNotNull(fetchedUser);
		assertTrue(fetchedUser instanceof User);
		assertEquals(user.getUsername(), fetchedUser.getUsername());
		assertEquals(user.getUserId(), fetchedUser.getUserId());
		assertEquals(user.getRoles(), fetchedUser.getRoles());
		assertEquals(user.getPassword(), fetchedUser.getPassword());
		assertEquals(user.getEmail(), fetchedUser.getEmail());
	}

    @Test
	void should_retrieveUserList_whenGetAllIsCalled() {
		BDDMockito.given(userDAL.findAll()).willReturn(userList);
		List<User> fetchedUsers = userService.getAll();

		assertNotNull(fetchedUsers);
		assertEquals(userList.size(), fetchedUsers.size());
	}

    @Test
	void should_throwNullPointerException_whenUpdatedRoleIdIsInvalid() {
		String expected = "Role ID does not exist.";
		
		Exception exception = assertThrows(NullPointerException.class,
				() -> userService.update(userId, username, password, email, userId));

		assertEquals(expected, exception.getMessage());
	}

    @Test
	void should_throwNullPointerException_whenUpdateRoleDoesNotExists() {
		String expected = "Role ID is invalid.";
		
//		BDDMockito.doReturn(true).when(spyUserService).existsById(userId);
//		BDDMockito.doReturn(false).when(spyUserService).existsByUsername(username);
//		BDDMockito.doReturn(false).when(spyRoleService).existsById(roleId);
		
		Exception exception = assertThrows(NullPointerException.class,
				() -> userService.save(username, password, email, userId));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_retrieveValidUser_whenUserIsUpdated() {
		userDAL.save(user);
		
		BDDMockito.doReturn(true).when(spyUserService).existsById(userId);
		BDDMockito.doReturn(false).when(spyUserService).existsByUsername(username);
//		BDDMockito.doReturn(true).when(spyRoleService).existsById(roleId);
		
		BDDMockito.given(roleDAL.findById(BDDMockito.anyInt())).willReturn(optionalRole);
		BDDMockito.given(userDAL.save(user)).willReturn(user);
		
		User fetchedUser = userService.update(userId, username, password, email, userId);
		
		assertNotNull(fetchedUser);
		assertTrue(fetchedUser instanceof User);
		assertEquals(user.getUserId(), fetchedUser.getUserId());
		assertEquals(user.getRoles(),user.getRoles());
		assertEquals(user.getUsername(), user.getUsername());
		assertEquals(user.getEmail(), user.getEmail());
		assertEquals(user.getPassword(), user.getPassword());
	}
}
