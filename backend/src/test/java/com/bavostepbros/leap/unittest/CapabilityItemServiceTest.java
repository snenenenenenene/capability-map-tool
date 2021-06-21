package com.bavostepbros.leap.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.CapabilityItem;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.Strategy;
import com.bavostepbros.leap.domain.model.StrategyItem;
import com.bavostepbros.leap.domain.model.paceofchange.PaceOfChange;
import com.bavostepbros.leap.domain.model.strategicimportance.StrategicImportance;
import com.bavostepbros.leap.domain.model.targetoperatingmodel.TargetOperatingModel;
import com.bavostepbros.leap.domain.service.capabilityitemservice.CapabilityItemService;
import com.bavostepbros.leap.persistence.CapabilityDAL;
import com.bavostepbros.leap.persistence.CapabilityItemDAL;
import com.bavostepbros.leap.persistence.StrategyItemDAL;

@AutoConfigureMockMvc
@SpringBootTest
public class CapabilityItemServiceTest {
	
	@Autowired
	private CapabilityItemService capabilityItemService;
	
	@MockBean
	private CapabilityItemDAL capabilityItemDAL;
	
	@MockBean
	private StrategyItemDAL strategyItemDAL;
	
	@MockBean
	private CapabilityDAL capabilityDAL;
	
	private Status statusFirst;
	private Status statusSecond;
	private Environment environmentFirst;
	private Environment environmentSecond;
	private Capability capabilityFirst;
	private Capability capabilitySecond;
	private Strategy strategyFirst;
	private Strategy strategySecond;
	private StrategyItem strategyItemFirst;
	private StrategyItem strategyItemSecond;
	private CapabilityItem capabilityItemFirst;
	private CapabilityItem capabilityItemSecond;
	private List<CapabilityItem> capabilityItems;
	private Optional<Capability> optionalCapabilityFirst;
	private Optional<StrategyItem> optionalStrategyItemFirst;
	private Optional<CapabilityItem> optionalCapabilityItemFirst;
	
	private Integer capabilityId;
	private Integer itemId;
	private String strategicImportance;
	
	@BeforeEach
	void init() {
		statusFirst = new Status(1, LocalDate.of(2021, 05, 15));
		statusSecond = new Status(2, LocalDate.of(2021, 10, 10));
		environmentFirst = new Environment(1, "Environment test");
		environmentSecond = new Environment(2, "Environment test");
		capabilityFirst = new Capability(1, environmentFirst, statusFirst, 0, "Capability 1", "Description 1",
				PaceOfChange.DIFFERENTIATION, TargetOperatingModel.COORDINATION, 10, 2.0, 3.0);
		capabilitySecond = new Capability(2, environmentFirst, statusSecond, capabilityFirst.getCapabilityId(),
				"Capability 2", "Description 2", PaceOfChange.DIFFERENTIATION, TargetOperatingModel.COORDINATION, 10, 2.0, 3.0);
		strategyFirst = new Strategy(1, statusFirst, "Strategy 1", LocalDate.of(2021, 05, 15),
				LocalDate.of(2021, 05, 20), environmentFirst);
		strategySecond = new Strategy(2, statusSecond, "Strategy 2", LocalDate.of(2021, 05, 15),
				LocalDate.of(2021, 05, 20), environmentSecond);
		strategyItemFirst = new StrategyItem(1, strategyFirst, "StrategyItem 1", "Description 1");
		strategyItemSecond = new StrategyItem(2, strategySecond, "StrategyItem 2", "Description 2");
		capabilityItemFirst = new CapabilityItem(capabilityFirst, strategyItemFirst, StrategicImportance.HIGHEST);
		capabilityItemSecond = new CapabilityItem(capabilitySecond, strategyItemSecond, StrategicImportance.MEDIUM);
		capabilityItems = List.of(capabilityItemFirst, capabilityItemSecond);
		optionalCapabilityFirst = Optional.of(capabilityFirst);
		optionalStrategyItemFirst = Optional.of(strategyItemFirst);
		optionalCapabilityItemFirst = Optional.of(capabilityItemFirst);
		
		capabilityId = capabilityItemFirst.getCapability().getCapabilityId();
		itemId = capabilityItemFirst.getStrategyItem().getItemId();
		strategicImportance = capabilityItemFirst.getStrategicImportance().toString();
	}
	
	@Test
	void shouldNotBeNull() {
		assertNotNull(capabilityItemService);
		assertNotNull(capabilityItemDAL);
		assertNotNull(capabilityDAL);
		assertNotNull(strategyItemDAL);
		
		assertNotNull(statusFirst);
		assertNotNull(statusSecond);
		assertNotNull(environmentFirst);
		assertNotNull(environmentSecond);
		assertNotNull(capabilityFirst);
		assertNotNull(capabilitySecond);
		assertNotNull(strategyFirst);
		assertNotNull(strategySecond);
		assertNotNull(strategyItemFirst);
		assertNotNull(strategyItemSecond);
		assertNotNull(capabilityItemFirst);
		assertNotNull(capabilityItemSecond);
		assertNotNull(capabilityItems);
		assertNotNull(optionalCapabilityFirst);
		assertNotNull(optionalStrategyItemFirst);
		assertNotNull(optionalCapabilityItemFirst);
		
		assertNotNull(capabilityId);
		assertNotNull(itemId);
		assertNotNull(strategicImportance);
	}
	
