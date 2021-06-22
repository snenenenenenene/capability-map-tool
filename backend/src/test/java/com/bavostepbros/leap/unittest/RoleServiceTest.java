package com.bavostepbros.leap.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
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
	
	private void testRole(Role expectedObject, Role actualObject) {
		assertEquals(expectedObject.getRoleId(), actualObject.getRoleId());
		assertEquals(expectedObject.getRoleName(), actualObject.getRoleName());
		assertEquals(expectedObject.getWeight(), actualObject.getWeight());
	}

    @Test
    void should_saveRole_whenRoleIsSaved() {
        String roleName = roleFirst.getRoleName();

        Role result = roleService.save(roleName, roleId);

        assertNotNull(result);
        assertTrue(result instanceof Role);
        assertEquals(roleFirst.getRoleId(), result.getRoleId());
        assertEquals(roleFirst.getRoleName(), result.getRoleName());
    }

    @Test
    void should_throwNullPointerException_whenFindByIdInputIsInvalid() {
        Integer invalidId = null;
        String roleName = "Test";
        String expected = "Role ID is not valid.";
        roleService.save(roleName, roleFirst.getWeight());

        Exception exception = assertThrows(NullPointerException.class, 
                () -> roleService.get(invalidId));

        assertEquals(exception.getMessage(), expected);
    }

    @Test
    void should_throwIndexDoesNotExistException_whenGetRoleIdDoesNotExist() {
        Integer id = roleFirst.getRoleId();
        String expected = "Role ID does not exist.";

        Exception exception = assertThrows(NullPointerException.class, 
                () -> roleService.get(id));
        
        assertEquals(exception.getMessage(), expected);
    }

    @Test
    void should_retrieveValidRole_whenIdIsValidAndIdExists() {
        Integer id = roleFirst.getRoleId();

        Role fetchedRole = roleService.get(id);

        assertNotNull(fetchedRole);
        assertTrue(fetchedRole instanceof Role);
        assertEquals(roleFirst.getRoleId(), fetchedRole.getRoleId());
        assertEquals(roleFirst.getRoleName(), fetchedRole.getRoleName());
    }

    @Test
    void should_retrieveRoleList_whenGetAllIsCalled() {
        BDDMockito.given(roleDAL.findAll()).willReturn(roles);
        List<Role> fetchedRoleList = roleService.getAll();

        assertNotNull(fetchedRoleList);
        assertEquals(roles.size(), fetchedRoleList.size());
    }

    @Test
    void should_throwNullPointerException_whenUpdateRoleIdDoesNotExist() {
        String roleName = roleFirst.getRoleName();
        Integer id = roleFirst.getRoleId();
		Integer weight = roleFirst.getWeight();
        String expected = "Can not update role if it does not exist.";

        Exception exception = assertThrows(NullPointerException.class, 
                () -> roleService.update(id, roleName, weight));
        
        assertEquals(exception.getMessage(), expected);  
    }

    @Test
    void should_retrieveValidRole_WhenRoleIsUpdated() {
        String roleName = "Admin";
        Integer id = roleFirst.getRoleId();
		Integer weight = roleFirst.getWeight();
        roleService.save(roleName, weight);

        Role fetchedRole = roleService.update(id, roleName, weight);

        assertNotNull(fetchedRole);
        assertTrue(fetchedRole instanceof Role);
        assertEquals(roleFirst.getRoleId(), fetchedRole.getRoleId());
        assertEquals(roleFirst.getRoleName(), fetchedRole.getRoleName());
    }

    @Test
    void should_throwNullPointerExceptioin_WhenDeleteRoleIdDoesNotExist() {
        Integer id = roleFirst.getRoleId();
        String expected = "Role does not exist.";

        Exception exception = assertThrows(NullPointerException.class, 
                () -> roleService.delete(id));

        assertEquals(exception.getMessage(), expected);
    }

    @Test
    void should_ReturnTrue_whenRoleDoesExistById() {
        Integer id = roleFirst.getRoleId();
        BDDMockito.given(roleDAL.existsById(BDDMockito.anyInt())).willReturn(true);

        boolean result = roleService.existsById(id);

        assertTrue(result);
    }

}
