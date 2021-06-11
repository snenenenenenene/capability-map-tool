package com.bavostepbros.leap.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.EnvironmentException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.service.environmentservice.EnvironmentService;
import com.bavostepbros.leap.persistence.EnvironmentDAL;

/**
 *
 * @author Bavo Van Meel
 *
 */
@AutoConfigureMockMvc
@SpringBootTest
public class EnvironmentServiceTest {
	
	@Autowired
    private MockMvc mockMvc;

	@Autowired
	private EnvironmentService environmentService;

	@MockBean
	private EnvironmentDAL environmentDAL;

	@SpyBean
	private EnvironmentService spyEnvironmentService;

	private Environment environment;
	private List<Environment> environments;
	private Optional<Environment> optionalEnvironment;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@BeforeEach
	public void init() {
		environment = new Environment(1, "Environment test");
		environments = List.of(new Environment(1, "Test 1"), new Environment(2, "Test 2"));
		optionalEnvironment = Optional.of(environment);
	}

	@Test
	void should_notBeNull() {
		assertNotNull(environmentService);
		assertNotNull(environmentDAL);
		assertNotNull(environment);
		assertNotNull(environments);
		assertNotNull(optionalEnvironment);
	}

	@Test
	void should_throwInvalidInputException_whenSavedInputIsInvalid() {
		String falseEnvironmentName = "";
		String expected = "Invalid input.";

		assertThrows(ConstraintViolationException.class,
				() -> environmentService.save(falseEnvironmentName));

		// assertEquals(exception.getMessage(), expected);
	}

//	@Test
//	void should_throwConstraintViolationException_whenSavedEnvironmentNameExists() {
//		String environmentName = environment.getEnvironmentName();
//		String expected = "No value present";
//
//		BDDMockito.given(environmentDAL.save(BDDMockito.any(Environment.class))).willThrow(ConstraintViolationException.class);
//
//		Exception exception = assertThrows(ConstraintViolationException.class,
//				() -> environmentService.save(environmentName));
//
//		assertEquals(expected, exception.getMessage());
//	}

	@Test //3
	void should_saveEnvironment_whenEnvironmentIsSaved() {
		String environmentName = environment.getEnvironmentName();

		BDDMockito.given(environmentDAL.save(BDDMockito.any(Environment.class))).willReturn(environment);
		Environment result = environmentService.save(environmentName);

		assertNotNull(result);
		assertTrue(result instanceof Environment);
		assertEquals(environment.getEnvironmentId(), result.getEnvironmentId());
		assertEquals(environment.getEnvironmentName(), result.getEnvironmentName());
	}

	@Test
	void should_throwNullPointerException_whenGetEnvironmentByIdInvalidId() {
		Integer invalidId = 0;
		String expected = "Environment does not exist.";

		Exception exception = assertThrows(NullPointerException.class,
				() -> environmentService.get(invalidId));

		assertEquals(exception.getMessage(), expected);
	}

	@Test
	void should_retrieveValidEnvironment_whenGetEnvironmentById() {
		Integer id = environment.getEnvironmentId();

		BDDMockito.given(environmentDAL.findById(id)).willReturn(Optional.of(environment));
		Environment fetchedEnvironment = environmentService.get(id);

		assertNotNull(fetchedEnvironment);
		assertTrue(fetchedEnvironment instanceof Environment);
		assertEquals(environment.getEnvironmentId(), fetchedEnvironment.getEnvironmentId());
		assertEquals(environment.getEnvironmentName(), fetchedEnvironment.getEnvironmentName());
	}

	@Test
	void should_throwConstraintViolationException_whenGetEnvironmentByNameIsEmpty() {
		String falseEnvironmentName = "";

		Exception exception = assertThrows(ConstraintViolationException.class,
				() -> environmentService.getByEnvironmentName(falseEnvironmentName));
	}

	@Test
	void should_throwNullPointerException_whenGetEnvironmentByNameInvalidName() {
		String falseEnvironmentName = "very special and weird name";
		String expected = "Environment does not exist.";

		Exception exception = assertThrows(NullPointerException.class,
				() -> environmentService.getByEnvironmentName(falseEnvironmentName));

		assertEquals(exception.getMessage(), expected);
	}

