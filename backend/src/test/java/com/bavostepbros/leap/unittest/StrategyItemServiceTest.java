package com.bavostepbros.leap.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.Strategy;
import com.bavostepbros.leap.domain.model.StrategyItem;
import com.bavostepbros.leap.domain.service.environmentservice.EnvironmentService;
import com.bavostepbros.leap.domain.service.statusservice.StatusService;
import com.bavostepbros.leap.domain.service.strategyitemservice.StrategyItemService;
import com.bavostepbros.leap.domain.service.strategyservice.StrategyService;
import com.bavostepbros.leap.persistence.EnvironmentDAL;
import com.bavostepbros.leap.persistence.StatusDAL;
import com.bavostepbros.leap.persistence.StrategyDAL;
import com.bavostepbros.leap.persistence.StrategyItemDAL;

@AutoConfigureMockMvc
@SpringBootTest
public class StrategyItemServiceTest {

	@SuppressWarnings("unused")
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private StrategyItemService strategyItemService;

	@Autowired
	private StrategyService strategyService;

	@Autowired
	private EnvironmentService environmentService;

	@Autowired
	private StatusService statusService;

	@MockBean
	private StatusDAL statusDAL;

	@MockBean
	private EnvironmentDAL environmentDAL;

	@MockBean
	private StrategyDAL strategyDAL;

	@MockBean
	private StrategyItemDAL strategyItemDAL;
	
	@SpyBean
	private StrategyService spyStrategyService;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private Status statusFirst;
	private Status statusSecond;
	private Environment environmentFirst;
	private Environment environmentSecond;
	private Strategy strategyFirst;
	private Strategy strategySecond;
	private StrategyItem strategyItemFirst;
	private StrategyItem strategyItemSecond;
	private Optional<Strategy> optionalStrategyFirst;
	
	private Integer itemId;
	private Integer strategyId;
	private String strategyItemName;
	private String description;

	@BeforeEach
	public void init() {
		statusFirst = new Status(1, LocalDate.of(2021, 05, 15));
		statusSecond = new Status(2, LocalDate.of(2021, 05, 20));
		environmentFirst = new Environment(1, "Test 1");
		environmentSecond = new Environment(2, "Test 2");
		strategyFirst = new Strategy(1, statusFirst, "Strategy 1", LocalDate.of(2021, 05, 15),
				LocalDate.of(2021, 05, 20), environmentFirst);
		strategySecond = new Strategy(2, statusSecond, "Strategy 2", LocalDate.of(2021, 05, 15),
				LocalDate.of(2021, 05, 20), environmentSecond);
		strategyItemFirst = new StrategyItem(1, strategyFirst, "StrategyItem 1", "Description 1");
		strategyItemSecond = new StrategyItem(2, strategySecond, "StrategyItem 2", "Description 2");
		optionalStrategyFirst = Optional.of(strategyFirst);
		
		itemId = strategyItemFirst.getItemId();
		strategyId = strategyItemFirst.getStrategy().getStrategyId();
		strategyItemName = strategyItemFirst.getStrategyItemName();
		description = strategyItemFirst.getDescription();
	}
	
	@Test
	void should_notBeNull() {
		assertNotNull(statusDAL);
		assertNotNull(environmentDAL);
		assertNotNull(strategyDAL);
		assertNotNull(strategyItemDAL);
		assertNotNull(statusService);
		assertNotNull(environmentService);
		assertNotNull(strategyService);
		assertNotNull(strategyItemService);
		assertNotNull(statusFirst);
		assertNotNull(statusSecond);
		assertNotNull(environmentFirst);
		assertNotNull(environmentSecond);
		assertNotNull(strategyFirst);
		assertNotNull(strategySecond);
		assertNotNull(strategyItemFirst);
		assertNotNull(strategyItemSecond);
	}
	
//	@Test
//	void should_throwNoSuchElementException_whenSavedStrategyItemIsNull() {
//		Integer strategyId = null;
//		String expected = "No value present";
//		
//		BDDMockito.given(strategyService.get(strategyId)).willReturn(strategyFirst);
//		
//		Exception exception = assertThrows(NoSuchElementException.class,
//				() -> strategyItemService.save(strategyId, strategyItemName, description));
//		
//		assertEquals(expected, exception.getMessage());
//	}
//	
//	@Test
//	void should_throwNoSuchElementException_whenSavedStrategyItemNameIsBlank() {
//		String strategyItemName = "";
//		String expected = "No value present";
//		
//		BDDMockito.given(strategyService.get(strategyId)).willReturn(strategyFirst);
//		
//		Exception exception = assertThrows(NoSuchElementException.class,
//				() -> strategyItemService.save(strategyId, strategyItemName, description));
//		
//		assertEquals(expected, exception.getMessage());
//	}
}
