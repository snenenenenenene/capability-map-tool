package com.bavostepbros.leap.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.CapabilityItem;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.Strategy;
import com.bavostepbros.leap.domain.model.StrategyItem;
import com.bavostepbros.leap.domain.model.capabilitylevel.CapabilityLevel;
import com.bavostepbros.leap.domain.model.dto.CapabilityItemDto;
import com.bavostepbros.leap.domain.model.strategicimportance.StrategicImportance;
import com.bavostepbros.leap.domain.service.capabilityitemservice.CapabilityItemService;
import com.bavostepbros.leap.persistence.CapabilityDAL;
import com.bavostepbros.leap.persistence.CapabilityItemDAL;
import com.bavostepbros.leap.persistence.EnvironmentDAL;
import com.bavostepbros.leap.persistence.StatusDAL;
import com.bavostepbros.leap.persistence.StrategyDAL;
import com.bavostepbros.leap.persistence.StrategyItemDAL;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class CapabilityItemControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private StatusDAL statusDAL;
	
	@Autowired
	private EnvironmentDAL environmentDAL;
	
	@Autowired
	private StrategyDAL strategyDAL;
	
	@Autowired
	private CapabilityDAL capabilityDAL;
	
	@Autowired
	private StrategyItemDAL strategyItemDAL;
	
	@Autowired
	private CapabilityItemDAL capabilityItemDAL;
	
	@Autowired
	private CapabilityItemService capabilityItemService;
	
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
	
	static final String PATH = "/api/capabilityitem/";
	
	@BeforeEach
	public void init() {
		statusFirst = statusDAL.save(new Status(1, LocalDate.of(2021, 05, 15)));
		statusSecond = statusDAL.save(new Status(2, LocalDate.of(2021, 05, 20)));
		environmentFirst = environmentDAL.save(new Environment(1, "Test 1"));
		environmentSecond = environmentDAL.save(new Environment(2, "Test 2"));
		capabilityFirst = capabilityDAL.save(new Capability(1, environmentFirst,
				statusFirst, 1, "Capability 1", CapabilityLevel.ONE, true, 
				"Target Operating Model", 1, 1, 1));
		capabilitySecond = capabilityDAL.save(new Capability(2, environmentFirst,
				statusFirst, capabilityFirst.getCapabilityId(), "Capability 2", 
				CapabilityLevel.TWO, true, "Target Operating Model", 1, 1, 1));
		strategyFirst = strategyDAL.save(new Strategy(1, statusFirst, "Strategy 1",
				LocalDate.of(2021, 05, 15), LocalDate.of(2021, 05, 20), 
				environmentFirst));
		strategySecond = strategyDAL.save(new Strategy(2, statusSecond, "Strategy 2",
				LocalDate.of(2021, 05, 15), LocalDate.of(2021, 05, 20), 
				environmentSecond));
		strategyItemFirst = strategyItemDAL.save(new StrategyItem(1, strategyFirst, 
				"StrategyItem 1", "Description 1"));
		strategyItemSecond = strategyItemDAL.save(new StrategyItem(2, strategySecond, 
				"StrategyItem 2", "Description 2"));
		capabilityItemFirst = capabilityItemDAL.save(new CapabilityItem(capabilityFirst, 
				strategyItemFirst, StrategicImportance.MEDIUM));
		capabilityItemSecond = capabilityItemDAL.save(new CapabilityItem(capabilitySecond, 
				strategyItemSecond, StrategicImportance.HIGHEST));
	}
	
	@AfterEach
	public void close() {
		statusDAL.delete(statusFirst);
		statusDAL.delete(statusSecond);
		environmentDAL.delete(environmentFirst);
		environmentDAL.delete(environmentSecond);
		capabilityDAL.delete(capabilityFirst);
		capabilityDAL.delete(capabilitySecond);
		strategyDAL.delete(strategyFirst);
		strategyDAL.delete(strategySecond);
		strategyItemDAL.delete(strategyItemFirst);
		strategyItemDAL.delete(strategyItemSecond);
		capabilityItemDAL.delete(capabilityItemFirst);
		capabilityItemDAL.delete(capabilityItemSecond);
	}
	
	@Test
	void should_notBeNull() {
		assertNotNull(statusDAL);
		assertNotNull(environmentDAL);
		assertNotNull(strategyDAL);
		assertNotNull(capabilityDAL);
		assertNotNull(strategyItemDAL);
		assertNotNull(capabilityItemDAL);
		assertNotNull(capabilityItemService);
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
	}
	
	@Test
	public void should_postCapabilityItem_whenSaveCapabilityItem() throws Exception {
		Integer capabilityId = capabilityFirst.getCapabilityId();
		Integer itemId = strategyItemSecond.getItemId();
		String strategicImportance = "LOW";
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("capabilityId", capabilityId.toString())
				.param("itemId", itemId.toString())
				.param("strategicImportance", strategicImportance)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		CapabilityItemDto resultCapabilityItem = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), CapabilityItemDto.class);
		
		CapabilityItem capabilityItem = capabilityItemService.get(capabilityId, itemId);
		
		assertNotNull(resultCapabilityItem);
		testCapabilityItem(capabilityItem, resultCapabilityItem);
	}
	
	@Test
	public void should_getCapabilityItem_whenGetCapabilityItemByCapabilityIdAndItemId() throws Exception {
		Integer capabilityId = capabilityItemFirst.getCapability().getCapabilityId();
		Integer itemId = capabilityItemFirst.getStrategyItem().getItemId();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH + capabilityId + "/" + itemId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		CapabilityItemDto resultCapabilityItem = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), CapabilityItemDto.class);
		
		assertNotNull(resultCapabilityItem);
		testCapabilityItem(capabilityItemFirst, resultCapabilityItem);
	}
	
	@Test
	public void should_putCapabilityItem_whenUpdateCapabilityItem() throws Exception {
		Integer capabilityId = capabilityFirst.getCapabilityId();
		Integer itemId = strategyItemFirst.getItemId();
		String strategicImportance = "HIGH";
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(PATH)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("capabilityId", capabilityId.toString())
				.param("itemId", itemId.toString())
				.param("strategicImportance", strategicImportance)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		CapabilityItemDto resultCapabilityItem = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), CapabilityItemDto.class);
		
		CapabilityItem capabilityItem = capabilityItemService.get(capabilityId, itemId);
		
		assertNotNull(resultCapabilityItem);
		testCapabilityItem(capabilityItem, resultCapabilityItem);
	}
	
	@Test
	public void should_deleteCapabilityItem_whenDeleteCapabilityItem() throws Exception {
		Integer capabilityId = capabilityItemFirst.getCapability().getCapabilityId();
		Integer itemId = capabilityItemFirst.getStrategyItem().getItemId();
		
		mockMvc.perform(MockMvcRequestBuilders.delete(PATH + capabilityId + "/" + itemId))
		.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void should_getCapabilityItems_whenGetCapabilityItemsByStrategyItemid() throws Exception {
		Integer itemId = strategyItemSecond.getItemId();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH + "all-capabilityitems-by-strategyitemid/" + itemId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<CapabilityItemDto> resultCapabilityItems = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<CapabilityItemDto>>() {});
		
		assertNotNull(resultCapabilityItems);
		assertEquals(1, resultCapabilityItems.size());
		testCapabilityItem(capabilityItemSecond, resultCapabilityItems.get(0));
	}
	
	@Test
	private void testCapabilityItem(CapabilityItem expectedObject, CapabilityItemDto actualObject) {
		assertEquals(expectedObject.getCapability().getCapabilityId(), actualObject.getCapability().getCapabilityId());
		assertEquals(expectedObject.getCapability().getEnvironment().getEnvironmentId(), actualObject.getCapability().getEnvironment().getEnvironmentId());
		assertEquals(expectedObject.getCapability().getEnvironment().getEnvironmentName(), actualObject.getCapability().getEnvironment().getEnvironmentName());
		assertEquals(expectedObject.getCapability().getStatus().getStatusId(), actualObject.getCapability().getStatus().getStatusId());
		assertEquals(expectedObject.getCapability().getStatus().getValidityPeriod(), actualObject.getCapability().getStatus().getValidityPeriod());
		assertEquals(expectedObject.getCapability().getParentCapabilityId(), actualObject.getCapability().getParentCapabilityId());
		assertEquals(expectedObject.getCapability().getCapabilityName(), actualObject.getCapability().getCapabilityName());
		assertEquals(expectedObject.getCapability().getLevel(), actualObject.getCapability().getLevel());
		assertEquals(expectedObject.getCapability().isPaceOfChange(), actualObject.getCapability().isPaceOfChange());
		assertEquals(expectedObject.getCapability().getTargetOperatingModel(), actualObject.getCapability().getTargetOperatingModel());
		assertEquals(expectedObject.getCapability().getResourceQuality(), actualObject.getCapability().getResourceQuality());
		assertEquals(expectedObject.getCapability().getInformationQuality(), actualObject.getCapability().getInformationQuality());
		assertEquals(expectedObject.getCapability().getApplicationFit(), actualObject.getCapability().getApplicationFit());
		assertEquals(expectedObject.getStrategyItem().getItemId(), actualObject.getStrategyItem().getItemId());
		assertEquals(expectedObject.getStrategyItem().getStrategy().getEnvironment().getEnvironmentId(), actualObject.getStrategyItem().getStrategy().getEnvironment().getEnvironmentId());
		assertEquals(expectedObject.getStrategyItem().getStrategy().getEnvironment().getEnvironmentName(), actualObject.getStrategyItem().getStrategy().getEnvironment().getEnvironmentName());
		assertEquals(expectedObject.getStrategyItem().getStrategy().getStatus().getStatusId(), actualObject.getStrategyItem().getStrategy().getStatus().getStatusId());
		assertEquals(expectedObject.getStrategyItem().getStrategy().getStatus().getValidityPeriod(), actualObject.getStrategyItem().getStrategy().getStatus().getValidityPeriod());
		assertEquals(expectedObject.getStrategyItem().getStrategy().getStrategyId(), actualObject.getStrategyItem().getStrategy().getStrategyId());
		assertEquals(expectedObject.getStrategyItem().getStrategy().getStrategyName(), actualObject.getStrategyItem().getStrategy().getStrategyName());
		assertEquals(expectedObject.getStrategyItem().getStrategy().getTimeFrameStart(), actualObject.getStrategyItem().getStrategy().getTimeFrameStart());
		assertEquals(expectedObject.getStrategyItem().getStrategy().getTimeFrameEnd(), actualObject.getStrategyItem().getStrategy().getTimeFrameEnd());
		assertEquals(expectedObject.getStrategyItem().getStrategyItemName(), actualObject.getStrategyItem().getStrategyItemName());
		assertEquals(expectedObject.getStrategyItem().getDescription(), actualObject.getStrategyItem().getDescription());
		assertEquals(expectedObject.getStrategicImportance(), actualObject.getStrategicImportance());
	}
}