	@Test
	void should_returnCapabilityItem_whenSaveCapabilityItem() {
		BDDMockito.given(capabilityDAL.findById(BDDMockito.anyInt())).willReturn(optionalCapabilityFirst);
		BDDMockito.given(strategyItemDAL.findById(BDDMockito.anyInt())).willReturn(optionalStrategyItemFirst);
		BDDMockito.given(capabilityItemDAL.save(BDDMockito.any(CapabilityItem.class)))
				.willReturn(capabilityItemFirst);

		CapabilityItem capabilityItem = capabilityItemService.save(capabilityId, itemId, strategicImportance);
		
		assertNotNull(capabilityItem);
		assertTrue(capabilityItem instanceof CapabilityItem);
		testCapabilityItem(capabilityItemFirst, capabilityItem);
	}
	
	@Test 
	void should_throwNullPointerException_whenGetCapabilityItemByIdInvalidId() {
		BDDMockito.given(capabilityDAL.findById(BDDMockito.anyInt())).willReturn(optionalCapabilityFirst);
		BDDMockito.given(strategyItemDAL.findById(BDDMockito.anyInt())).willReturn(optionalStrategyItemFirst);
		String expectedErrorMessage = "CapabilityItem does not exist.";
		
		Exception exception = assertThrows(NullPointerException.class, 
				() -> capabilityItemService.get(capabilityId, itemId));
		
		assertEquals(expectedErrorMessage, exception.getMessage());
	}
	
	@Test
	void should_returnCapabilityItem_whenGetCapabilityItem() {
		BDDMockito.given(capabilityDAL.findById(BDDMockito.anyInt())).willReturn(optionalCapabilityFirst);
		BDDMockito.given(strategyItemDAL.findById(BDDMockito.anyInt())).willReturn(optionalStrategyItemFirst);
		BDDMockito.given(capabilityItemDAL.findByCapabilityAndStrategyItem(BDDMockito.any(Capability.class), BDDMockito.any(StrategyItem.class)))
				.willReturn(optionalCapabilityItemFirst);

		CapabilityItem capabilityItem = capabilityItemService.get(capabilityId, itemId);
		
		assertNotNull(capabilityItem);
		assertTrue(capabilityItem instanceof CapabilityItem);
		testCapabilityItem(capabilityItemFirst, capabilityItem);
	}
	
	@Test
	void should_returnCapabilityItem_whenUpdateCapabilityItem() {
		BDDMockito.given(capabilityDAL.findById(BDDMockito.anyInt())).willReturn(optionalCapabilityFirst);
		BDDMockito.given(strategyItemDAL.findById(BDDMockito.anyInt())).willReturn(optionalStrategyItemFirst);
		BDDMockito.given(capabilityItemDAL.save(BDDMockito.any(CapabilityItem.class)))
				.willReturn(capabilityItemFirst);

		CapabilityItem capabilityItem = capabilityItemService.update(capabilityId, itemId, strategicImportance);
		
		assertNotNull(capabilityItem);
		assertTrue(capabilityItem instanceof CapabilityItem);
		testCapabilityItem(capabilityItemFirst, capabilityItem);
	}
	
	@Test 
	void should_verifyDeleted_whenDeleteCapabilityItem() {
		BDDMockito.given(capabilityDAL.findById(BDDMockito.anyInt())).willReturn(optionalCapabilityFirst);
		BDDMockito.given(strategyItemDAL.findById(BDDMockito.anyInt())).willReturn(optionalStrategyItemFirst);
		
		capabilityItemService.delete(capabilityId, itemId);
		
		Mockito.verify(capabilityItemDAL, Mockito.times(1)).deleteByCapabilityAndStrategyItem(
				BDDMockito.any(Capability.class), BDDMockito.any(StrategyItem.class));
	}
	
	@Test
	void should_returnCapabilityItems_whenGetCapabilityItemByCapability() {
		BDDMockito.given(capabilityDAL.findById(BDDMockito.anyInt())).willReturn(optionalCapabilityFirst);
		BDDMockito.given(capabilityItemDAL.findByCapability(BDDMockito.any(Capability.class)))
				.willReturn(capabilityItems);

		List<CapabilityItem> capabilityItemsResult = capabilityItemService.getCapabilityItemsByCapability(capabilityId);
		
		assertNotNull(capabilityItemsResult);
		assertEquals(capabilityItems.size(), capabilityItemsResult.size());
		testCapabilityItem(capabilityItemFirst, capabilityItemsResult.get(0));
		testCapabilityItem(capabilityItemSecond, capabilityItemsResult.get(1));
	}
	
	@Test
	void should_returnCapabilityItems_whenGetCapabilityItemByStrategyItem() {
		BDDMockito.given(strategyItemDAL.findById(BDDMockito.anyInt())).willReturn(optionalStrategyItemFirst);
		BDDMockito.given(capabilityItemDAL.findByStrategyItem(BDDMockito.any(StrategyItem.class)))
				.willReturn(capabilityItems);

		List<CapabilityItem> capabilityItemsResult = capabilityItemService.getCapabilityItemsByStrategyItem(itemId);
		
		assertNotNull(capabilityItemsResult);
		assertEquals(capabilityItems.size(), capabilityItemsResult.size());
		testCapabilityItem(capabilityItemFirst, capabilityItemsResult.get(0));
		testCapabilityItem(capabilityItemSecond, capabilityItemsResult.get(1));
	}
	
	private void testCapabilityItem(CapabilityItem expectedObject, CapabilityItem actualObject) {
		assertEquals(expectedObject.getCapability().getCapabilityId(), actualObject.getCapability().getCapabilityId());
		assertEquals(expectedObject.getStrategyItem().getItemId(), actualObject.getStrategyItem().getItemId());
		assertEquals(expectedObject.getStrategicImportance(), actualObject.getStrategicImportance());
	}
}
