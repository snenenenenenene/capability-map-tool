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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.ForeignKeyException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.customexceptions.StrategyException;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.Strategy;
import com.bavostepbros.leap.domain.service.StrategyService.StrategyService;
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
@SpringBootTest
public class StrategyServiceTest {

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

	private Strategy strategy;
	private Status status;
	private Environment environment;
	private List<Strategy> strategies;
	private Optional<Status> optionalStatus;
	private Optional<Strategy> optionalStrategy;
	private Optional<Environment> optionalEnvironment;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@BeforeEach
	public void init() {
		status = new Status(1, LocalDate.of(2021, 5, 9));
		environment = new Environment(1, "Environment test");
		strategy = new Strategy(1, status, "Strategy name", LocalDate.of(2021, 8, 10), LocalDate.of(2021, 8, 10),
				environment);
		strategies = List.of(
				new Strategy(status, "Strategy name", LocalDate.of(2021, 8, 10), LocalDate.of(2021, 8, 10),
						environment),
				new Strategy(status, "Strategy name 1", LocalDate.of(2021, 8, 10), LocalDate.of(2021, 8, 10),
						environment));
		optionalStatus = Optional.of(status);
		optionalStrategy = Optional.of(strategy);
		optionalEnvironment = Optional.of(environment);
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
		assertNotNull(strategy);
		assertNotNull(strategies);
		assertNotNull(optionalStatus);
		assertNotNull(optionalStrategy);
		assertNotNull(optionalEnvironment);
	}

