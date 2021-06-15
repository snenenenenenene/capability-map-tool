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
	private List<StrategyItem> strategyItems;
	private Optional<Strategy> optionalStrategyFirst;
	private Optional<StrategyItem> optionalStrategyItemFirst;
	
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
		strategyItems = List.of(strategyItemFirst, strategyItemSecond);
		optionalStrategyFirst = Optional.of(strategyFirst);
		optionalStrategyItemFirst = Optional.of(strategyItemFirst);
		
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
	
	@Test
	void should_throwNoSuchElementException_whenSavedInpuStrategyIdIsNull() {
		Integer strategyId = null;
		String expected = "No value present";
		
		Exception exception = assertThrows(NoSuchElementException.class,
				() -> strategyItemService.save(strategyId, strategyItemName, description));
		
		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_throwNoSuchElementException_whenSavedInpuStrategyIdDoesNotExist() {
		Integer strategyId = strategyItemFirst.getStrategy().getStrategyId() + 10;
		String expected = "No value present";
		
		Exception exception = assertThrows(NoSuchElementException.class,
				() -> strategyItemService.save(strategyId, strategyItemName, description));
		
		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_throwNoSuchElementException_whenSavedStrategyItemNameIsBlank() {
		String strategyItemName = "";
		String expected = "No value present";
		
		Exception exception = assertThrows(NoSuchElementException.class,
				() -> strategyItemService.save(strategyId, strategyItemName, description));
		
		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_throwNoSuchElementException_whenSavedStrategyItemNameIsDuplicate() {
		String strategyItemName = strategyItemFirst.getStrategyItemName();
		String expected = "No value present";
		
		Exception exception = assertThrows(NoSuchElementException.class,
				() -> strategyItemService.save(strategyId, strategyItemName, description));
		
		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_throwNoSuchElementException_whenSavedStrategyItemDescriptionIsBlank() {
		String description = "";
		String expected = "No value present";
		
		Exception exception = assertThrows(NoSuchElementException.class,
				() -> strategyItemService.save(strategyId, strategyItemName, description));
		
		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_retrieveStrategyItem_whenSavedStrategyItem() {
		BDDMockito.given(strategyDAL.findById(BDDMockito.anyInt())).willReturn(optionalStrategyFirst);
		BDDMockito.given(strategyItemDAL.save(BDDMockito.any(StrategyItem.class))).willReturn(strategyItemFirst);
		
		StrategyItem strategyItem = strategyItemService.save(strategyId, strategyItemName, description);
		
		assertNotNull(strategyItem);
		assertTrue(strategyItem instanceof StrategyItem);
		testStrategyItem(strategyItemFirst, strategyItem);
	}
	
	@Test 
	void should_throwNoSuchElementException_whenGetStrategyItemIdNotValid() {
		Integer itemId = 0;
		String expected = "No value present";

		Exception exception = assertThrows(NoSuchElementException.class, 
				() -> strategyItemService.get(itemId));

		assertEquals(expected, exception.getMessage());
	}
	
	@Test 
	void should_throwNoSuchElementException_whenGetStrategyItemIdDoesNotExists() {
		Integer itemId = strategyItemFirst.getItemId();
		String expected = "No value present";

		Exception exception = assertThrows(NoSuchElementException.class, 
				() -> strategyItemService.get(itemId));

		assertEquals(expected, exception.getMessage());
	}
	
	@Test 
	void should_retrieveValidStrategy_whenIdIsValidAndIdExists() {
		Integer itemId = strategyFirst.getStrategyId();

		BDDMockito.given(strategyItemDAL.findById(itemId)).willReturn(optionalStrategyItemFirst);
		StrategyItem fetchedStrategyItem = strategyItemService.get(itemId);

		assertNotNull(fetchedStrategyItem);
		assertTrue(fetchedStrategyItem instanceof StrategyItem);
		testStrategyItem(strategyItemFirst, fetchedStrategyItem);
	}
	
	@Test
	void should_throwNoSuchElementException_whenUpdateInpuStrategyIdIsNull() {
		Integer strategyId = null;
		String expected = "No value present";
		
		Exception exception = assertThrows(NoSuchElementException.class,
				() -> strategyItemService.update(itemId, strategyId, strategyItemName, description));
		
		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_throwNoSuchElementException_whenUpdateInpuStrategyIdDoesNotExist() {
		Integer strategyId = strategyItemFirst.getStrategy().getStrategyId() + 10;
		String expected = "No value present";
		
		Exception exception = assertThrows(NoSuchElementException.class,
				() -> strategyItemService.update(itemId, strategyId, strategyItemName, description));
		
		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_throwNoSuchElementException_whenUpdateStrategyItemNameIsBlank() {
		String strategyItemName = "";
		String expected = "No value present";
		
		Exception exception = assertThrows(NoSuchElementException.class,
				() -> strategyItemService.update(itemId, strategyId, strategyItemName, description));
		
		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_throwNoSuchElementException_whenUpdateStrategyItemDescriptionIsBlank() {
		String description = "";
		String expected = "No value present";
		
		Exception exception = assertThrows(NoSuchElementException.class,
				() -> strategyItemService.update(itemId, strategyId, strategyItemName, description));
		
		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_retrieveStrategyItem_whenUpdateStrategyItem() {
		BDDMockito.given(strategyDAL.findById(BDDMockito.anyInt())).willReturn(optionalStrategyFirst);
		BDDMockito.given(strategyItemDAL.save(BDDMockito.any(StrategyItem.class))).willReturn(strategyItemFirst);
		
		StrategyItem strategyItem = strategyItemService.update(itemId, strategyId, strategyItemName, description);
		
		assertNotNull(strategyItem);
		assertTrue(strategyItem instanceof StrategyItem);
		testStrategyItem(strategyItemFirst, strategyItem);
	}
	
	@Test 
	void should_throwIndexDoesNotExistException_whenDeleteInputDoesNotExists() {
		strategyItemService.delete(itemId);

		Mockito.verify(strategyItemDAL, Mockito.times(1)).deleteById(Mockito.eq(itemId));
	}
	
	@Test 
	void should_retrieveStrategyItemList_whenGetAllIsCalled() {
		BDDMockito.given(strategyItemDAL.findAll()).willReturn(strategyItems);
		List<StrategyItem> fetchedStrategyItems = strategyItemService.getAll();

		assertNotNull(fetchedStrategyItems);
		assertEquals(strategyItems.size(), fetchedStrategyItems.size());
		testStrategyItem(strategyItemFirst, fetchedStrategyItems.get(0));
		testStrategyItem(strategyItemSecond, fetchedStrategyItems.get(1));
	}
	
	@Test 
	void should_ReturnFalse_whenStrategyItemDoesNotExistById() {
		BDDMockito.given(strategyItemDAL.existsById(BDDMockito.anyInt())).willReturn(false);

		boolean result = strategyItemService.existsById(strategyItemFirst.getItemId());

		assertFalse(result);
	}

	@Test
	void should_ReturnTrue_whenStrategyItemDoesExistById() {
		BDDMockito.given(strategyItemDAL.existsById(BDDMockito.anyInt())).willReturn(true);

		boolean result = strategyItemService.existsById(strategyItemFirst.getItemId());

		assertTrue(result);
	}
	
	@Test 
	void should_ReturnFalse_whenStrategyItemDoesNotExistByName() {
		BDDMockito.given(strategyItemDAL.findByStrategyItemName(BDDMockito.any(String.class))).willReturn(Optional.empty());

		boolean result = strategyItemService.existsByStrategyItemName(strategyItemFirst.getStrategyItemName());

		assertFalse(result);
	}

	@Test
	void should_ReturnTrue_whenStrategyItemDoesExistByName() {
		BDDMockito.given(strategyItemDAL.findByStrategyItemName(BDDMockito.any(String.class))).willReturn(optionalStrategyItemFirst);

		boolean result = strategyItemService.existsByStrategyItemName(strategyItemFirst.getStrategyItemName());

		assertTrue(result);
	}
	
	@Test 
	void should_throwNullPointerException_whenGetByStrategyItemNameNotValid() {
		String strategyItemName = "";
		String expected = "StrategyItem does not exist";

		Exception exception = assertThrows(NullPointerException.class, 
				() -> strategyItemService.getStrategyItemByStrategyItemName(strategyItemName));

		assertEquals(expected, exception.getMessage());
	}
	
	@Test 
	void should_throwNullPointerException_whenGetByStrategyItemNameDoesNotExists() {
		String strategyItemName = "A search that does not exist";
		String expected = "StrategyItem does not exist";

		Exception exception = assertThrows(NullPointerException.class, 
				() -> strategyItemService.getStrategyItemByStrategyItemName(strategyItemName));

		assertEquals(expected, exception.getMessage());
	}
	
	@Test 
	void should_retrieveValidStrategyItem_whenGetByStrategyItemName() {
		BDDMockito.given(strategyItemDAL.findByStrategyItemName(BDDMockito.any(String.class))).willReturn(optionalStrategyItemFirst);

		StrategyItem strategyItem = strategyItemService.getStrategyItemByStrategyItemName(strategyItemName);

		assertNotNull(strategyItem);
		assertTrue(strategyItem instanceof StrategyItem);
		testStrategyItem(strategyItemFirst, strategyItem);
	}
	
	@Test 
	void should_throwNoSuchElementException_whenGetStrategyItemsIdNotValid() {
		Integer strategyId = 0;
		String expected = "No value present";

		Exception exception = assertThrows(NoSuchElementException.class, 
				() -> strategyItemService.getStrategyItemsByStrategy(strategyId));

		assertEquals(expected, exception.getMessage());
	}
	
	@Test 
	void should_throwNoSuchElementException_whenGetStrategyItemsByStrategyIdDoesNotExists() {
		Integer strategyId = strategyItemFirst.getItemId() + 10;
		String expected = "No value present";

		Exception exception = assertThrows(NoSuchElementException.class, 
				() -> strategyItemService.getStrategyItemsByStrategy(strategyId));

		assertEquals(expected, exception.getMessage());
	}
	
	@Test 
	void should_retrieveValidStrategyItems_whenGetStrategyItemsByStrategy() {
		Integer strategyId = strategyItemFirst.getItemId();

		BDDMockito.given(strategyDAL.findById(strategyId)).willReturn(optionalStrategyFirst);
		BDDMockito.given(strategyItemDAL.findByStrategy(strategyFirst)).willReturn(strategyItems);
		
		List<StrategyItem> fetchedStrategyItems = strategyItemService.getStrategyItemsByStrategy(strategyId);

		assertNotNull(fetchedStrategyItems);
		assertEquals(strategyItems.size(), fetchedStrategyItems.size());
		testStrategyItem(strategyItemFirst, fetchedStrategyItems.get(0));
		testStrategyItem(strategyItemSecond, fetchedStrategyItems.get(1));
	}
	
	
	/** 
	 * @param expectedObject
	 * @param actualObject
	 */
	private void testStrategyItem(StrategyItem expectedObject, StrategyItem actualObject) {
		assertEquals(expectedObject.getItemId(), actualObject.getItemId());
		assertEquals(expectedObject.getStrategy().getStrategyId(), actualObject.getStrategy().getStrategyId());
		assertEquals(expectedObject.getStrategy().getStrategyName(), actualObject.getStrategy().getStrategyName());
		assertEquals(expectedObject.getStrategy().getStatus().getStatusId(), actualObject.getStrategy().getStatus().getStatusId());
		assertEquals(expectedObject.getStrategy().getStatus().getValidityPeriod(), actualObject.getStrategy().getStatus().getValidityPeriod());
		assertEquals(expectedObject.getStrategy().getTimeFrameStart(), actualObject.getStrategy().getTimeFrameStart());
		assertEquals(expectedObject.getStrategy().getTimeFrameEnd(), actualObject.getStrategy().getTimeFrameEnd());
		assertEquals(expectedObject.getStrategy().getEnvironment().getEnvironmentId(), actualObject.getStrategy().getEnvironment().getEnvironmentId());
		assertEquals(expectedObject.getStrategy().getEnvironment().getEnvironmentName(), actualObject.getStrategy().getEnvironment().getEnvironmentName());
		assertEquals(expectedObject.getStrategyItemName(), actualObject.getStrategyItemName());
		assertEquals(expectedObject.getDescription(), actualObject.getDescription());
	}
}
