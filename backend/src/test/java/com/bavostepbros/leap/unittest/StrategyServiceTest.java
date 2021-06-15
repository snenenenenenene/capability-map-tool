package com.bavostepbros.leap.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
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

import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.Strategy;
import com.bavostepbros.leap.domain.service.strategyservice.StrategyService;
import com.bavostepbros.leap.domain.service.environmentservice.EnvironmentService;
import com.bavostepbros.leap.domain.service.statusservice.StatusService;
import com.bavostepbros.leap.persistence.EnvironmentDAL;
import com.bavostepbros.leap.persistence.StatusDAL;
import com.bavostepbros.leap.persistence.StrategyDAL;

/**
 *
 * @author Bavo Van Meel
 *
 */
@AutoConfigureMockMvc
@SpringBootTest
public class StrategyServiceTest {
	
	@SuppressWarnings("unused")
	@Autowired
    private MockMvc mockMvc;

	@Autowired
	private StrategyService strategyService;

	@Autowired
	private EnvironmentService environmentService;

	@Autowired
	private StatusService statusService;

	@MockBean
	private StrategyDAL strategyDAL;

	@MockBean
	private StatusDAL statusDAL;

	@MockBean
	private EnvironmentDAL environmentDAL;

	@SpyBean
	private StrategyService spyStrategyService;

	@SpyBean
	private StatusService spyStatusService;

	@SpyBean
	private EnvironmentService spyEnvironmentService;

	private Strategy strategyFirst;
	private Strategy strategySecond;
	private Status status;
	private Environment environment;
	private List<Strategy> strategies;
	private Optional<Status> optionalStatus;
	private Optional<Strategy> optionalStrategy;
	private Optional<Environment> optionalEnvironment;
	
	private Integer strategyId;
	private Integer statusId;
	private String strategyName;
	private LocalDate timeFrameStart;
	private LocalDate timeFrameEnd;
	private Integer environmentId;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@BeforeEach
	public void init() {
		status = new Status(1, LocalDate.of(2021, 5, 9));
		environment = new Environment(1, "Environment test");
		strategyFirst = new Strategy(1, status, "Strategy name", LocalDate.of(2021, 8, 10), LocalDate.of(2021, 8, 10),
				environment);
		strategySecond = new Strategy(2, status, "Strategy name 1", LocalDate.of(2021, 8, 10), LocalDate.of(2021, 8, 10),
						environment);
		strategies = List.of(strategyFirst, strategySecond);
		optionalStatus = Optional.of(status);
		optionalStrategy = Optional.of(strategyFirst);
		optionalEnvironment = Optional.of(environment);
		
		strategyId = strategyFirst.getStrategyId();
		statusId = strategyFirst.getStatus().getStatusId();
		strategyName = strategyFirst.getStrategyName();
		timeFrameStart = strategyFirst.getTimeFrameStart();
		timeFrameEnd = strategyFirst.getTimeFrameEnd();
		environmentId = strategyFirst.getEnvironment().getEnvironmentId();
	}

	@Test
	void should_notBeNull() {
		assertNotNull(strategyService);
		assertNotNull(environmentService);
		assertNotNull(statusService);
		assertNotNull(environmentDAL);
		assertNotNull(statusDAL);
		assertNotNull(strategyDAL);
		assertNotNull(status);
		assertNotNull(environment);
		assertNotNull(strategyFirst);
		assertNotNull(strategySecond);
		assertNotNull(strategies);
		assertNotNull(optionalStatus);
		assertNotNull(optionalStrategy);
		assertNotNull(optionalEnvironment);
	}

	@Test 
	void should_throwNoSuchElementException_whenSavedInputNameIsInvalid() {
		String strategyName = "";
		String expected = "No value present";

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> strategyService.save(statusId, strategyName, timeFrameStart, timeFrameEnd, environmentId));

