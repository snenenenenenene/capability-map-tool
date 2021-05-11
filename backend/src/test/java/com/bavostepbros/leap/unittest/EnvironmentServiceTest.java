package com.bavostepbros.leap.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

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
@SpringBootTest
public class EnvironmentServiceTest {

	@Autowired
	private EnvironmentService environmentService;

	@MockBean
	private EnvironmentDAL environmentDAL;

	@SpyBean
	private EnvironmentService spyService;

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

		Exception exception = assertThrows(InvalidInputException.class,
				() -> environmentService.save(falseEnvironmentName));

		assertEquals(exception.getMessage(), expected);
	}

	@Test
	void should_throwDuplicateValueException_whenSavedEnvironmentNameExists() {
		String environmentName = environment.getEnvironmentName();
		String expected = "Environment name already exists.";
		BDDMockito.doReturn(false).when(spyService).existsByEnvironmentName(environmentName);

		Exception exception = assertThrows(DuplicateValueException.class,
				() -> environmentService.save(environmentName));

		assertEquals(exception.getMessage(), expected);
	}

	@Test
	void should_saveEnvironment_whenEnvironmentIsSaved() {
		String environmentName = environment.getEnvironmentName();

		Mockito.doReturn(true).when(spyService).existsByEnvironmentName(environmentName);
		BDDMockito.given(environmentDAL.save(BDDMockito.any(Environment.class))).willReturn(environment);
		Environment result = environmentService.save(environmentName);

		assertNotNull(result);
		assertTrue(result instanceof Environment);
		assertEquals(environment.getEnvironmentId(), result.getEnvironmentId());
		assertEquals(environment.getEnvironmentName(), result.getEnvironmentName());
	}

	@Test
	void should_throwInvalidInputException_whenGetEnvironmentIdNotValidWithId() {
		Integer invalidId = 0;
		String expected = "Environment ID is not valid.";

		Exception exception = assertThrows(InvalidInputException.class, 
				() -> environmentService.get(invalidId));

		assertEquals(exception.getMessage(), expected);
	}

	@Test
	void should_throwIndexDoesNotExistException_whenGetEnvironmentIdDoesNotExist() {
		Integer id = environment.getEnvironmentId();
		String expected = "Environment ID does not exists.";

		Exception exception = assertThrows(IndexDoesNotExistException.class, 
				() -> environmentService.get(id));

		assertEquals(exception.getMessage(), expected);
	}

	@Test
	void should_retrieveValidEnvironment_whenIdIsValidAndIdExists() {
		Integer id = environment.getEnvironmentId();

		Mockito.doReturn(true).when(spyService).existsById(id);
		BDDMockito.given(environmentDAL.findById(id)).willReturn(Optional.of(environment));
		Environment fetchedEnvironment = environmentService.get(id);

		assertNotNull(fetchedEnvironment);
		assertTrue(fetchedEnvironment instanceof Environment);
		assertEquals(environment.getEnvironmentId(), fetchedEnvironment.getEnvironmentId());
		assertEquals(environment.getEnvironmentName(), fetchedEnvironment.getEnvironmentName());
	}

	@Test
	void should_throwInvalidInputException_whenGetByEnvironmentNameHasInvalidInput() {
		String falseEnvironmentName = "";
		String expected = "Environment name is not valid.";

		Exception exception = assertThrows(InvalidInputException.class,
				() -> environmentService.getByEnvironmentName(falseEnvironmentName));

		assertEquals(exception.getMessage(), expected);
	}

	@Test
	void should_throwEnvironmentException_whenEnvironmentNameExists() {
		String environmentName = environment.getEnvironmentName();
		String expected = "Environment name does not exists.";
		BDDMockito.doReturn(true).when(spyService).existsByEnvironmentName(environmentName);

		Exception exception = assertThrows(EnvironmentException.class,
				() -> environmentService.getByEnvironmentName(environmentName));

		assertEquals(exception.getMessage(), expected);
	}

	@Test
	void should_RetrieveValidEnvironment_whenEnvironmentNameIsValid() {
		String falseEnvironmentName = environment.getEnvironmentName();
		environmentService.save(falseEnvironmentName);

		Mockito.doReturn(false).when(spyService).existsByEnvironmentName(falseEnvironmentName);
		BDDMockito.given(environmentDAL.findByEnvironmentName(BDDMockito.any(String.class)))
				.willReturn(Optional.of(environment));
		Environment fetchedEnvironment = environmentService.getByEnvironmentName(falseEnvironmentName);

		assertNotNull(fetchedEnvironment);
		assertTrue(fetchedEnvironment instanceof Environment);
		assertEquals(fetchedEnvironment.getEnvironmentName(), environment.getEnvironmentName());
		assertEquals(environment.getEnvironmentId(), fetchedEnvironment.getEnvironmentId());
		assertEquals(environment.getEnvironmentName(), fetchedEnvironment.getEnvironmentName());
	}

	@Test
	void should_retrieveEnvironmentList_whenGetAllIsCalled() {
		BDDMockito.given(environmentDAL.findAll()).willReturn(environments);
		List<Environment> fetchedEnvironments = environmentService.getAll();

		assertNotNull(fetchedEnvironments);
		assertEquals(environments.size(), fetchedEnvironments.size());
	}

	@Test
	void should_throwInvalidInputException_whenUpdatedInputIsInvalid() {
		String falseEnvironmentName = "";
		Integer id = environment.getEnvironmentId();
		String expected = "Invalid input.";

		Exception exception = assertThrows(InvalidInputException.class,
				() -> environmentService.update(id, falseEnvironmentName));

		assertEquals(exception.getMessage(), expected);
	}

	@Test
	void should_throwIndexDoesNotExistException_whenUpdateEnvironmentIdDoesNotExist() {
		String falseEnvironmentName = "Environment test 2";
		Integer id = environment.getEnvironmentId();
		String expected = "Can not update environment if it does not exist.";
		BDDMockito.doReturn(false).when(spyService).existsById(id);

		Exception exception = assertThrows(IndexDoesNotExistException.class,
				() -> environmentService.update(id, falseEnvironmentName));

		assertEquals(exception.getMessage(), expected);
	}

	@Test
	void should_throwEnvironmentException_whenUpdateEnvironmentNameExists() {
		String environmentName = environment.getEnvironmentName();
		Integer id = environment.getEnvironmentId();
		String expected = "Environment name already exists.";
		BDDMockito.doReturn(true).when(spyService).existsById(id);
		BDDMockito.doReturn(false).when(spyService).existsByEnvironmentName(environmentName);

		Exception exception = assertThrows(EnvironmentException.class,
				() -> environmentService.update(id, environmentName));

		assertEquals(exception.getMessage(), expected);
	}

	@Test
	void should_retrieveValidEnvironment_whenEnvironmentIsUpdated() {
		String falseEnvironmentName = environment.getEnvironmentName();
		Integer id = environment.getEnvironmentId();
		environmentService.save(falseEnvironmentName);

		Mockito.doReturn(true).when(spyService).existsById(id);
		Mockito.doReturn(true).when(spyService).existsByEnvironmentName(falseEnvironmentName);

		BDDMockito.given(environmentDAL.save(environment)).willReturn(environment);
		Environment fetchedEnvironment = environmentService.update(id, falseEnvironmentName);

		assertNotNull(fetchedEnvironment);
		assertTrue(fetchedEnvironment instanceof Environment);
		assertEquals(environment.getEnvironmentId(), fetchedEnvironment.getEnvironmentId());
		assertEquals(environment.getEnvironmentName(), fetchedEnvironment.getEnvironmentName());
	}

	@Test
	void should_throwInvalidInputException_whenDeleteInputIsInvalid() {
		Integer id = 0;
		String expected = "Environment ID is not valid.";

		Exception exception = assertThrows(InvalidInputException.class, 
				() -> environmentService.delete(id));

		assertEquals(exception.getMessage(), expected);
	}

	@Test
	void should_throwIndexDoesNotExistException_whenDeleteInputDoesNotExists() {
		Integer id = environment.getEnvironmentId();
		String expected = "Environment ID does not exists.";

		environmentDAL.deleteById(id);

		Exception exception = assertThrows(IndexDoesNotExistException.class, 
				() -> environmentService.get(id));

		assertEquals(exception.getMessage(), expected);
	}

	@Test
	void should_ReturnFalse_whenEnvironmentDoesNotExistById() {
		BDDMockito.given(environmentDAL.existsById(BDDMockito.anyInt())).willReturn(false);

		boolean result = environmentService.existsById(environment.getEnvironmentId());

		assertFalse(result);
	}

	@Test
	void should_ReturnTrue_whenEnvironmentDoesExistById() {
		BDDMockito.given(environmentDAL.existsById(BDDMockito.anyInt())).willReturn(true);

		boolean result = environmentService.existsById(environment.getEnvironmentId());

		assertTrue(result);
	}
	
	@Test
	void should_ReturnFalse_whenEnvironmentDoesExistByEnvironmentName() {
		BDDMockito.given(environmentDAL.findByEnvironmentName(BDDMockito.any(String.class)))
			.willReturn(optionalEnvironment);

		boolean result = environmentService.existsByEnvironmentName(environment.getEnvironmentName());

		assertFalse(result);
	}
	
	@Test
	void should_ReturnTrue_whenEnvironmentDoesNotExistByEnvironmentName() {
		BDDMockito.given(environmentDAL.findByEnvironmentName(BDDMockito.any(String.class)))
			.willReturn(Optional.empty());

		boolean result = environmentService.existsByEnvironmentName(environment.getEnvironmentName());

		assertTrue(result);
	}

}
