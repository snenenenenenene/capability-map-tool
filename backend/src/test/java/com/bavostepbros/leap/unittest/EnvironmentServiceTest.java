package com.bavostepbros.leap.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
import org.springframework.test.web.servlet.MockMvc;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.Strategy;
import com.bavostepbros.leap.domain.model.paceofchange.PaceOfChange;
import com.bavostepbros.leap.domain.model.targetoperatingmodel.TargetOperatingModel;
import com.bavostepbros.leap.domain.service.environmentservice.EnvironmentService;
import com.bavostepbros.leap.persistence.EnvironmentDAL;

@AutoConfigureMockMvc
@SpringBootTest
public class EnvironmentServiceTest {
	
	@SuppressWarnings("unused")
	@Autowired
    private MockMvc mockMvc;

	@Autowired
	private EnvironmentService environmentService;

	@MockBean
	private EnvironmentDAL environmentDAL;

	@SpyBean
	private EnvironmentService spyEnvironmentService;

	private Environment environment;
	private Status status;
	private Capability capabilityFirst;
	private Capability capabilitySecond;
	private Strategy strategy;
	private List<Environment> environments;
	private List<Capability> capabilities;
	private Optional<Environment> optionalEnvironment;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@BeforeEach
	public void init() {
		environment = new Environment(1, "Environment test");
		status = new Status(1, LocalDate.of(2021, 5, 9));
		capabilityFirst = new Capability(1, environment, status, 0, "Capability 1", "Description 1",
				PaceOfChange.DIFFERENTIATION, TargetOperatingModel.COORDINATION, 10, 2.0, 3.0);
		capabilitySecond = new Capability(1, environment, status, capabilityFirst.getCapabilityId(), "Capability 1",
				"Description 1", PaceOfChange.DIFFERENTIATION, TargetOperatingModel.COORDINATION, 10, 2.0, 3.0);
		strategy = new Strategy(1, status, "Strategy name", LocalDate.of(2021, 8, 10), LocalDate.of(2021, 8, 10),
				environment);
		environments = List.of(new Environment(1, "Test 1"), new Environment(2, "Test 2"));
		capabilities = List.of(capabilityFirst, capabilitySecond);
		optionalEnvironment = Optional.of(environment);
	}

	@Test
	void should_notBeNull() {
		assertNotNull(environmentService);
		assertNotNull(environmentDAL);
		assertNotNull(environment);
		assertNotNull(status);
		assertNotNull(capabilityFirst);
		assertNotNull(capabilitySecond);
		assertNotNull(strategy);
		assertNotNull(environments);
		assertNotNull(capabilities);
		assertNotNull(optionalEnvironment);
	}

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

		assertThrows(NullPointerException.class,
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
	void should_addCapabilityToEnvironment_whenAddCapability() {
		Integer id = environment.getEnvironmentId();
		
		BDDMockito.doReturn(environment).when(spyEnvironmentService).get(id);
		BDDMockito.given(environmentDAL.save(environment)).willReturn(environment);
		
		Environment fetchedEnvironment = environmentService.addCapability(id, capabilityFirst);
		
		assertNotNull(fetchedEnvironment);
		assertTrue(fetchedEnvironment instanceof Environment);
		assertEquals(environment.getEnvironmentId(), fetchedEnvironment.getEnvironmentId());
		assertEquals(environment.getEnvironmentName(), fetchedEnvironment.getEnvironmentName());
	}
	
	@Test
	void should_addCapabilitiesToEnvironment_whenAddCapabilities() {
		Integer id = environment.getEnvironmentId();
		
		BDDMockito.doReturn(environment).when(spyEnvironmentService).get(id);
		BDDMockito.given(environmentDAL.save(environment)).willReturn(environment);
		
		Environment fetchedEnvironment = environmentService.addCapabilities(id, capabilities);
		
		assertNotNull(fetchedEnvironment);
		assertTrue(fetchedEnvironment instanceof Environment);
		assertEquals(environment.getEnvironmentId(), fetchedEnvironment.getEnvironmentId());
		assertEquals(environment.getEnvironmentName(), fetchedEnvironment.getEnvironmentName());
	}
	
	@Test
	void should_addStrategyToEnvironment_whenAddStrategy() {
		Integer id = environment.getEnvironmentId();
		
		BDDMockito.doReturn(environment).when(spyEnvironmentService).get(id);
		BDDMockito.given(environmentDAL.save(environment)).willReturn(environment);
		
		Environment fetchedEnvironment = environmentService.addStrategy(id, strategy);
		
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