		assertEquals(expected, exception.getMessage());
	}
	
	@Test 
	void should_throwNoSuchElementException_whenSavedInputNameIsDuplicate() {
		String strategyName = strategyFirst.getStrategyName();
		String expected = "No value present";

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> strategyService.save(statusId, strategyName, timeFrameStart, timeFrameEnd, environmentId));

		assertEquals(expected, exception.getMessage());
	}

	@Test 
	void should_throwNoSuchElementException_whenSavedStatusIdIsInvalid() {
		Integer statusId = 0;
		String expected = "No value present";

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> strategyService.save(statusId, strategyName, timeFrameStart, timeFrameEnd, environmentId));

		assertEquals(expected, exception.getMessage());
	}

	@Test 
	void should_throwNoSuchElementException_whenSavedEnvironmentIdIsInvalid() {
		Integer environmentId = 0;
		String expected = "No value present";

		Exception exception = assertThrows(NoSuchElementException.class,
				() -> strategyService.save(statusId, strategyName, timeFrameStart, timeFrameEnd, environmentId));

		assertEquals(expected, exception.getMessage());
	}

	@Test 
	void should_returnStrategy_whenStrategyIsSaved() {
		BDDMockito.given(statusDAL.findById(BDDMockito.anyInt())).willReturn(optionalStatus);
		BDDMockito.given(environmentDAL.findById(BDDMockito.anyInt())).willReturn(optionalEnvironment);
		BDDMockito.given(strategyDAL.save(BDDMockito.any(Strategy.class))).willReturn(strategyFirst);
		
		Strategy result = strategyService.save(statusId, strategyName, timeFrameStart, timeFrameEnd,
				environmentId);

		assertNotNull(result);
		assertTrue(result instanceof Strategy);
		testStrategy(strategyFirst, result);
	}

	@Test 
	void should_throwNoSuchElementException_whenGetStrategyIdNotValid() {
		Integer strategyId = 0;
		String expected = "No value present";

		Exception exception = assertThrows(NoSuchElementException.class, 
				() -> strategyService.get(strategyId));

		assertEquals(expected, exception.getMessage());
	}

	@Test 
	void should_throwNoSuchElementException_whenGetStrategyIdDoesNotExists() {
		Integer strategyId = strategyFirst.getStrategyId();
		String expected = "No value present";

		Exception exception = assertThrows(NoSuchElementException.class, 
				() -> strategyService.get(strategyId));

		assertEquals(expected, exception.getMessage());
	}

	@Test 
	void should_retrieveValidStrategy_whenIdIsValidAndIdExists() {
		Integer strategyId = strategyFirst.getStrategyId();

		BDDMockito.given(strategyDAL.findById(strategyId)).willReturn(optionalStrategy);
		Strategy fetchedStrategy = strategyService.get(strategyId);

		assertNotNull(fetchedStrategy);
		assertTrue(fetchedStrategy instanceof Strategy);
		testStrategy(strategyFirst, fetchedStrategy);
	}

	@Test 
	void should_retrieveStrategyList_whenGetAllIsCalled() {
		BDDMockito.given(strategyDAL.findAll()).willReturn(strategies);
		List<Strategy> fetchedStrategies = strategyService.getAll();

		assertNotNull(fetchedStrategies);
		assertEquals(strategies.size(), fetchedStrategies.size());
		testStrategy(strategyFirst, strategies.get(0));
		testStrategy(strategySecond, strategies.get(1));
	}

	@Test
	void should_throwNoSuchElementException_whenUpdatedInputNameIsInvalid() {
		String strategyName = "";
		String expected = "No value present";

		Exception exception = assertThrows(NoSuchElementException.class, 
				() -> strategyService.update(strategyId, statusId, strategyName, 
						timeFrameStart, timeFrameEnd, environmentId));

		assertEquals(expected, exception.getMessage());
	}

	@Test 
	void should_throwNoSuchElementException_whenUpdateStrategyIdDoesNotExist() {
		Integer strategyId = strategyFirst.getStrategyId() + 10;
		String expected = "No value present";

		Exception exception = assertThrows(NoSuchElementException.class, () -> strategyService.update(strategyId, statusId,
				strategyName, timeFrameStart, timeFrameEnd, environmentId));

		assertEquals(expected, exception.getMessage());
	}

	@Test 
	void should_throwNoSuchElementException_whenUpdateStrategyNameExist() {
		String falseStrategyName = strategyFirst.getStrategyName();
		String expected = "No value present";
		
		BDDMockito.given(strategyDAL.findById(BDDMockito.anyInt())).willReturn(optionalStrategy);

		Exception exception = assertThrows(NoSuchElementException.class, () -> strategyService.update(strategyId,
				statusId, falseStrategyName, timeFrameStart, timeFrameEnd, environmentId));

		assertEquals(expected, exception.getMessage());
	}

	@Test 
	void should_throwNoSuchElementException_whenUpdateStatusDoesNotExist() {
		Integer statusId = status.getStatusId() + 10;
		String expected = "No value present";
		
		BDDMockito.given(strategyDAL.findById(BDDMockito.anyInt())).willReturn(optionalStrategy);

		Exception exception = assertThrows(NoSuchElementException.class, () -> strategyService.update(strategyId, statusId,
				strategyName, timeFrameStart, timeFrameEnd, environmentId));

		assertEquals(expected, exception.getMessage());
	}

	@Test 
	void should_throwNoSuchElementException_whenUpdateEnvironmentDoesNotExist() {
		Integer environmentId = environment.getEnvironmentId() + 10;
		String expected = "No value present";
		
		BDDMockito.given(strategyDAL.findById(BDDMockito.anyInt())).willReturn(optionalStrategy);

		Exception exception = assertThrows(NoSuchElementException.class, () -> strategyService.update(strategyId, statusId,
				strategyName, timeFrameStart, timeFrameEnd, environmentId));

		assertEquals(expected, exception.getMessage());
	}

	@Test 
	void should_retrieveValidStrategy_whenStrategyIsUpdated() {
		String falseStrategyName = "Update test";
		strategyDAL.save(strategyFirst);

		BDDMockito.given(statusDAL.findById(BDDMockito.anyInt())).willReturn(optionalStatus);
		BDDMockito.given(environmentDAL.findById(BDDMockito.anyInt())).willReturn(optionalEnvironment);
		BDDMockito.given(strategyDAL.save(strategyFirst)).willReturn(strategyFirst);
		BDDMockito.given(strategyDAL.findById(BDDMockito.anyInt())).willReturn(optionalStrategy);
		
		Strategy fetchedStrategy = strategyService.update(strategyId, statusId, falseStrategyName, timeFrameStart,
				timeFrameEnd, environmentId);

		assertNotNull(fetchedStrategy);
		assertTrue(fetchedStrategy instanceof Strategy);
		testStrategy(strategyFirst, fetchedStrategy);
	}

	@Test 
	void should_throwIndexDoesNotExistException_whenDeleteInputDoesNotExists() {
		strategyService.delete(strategyId);

		Mockito.verify(strategyDAL, Mockito.times(1)).deleteById(Mockito.eq(strategyId));
	}

	@Test 
	void should_ReturnFalse_whenStrategyDoesNotExistById() {
		BDDMockito.given(strategyDAL.existsById(BDDMockito.anyInt())).willReturn(false);

		boolean result = strategyService.existsById(strategyFirst.getStrategyId());

		assertFalse(result);
	}

	@Test
	void should_ReturnTrue_whenStrategyDoesExistById() {
		BDDMockito.given(strategyDAL.existsById(BDDMockito.anyInt())).willReturn(true);

		boolean result = strategyService.existsById(strategyFirst.getStrategyId());

		assertTrue(result);
	}

	@Test 
	void should_ReturnTrue_whenStrategyDoesExistByStrategyName() {
		BDDMockito.given(strategyDAL.findByStrategyName(BDDMockito.any(String.class))).willReturn(optionalStrategy);

		boolean result = strategyService.existsByStrategyName(strategyFirst.getStrategyName());

		assertTrue(result);
	}

	@Test 
	void should_ReturnFalse_whenStrategyDoesNotExistByStrategyName() {
		BDDMockito.given(strategyDAL.findByStrategyName(BDDMockito.any(String.class))).willReturn(Optional.empty());

		boolean result = strategyService.existsByStrategyName(strategyFirst.getStrategyName());

		assertFalse(result);
	}

	@Test 
	void should_throwInvalidInputException_whenGetStrategiesByEnvironmentInputIsInvalid() {
		Integer environmentId = 0;
		String expected = "Environment does not exist.";

		Exception exception = assertThrows(NullPointerException.class,
				() -> strategyService.getStrategiesByEnvironment(environmentId));

		assertEquals(exception.getMessage(), expected);
	}

	@Test 
	void should_throwIndexDoesNotExistException_whenGetStrategiesByEnvironmentIdDoesNotExist() {
		Integer environmentId = strategyFirst.getEnvironment().getEnvironmentId() + 10;
		String expected = "Environment does not exist.";

		Exception exception = assertThrows(NullPointerException.class,
				() -> strategyService.getStrategiesByEnvironment(environmentId));

		assertEquals(exception.getMessage(), expected);
	}

	@Test 
	void should_retrieveValidEnvironment_whenGetStrategiesByEnvironmentIdDoesExistAndIsValid() {
		Integer environmentId = strategyFirst.getEnvironment().getEnvironmentId();

		BDDMockito.given(environmentDAL.findById(environmentId)).willReturn(optionalEnvironment);
		Environment fetchedEnvironment = environmentService.get(environmentId);

		assertNotNull(fetchedEnvironment);
		assertTrue(fetchedEnvironment instanceof Environment);
		assertEquals(strategyFirst.getEnvironment().getEnvironmentId(), fetchedEnvironment.getEnvironmentId());
		assertEquals(strategyFirst.getEnvironment().getEnvironmentName(), fetchedEnvironment.getEnvironmentName());
	}

	@Test
	void should_retrieveValidStrategies_whenGetStrategiesByEnvironmentIdDoesExistAndIsValid() {
		Integer environmentId = strategyFirst.getEnvironment().getEnvironmentId();

		BDDMockito.given(environmentDAL.findById(environmentId)).willReturn(optionalEnvironment);
		BDDMockito.given(strategyDAL.findByEnvironment(environment)).willReturn(strategies);
		List<Strategy> fetchedStrategies = strategyService.getStrategiesByEnvironment(environmentId);

		assertNotNull(fetchedStrategies);
		assertEquals(strategies.size(), fetchedStrategies.size());
		testStrategy(strategyFirst, strategies.get(0));
		testStrategy(strategySecond, strategies.get(1));
	}
	
	@Test 
	void should_throwNullPointerException_whenGetByNameStrategyNameNotValid() {
		String strategyName = "";
		String expected = "Strategy does not exist";

		Exception exception = assertThrows(NullPointerException.class, 
				() -> strategyService.getStrategyByStrategyname(strategyName));

		assertEquals(expected, exception.getMessage());
	}

	@Test 
	void should_throwNullPointerException_whenGetByNameStrategyNameDoesNotExists() {
		String strategyName = "Very strange query search";
		String expected = "Strategy does not exist";

		Exception exception = assertThrows(NullPointerException.class, 
				() -> strategyService.getStrategyByStrategyname(strategyName));

		assertEquals(expected, exception.getMessage());
	}

	@Test 
	void should_retrieveValidStrategy_whenGetByNameIsValidAndExists() {
		BDDMockito.given(strategyDAL.findByStrategyName(strategyName)).willReturn(optionalStrategy);
		
		Strategy fetchedStrategy = strategyService.getStrategyByStrategyname(strategyName);

		assertNotNull(fetchedStrategy);
		assertTrue(fetchedStrategy instanceof Strategy);
		testStrategy(strategyFirst, fetchedStrategy);
	}
	
	
	/** 
	 * @param expectedObject
	 * @param actualObject
	 */
	private void testStrategy(Strategy expectedObject, Strategy actualObject) {
		assertEquals(expectedObject.getStrategyId(), actualObject.getStrategyId());
		assertEquals(expectedObject.getStrategyName(), actualObject.getStrategyName());
		assertEquals(expectedObject.getStatus().getStatusId(), actualObject.getStatus().getStatusId());
		assertEquals(expectedObject.getStatus().getValidityPeriod(), actualObject.getStatus().getValidityPeriod());
		assertEquals(expectedObject.getTimeFrameStart(), actualObject.getTimeFrameStart());
		assertEquals(expectedObject.getTimeFrameEnd(), actualObject.getTimeFrameEnd());
		assertEquals(expectedObject.getEnvironment().getEnvironmentId(), actualObject.getEnvironment().getEnvironmentId());
		assertEquals(expectedObject.getEnvironment().getEnvironmentName(), actualObject.getEnvironment().getEnvironmentName());
	}
	 
}