	@Test
	void should_throwInvalidInputException_whenSavedInputIsInvalid() {
		String falseStrategyName = "";
		LocalDate timeFrameStart = strategy.getTimeFrameStart();
		LocalDate timeFrameEnd = strategy.getTimeFrameEnd();
		Integer statusId = status.getStatusId();
		Integer environmentId = environment.getEnvironmentId();
		String expected = "Invalid input.";

		Exception exception = assertThrows(InvalidInputException.class,
				() -> strategyService.save(statusId, falseStrategyName, timeFrameStart, timeFrameEnd, environmentId));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwForeignKeyException_whenSavedStatusIdIsInvalid() {
		String falseStrategyName = strategy.getStrategyName();
		LocalDate timeFrameStart = strategy.getTimeFrameStart();
		LocalDate timeFrameEnd = strategy.getTimeFrameEnd();
		Integer statusId = 0;
		Integer environmentId = environment.getEnvironmentId();
		String expected = "Status ID is invalid.";

		Exception exception = assertThrows(ForeignKeyException.class,
				() -> strategyService.save(statusId, falseStrategyName, timeFrameStart, timeFrameEnd, environmentId));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwForeignKeyException_whenSavedEnvironmentIdIsInvalid() {
		String falseStrategyName = strategy.getStrategyName();
		LocalDate timeFrameStart = strategy.getTimeFrameStart();
		LocalDate timeFrameEnd = strategy.getTimeFrameEnd();
		Integer statusId = status.getStatusId();
		Integer environmentId = 0;
		String expected = "Environment ID is invalid.";

		Exception exception = assertThrows(ForeignKeyException.class,
				() -> strategyService.save(statusId, falseStrategyName, timeFrameStart, timeFrameEnd, environmentId));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwDuplicateValueException_whenSavedStrategyNameExists() {
		String falseStrategyName = strategy.getStrategyName();
		LocalDate timeFrameStart = strategy.getTimeFrameStart();
		LocalDate timeFrameEnd = strategy.getTimeFrameEnd();
		Integer statusId = status.getStatusId();
		Integer environmentId = environment.getEnvironmentId();
		String expected = "Strategy name already exists.";

		BDDMockito.doReturn(true).when(spyStrategyService).existsByStrategyName(falseStrategyName);

		Exception exception = assertThrows(DuplicateValueException.class,
				() -> strategyService.save(statusId, falseStrategyName, timeFrameStart, timeFrameEnd, environmentId));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwForeignKeyException_whenSavedStatusDoesNotExists() {
		String falseStrategyName = strategy.getStrategyName();
		LocalDate timeFrameStart = strategy.getTimeFrameStart();
		LocalDate timeFrameEnd = strategy.getTimeFrameEnd();
		Integer statusId = status.getStatusId();
		Integer environmentId = environment.getEnvironmentId();
		String expected = "Status ID does not exists.";

		BDDMockito.doReturn(false).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(ForeignKeyException.class,
				() -> strategyService.save(statusId, falseStrategyName, timeFrameStart, timeFrameEnd, environmentId));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwForeignKeyException_whenSavedEnvironmentDoesNotExists() {
		String falseStrategyName = strategy.getStrategyName();
		LocalDate timeFrameStart = strategy.getTimeFrameStart();
		LocalDate timeFrameEnd = strategy.getTimeFrameEnd();
		Integer statusId = status.getStatusId();
		Integer environmentId = environment.getEnvironmentId();
		String expected = "Environment ID does not exists.";

		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);
		BDDMockito.doReturn(false).when(spyEnvironmentService).existsById(environmentId);

		Exception exception = assertThrows(ForeignKeyException.class,
				() -> strategyService.save(statusId, falseStrategyName, timeFrameStart, timeFrameEnd, environmentId));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_saveEnvironment_whenEnvironmentIsSaved() {
		String falseStrategyName = strategy.getStrategyName();
		LocalDate timeFrameStart = strategy.getTimeFrameStart();
		LocalDate timeFrameEnd = strategy.getTimeFrameEnd();
		Integer statusId = status.getStatusId();
		Integer environmentId = environment.getEnvironmentId();

		BDDMockito.doReturn(false).when(spyStrategyService).existsByStrategyName(falseStrategyName);
		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);
		BDDMockito.doReturn(true).when(spyEnvironmentService).existsById(environmentId);

		BDDMockito.given(statusDAL.findById(BDDMockito.anyInt())).willReturn(optionalStatus);
		BDDMockito.given(environmentDAL.findById(BDDMockito.anyInt())).willReturn(optionalEnvironment);
		BDDMockito.given(strategyDAL.save(BDDMockito.any(Strategy.class))).willReturn(strategy);
		
		Strategy result = strategyService.save(statusId, falseStrategyName, timeFrameStart, timeFrameEnd,
				environmentId);

		assertNotNull(result);
		assertTrue(result instanceof Strategy);
		assertEquals(strategy.getStrategyId(), result.getStrategyId());
		assertEquals(strategy.getStrategyName(), result.getStrategyName());
		assertEquals(strategy.getStatus().getStatusId(), result.getStatus().getStatusId());
		assertEquals(strategy.getStatus().getValidityPeriod(), result.getStatus().getValidityPeriod());
		assertEquals(strategy.getTimeFrameStart(), result.getTimeFrameStart());
		assertEquals(strategy.getTimeFrameEnd(), result.getTimeFrameEnd());
		assertEquals(strategy.getEnvironment().getEnvironmentId(), result.getEnvironment().getEnvironmentId());
		assertEquals(strategy.getEnvironment().getEnvironmentName(), result.getEnvironment().getEnvironmentName());
	}

	@Test
	void should_throwInvalidInputException_whenGetStrategyIdNotValid() {
		Integer strategyId = 0;
		String expected = "Strategy ID is not valid.";

		Exception exception = assertThrows(InvalidInputException.class, 
				() -> strategyService.get(strategyId));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwIndexDoesNotExistException_whenGetStrategyIdDoesNotExists() {
		Integer strategyId = strategy.getStrategyId();
		String expected = "Strategy ID does not exists.";

		Exception exception = assertThrows(IndexDoesNotExistException.class, 
				() -> strategyService.get(strategyId));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_retrieveValidStrategy_whenIdIsValidAndIdExists() {
		Integer strategyId = strategy.getStrategyId();

		BDDMockito.doReturn(true).when(spyStrategyService).existsById(strategyId);
		BDDMockito.given(strategyDAL.findById(strategyId)).willReturn(optionalStrategy);
		Strategy fetchedStrategy = strategyService.get(strategyId);

		assertNotNull(fetchedStrategy);
		assertTrue(fetchedStrategy instanceof Strategy);
		assertEquals(strategy.getStrategyId(), fetchedStrategy.getStrategyId());
		assertEquals(strategy.getStrategyName(), fetchedStrategy.getStrategyName());
		assertEquals(strategy.getStatus().getStatusId(), fetchedStrategy.getStatus().getStatusId());
		assertEquals(strategy.getStatus().getValidityPeriod(), fetchedStrategy.getStatus().getValidityPeriod());
		assertEquals(strategy.getTimeFrameStart(), fetchedStrategy.getTimeFrameStart());
		assertEquals(strategy.getTimeFrameEnd(), fetchedStrategy.getTimeFrameEnd());
		assertEquals(strategy.getEnvironment().getEnvironmentId(), fetchedStrategy.getEnvironment().getEnvironmentId());
		assertEquals(strategy.getEnvironment().getEnvironmentName(), fetchedStrategy.getEnvironment().getEnvironmentName());
	}

	@Test
	void should_retrieveEnvironmentList_whenGetAllIsCalled() {
		BDDMockito.given(strategyDAL.findAll()).willReturn(strategies);
		List<Strategy> fetchedStrategies = strategyService.getAll();

		assertNotNull(fetchedStrategies);
		assertEquals(strategies.size(), fetchedStrategies.size());
	}

	@Test
	void should_throwInvalidInputException_whenUpdatedInputIsInvalid() {
		Integer strategyId = strategy.getStrategyId();
		String falseStrategyName = "";
		LocalDate timeFrameStart = strategy.getTimeFrameStart();
		LocalDate timeFrameEnd = strategy.getTimeFrameEnd();
		Integer statusId = status.getStatusId();
		Integer environmentId = environment.getEnvironmentId();
		String expected = "Invalid input.";

		Exception exception = assertThrows(InvalidInputException.class, 
				() -> strategyService.update(strategyId, statusId, falseStrategyName, 
						timeFrameStart, timeFrameEnd, environmentId));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwStrategyException_whenUpdateStrategyIdDoesNotExist() {
		Integer strategyId = strategy.getStrategyId();
		String falseStrategyName = strategy.getStrategyName();
		LocalDate timeFrameStart = strategy.getTimeFrameStart();
		LocalDate timeFrameEnd = strategy.getTimeFrameEnd();
		Integer statusId = status.getStatusId();
		Integer environmentId = environment.getEnvironmentId();
		String expected = "Can not update strategy if it does not exist.";

		BDDMockito.doReturn(false).when(spyStrategyService).existsById(strategyId);

		Exception exception = assertThrows(StrategyException.class, () -> strategyService.update(strategyId, statusId,
				falseStrategyName, timeFrameStart, timeFrameEnd, environmentId));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwDuplicateValueException_whenUpdateStrategyNameExist() {
		Integer strategyId = strategy.getStrategyId();
		String falseStrategyName = strategy.getStrategyName();
		LocalDate timeFrameStart = strategy.getTimeFrameStart();
		LocalDate timeFrameEnd = strategy.getTimeFrameEnd();
		Integer statusId = status.getStatusId();
		Integer environmentId = environment.getEnvironmentId();
		String expected = "Strategy name already exists.";

		BDDMockito.doReturn(true).when(spyStrategyService).existsById(strategyId);
		BDDMockito.doReturn(true).when(spyStrategyService).existsByStrategyName(falseStrategyName);

		Exception exception = assertThrows(DuplicateValueException.class, () -> strategyService.update(strategyId,
				statusId, falseStrategyName, timeFrameStart, timeFrameEnd, environmentId));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwIndexDoesNotExistException_whenUpdateStatusDoesNotExist() {
		Integer strategyId = strategy.getStrategyId();
		String falseStrategyName = strategy.getStrategyName();
		LocalDate timeFrameStart = strategy.getTimeFrameStart();
		LocalDate timeFrameEnd = strategy.getTimeFrameEnd();
		Integer statusId = status.getStatusId();
		Integer environmentId = environment.getEnvironmentId();
		String expected = "Status ID does not exists.";

		BDDMockito.doReturn(true).when(spyStrategyService).existsById(strategyId);
		BDDMockito.doReturn(false).when(spyStrategyService).existsByStrategyName(falseStrategyName);
		BDDMockito.doReturn(false).when(spyStatusService).existsById(statusId);

		Exception exception = assertThrows(ForeignKeyException.class, () -> strategyService.update(strategyId, statusId,
				falseStrategyName, timeFrameStart, timeFrameEnd, environmentId));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_throwIndexDoesNotExistException_whenUpdateEnvironmentDoesNotExist() {
		Integer strategyId = strategy.getStrategyId();
		String falseStrategyName = strategy.getStrategyName();
		LocalDate timeFrameStart = strategy.getTimeFrameStart();
		LocalDate timeFrameEnd = strategy.getTimeFrameEnd();
		Integer statusId = status.getStatusId();
		Integer environmentId = environment.getEnvironmentId();
		String expected = "Environment ID does not exists.";

		BDDMockito.doReturn(true).when(spyStrategyService).existsById(strategyId);
		BDDMockito.doReturn(false).when(spyStrategyService).existsByStrategyName(falseStrategyName);
		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);
		BDDMockito.doReturn(false).when(spyEnvironmentService).existsById(statusId);

		Exception exception = assertThrows(ForeignKeyException.class, () -> strategyService.update(strategyId, statusId,
				falseStrategyName, timeFrameStart, timeFrameEnd, environmentId));

		assertEquals(expected, exception.getMessage());
	}

	@Test
	void should_retrieveValidStrategy_whenStrategyIsUpdated() {
		Integer strategyId = strategy.getStrategyId();
		String falseStrategyName = strategy.getStrategyName();
		LocalDate timeFrameStart = strategy.getTimeFrameStart();
		LocalDate timeFrameEnd = strategy.getTimeFrameEnd();
		Integer statusId = status.getStatusId();
		Integer environmentId = environment.getEnvironmentId();
		strategyDAL.save(strategy);

		BDDMockito.doReturn(true).when(spyStrategyService).existsById(strategyId);
		BDDMockito.doReturn(false).when(spyStrategyService).existsByStrategyName(falseStrategyName);
		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);
		BDDMockito.doReturn(true).when(spyEnvironmentService).existsById(statusId);

		BDDMockito.given(statusDAL.findById(BDDMockito.anyInt())).willReturn(optionalStatus);
		BDDMockito.given(environmentDAL.findById(BDDMockito.anyInt())).willReturn(optionalEnvironment);
		BDDMockito.given(strategyDAL.save(strategy)).willReturn(strategy);
		
		Strategy fetchedStrategy = strategyService.update(strategyId, statusId, falseStrategyName, timeFrameStart,
				timeFrameEnd, environmentId);

		assertNotNull(fetchedStrategy);
		assertTrue(fetchedStrategy instanceof Strategy);
		assertEquals(strategy.getStrategyId(), fetchedStrategy.getStrategyId());
		assertEquals(strategy.getStrategyName(), fetchedStrategy.getStrategyName());
		assertEquals(strategy.getStatus().getStatusId(), fetchedStrategy.getStatus().getStatusId());
		assertEquals(strategy.getStatus().getValidityPeriod(), fetchedStrategy.getStatus().getValidityPeriod());
		assertEquals(strategy.getTimeFrameStart(), fetchedStrategy.getTimeFrameStart());
		assertEquals(strategy.getTimeFrameEnd(), fetchedStrategy.getTimeFrameEnd());
		assertEquals(strategy.getEnvironment().getEnvironmentId(), fetchedStrategy.getEnvironment().getEnvironmentId());
		assertEquals(strategy.getEnvironment().getEnvironmentName(), fetchedStrategy.getEnvironment().getEnvironmentName());
	}

	@Test
	void should_throwInvalidInputException_whenDeleteInputIsInvalid() {
		Integer strategyId = 0;
		String expected = "Strategy ID is not valid.";

		Exception exception = assertThrows(InvalidInputException.class, 
				() -> strategyService.delete(strategyId));

		assertEquals(exception.getMessage(), expected);
	}

	@Test
	void should_throwIndexDoesNotExistException_whenDeleteStrategyIdDoesNotExist() {
		Integer id = strategy.getStrategyId();
		String expected = "Strategy ID does not exists.";
		BDDMockito.doReturn(false).when(spyStatusService).existsById(id);

		Exception exception = assertThrows(IndexDoesNotExistException.class, 
				() -> strategyService.delete(id));

		assertEquals(exception.getMessage(), expected);
	}

	@Test
	void should_throwIndexDoesNotExistException_whenDeleteInputDoesNotExists() {
		Integer strategyId = strategy.getStrategyId();
		String expected = "Strategy ID does not exists.";

		strategyDAL.deleteById(strategyId);

		Exception exception = assertThrows(IndexDoesNotExistException.class, 
				() -> strategyService.get(strategyId));

		assertEquals(exception.getMessage(), expected);
	}

	@Test
	void should_ReturnFalse_whenStrategyDoesNotExistById() {
		BDDMockito.given(strategyDAL.existsById(BDDMockito.anyInt())).willReturn(false);

		boolean result = strategyService.existsById(strategy.getStrategyId());

		assertFalse(result);
	}

	@Test
	void should_ReturnTrue_whenStrategyDoesExistById() {
		BDDMockito.given(strategyDAL.existsById(BDDMockito.anyInt())).willReturn(true);

		boolean result = strategyService.existsById(strategy.getStrategyId());

		assertTrue(result);
	}

	@Test
	void should_ReturnTrue_whenStrategyDoesExistByStrategyName() {
		BDDMockito.given(strategyDAL.findByStrategyName(BDDMockito.any(String.class))).willReturn(optionalStrategy);

		boolean result = strategyService.existsByStrategyName(strategy.getStrategyName());

		assertTrue(result);
	}

	@Test
	void should_ReturnFalse_whenStrategyDoesNotExistByStrategyName() {
		BDDMockito.given(strategyDAL.findByStrategyName(BDDMockito.any(String.class))).willReturn(Optional.empty());

		boolean result = strategyService.existsByStrategyName(strategy.getStrategyName());

		assertFalse(result);
	}

	@Test
	void should_throwInvalidInputException_whenGetStrategiesByEnvironmentInputIsInvalid() {
		Integer environmentId = 0;
		String expected = "Environment ID is not valid.";

		Exception exception = assertThrows(InvalidInputException.class,
				() -> strategyService.getStrategiesByEnvironment(environmentId));

		assertEquals(exception.getMessage(), expected);
	}

	@Test
	void should_throwIndexDoesNotExistException_whenGetStrategiesByEnvironmentIdDoesNotExist() {
		Integer environmentId = strategy.getEnvironment().getEnvironmentId();
		String expected = "Environment ID does not exists.";
		
		BDDMockito.doReturn(false).when(spyEnvironmentService).existsById(environmentId);

		Exception exception = assertThrows(ForeignKeyException.class,
				() -> strategyService.getStrategiesByEnvironment(environmentId));

		assertEquals(exception.getMessage(), expected);
	}

	@Test
	void should_retrieveValidEnvironment_whenGetStrategiesByEnvironmentIdDoesExistAndIsValid() {
		Integer environmentId = strategy.getEnvironment().getEnvironmentId();

		BDDMockito.doReturn(true).when(spyEnvironmentService).existsById(environmentId);
		BDDMockito.given(environmentDAL.findById(environmentId)).willReturn(optionalEnvironment);
		Environment fetchedEnvironment = environmentService.get(environmentId);

		assertNotNull(fetchedEnvironment);
		assertTrue(fetchedEnvironment instanceof Environment);
		assertEquals(strategy.getEnvironment().getEnvironmentId(), fetchedEnvironment.getEnvironmentId());
		assertEquals(strategy.getEnvironment().getEnvironmentName(), fetchedEnvironment.getEnvironmentName());
	}

	@Test
	void should_retrieveValidStrategies_whenGetStrategiesByEnvironmentIdDoesExistAndIsValid() {
		Integer environmentId = strategy.getEnvironment().getEnvironmentId();

		BDDMockito.doReturn(true).when(spyEnvironmentService).existsById(environmentId);
		BDDMockito.given(environmentDAL.findById(environmentId)).willReturn(optionalEnvironment);
		BDDMockito.given(strategyDAL.findByEnvironment(environment)).willReturn(strategies);
		List<Strategy> fetchedStrategies = strategyService.getStrategiesByEnvironment(environmentId);

		assertNotNull(fetchedStrategies);
		assertEquals(strategies.size(), fetchedStrategies.size());
	}
	 
}