	@Test
	void should_retrieveValidEnvironment_whenGetEnvironmentByNameIsValid() {
		String falseEnvironmentName = environment.getEnvironmentName();

		BDDMockito.given(environmentDAL.findByEnvironmentName(BDDMockito.any(String.class)))
				.willReturn(Optional.of(environment));
		Environment fetchedEnvironment = environmentService.getByEnvironmentName(falseEnvironmentName);

		assertNotNull(fetchedEnvironment);
		assertTrue(fetchedEnvironment instanceof Environment);
		assertEquals(fetchedEnvironment.getEnvironmentName(), environment.getEnvironmentName());
		assertEquals(environment.getEnvironmentId(), fetchedEnvironment.getEnvironmentId());
		assertEquals(environment.getEnvironmentName(), fetchedEnvironment.getEnvironmentName());
	}

	@Test //10
	void should_retrieveEnvironmentList_whenGetAllIsCalled() {
		BDDMockito.given(environmentDAL.findAll()).willReturn(environments);
		List<Environment> fetchedEnvironments = environmentService.getAll();

		assertNotNull(fetchedEnvironments);
		assertEquals(environments.size(), fetchedEnvironments.size());
	}

	@Test
	void should_throwInvalidInputException_whenUpdateNameIsInvalid() {
		String falseEnvironmentName = "";
		Integer id = environment.getEnvironmentId();

		Exception exception = assertThrows(ConstraintViolationException.class,
				() -> environmentService.update(id, falseEnvironmentName));
	}

	@Test //14
	void should_retrieveValidEnvironment_whenEnvironmentIsUpdated() {
		String falseEnvironmentName = environment.getEnvironmentName();
		Integer id = environment.getEnvironmentId();

		BDDMockito.given(environmentDAL.save(environment)).willReturn(environment);
		BDDMockito.given(environmentDAL.findById(BDDMockito.anyInt())).willReturn(optionalEnvironment);
		Environment fetchedEnvironment = environmentService.update(id, falseEnvironmentName);

		assertNotNull(fetchedEnvironment);
		assertTrue(fetchedEnvironment instanceof Environment);
		assertEquals(environment.getEnvironmentId(), fetchedEnvironment.getEnvironmentId());
		assertEquals(environment.getEnvironmentName(), fetchedEnvironment.getEnvironmentName());
	}

	@Test
	void should_verifyDeleted_whenDeleted() {
		Integer id = environment.getEnvironmentId();
		environmentService.delete(id);

		Mockito.verify(environmentDAL, Mockito.times(1)).deleteById(Mockito.eq(id));
	}

	@Test //17
	void should_ReturnFalse_whenEnvironmentDoesNotExistById() {
		BDDMockito.given(environmentDAL.existsById(BDDMockito.anyInt())).willReturn(false);

		boolean result = environmentService.existsById(environment.getEnvironmentId());

		assertFalse(result);
	}

	@Test //18
	void should_ReturnTrue_whenEnvironmentDoesExistById() {
		BDDMockito.given(environmentDAL.existsById(BDDMockito.anyInt())).willReturn(true);

		boolean result = environmentService.existsById(environment.getEnvironmentId());

		assertTrue(result);
	}

	@Test //19
	void should_ReturnTrue_whenEnvironmentDoesExistByEnvironmentName() {
		BDDMockito.given(environmentDAL.findByEnvironmentName(BDDMockito.any(String.class)))
			.willReturn(optionalEnvironment);

		boolean result = environmentService.existsByEnvironmentName(environment.getEnvironmentName());

		assertTrue(result);
	}
	
	@Test //20
	void should_ReturnFalse_whenEnvironmentDoesNotExistByEnvironmentName() {
		BDDMockito.given(environmentDAL.findByEnvironmentName(BDDMockito.any(String.class)))
			.willReturn(Optional.empty());

		boolean result = environmentService.existsByEnvironmentName(environment.getEnvironmentName());

		assertFalse(result);
	}

}
