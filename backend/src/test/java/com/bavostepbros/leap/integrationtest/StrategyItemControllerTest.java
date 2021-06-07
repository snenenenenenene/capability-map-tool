package com.bavostepbros.leap.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.Strategy;
import com.bavostepbros.leap.domain.model.StrategyItem;
import com.bavostepbros.leap.domain.model.dto.StrategyItemDto;
import com.bavostepbros.leap.domain.service.strategyitemservice.StrategyItemService;
import com.bavostepbros.leap.integrationtest.testconfiguration.RequestFactory;
import com.bavostepbros.leap.persistence.EnvironmentDAL;
import com.bavostepbros.leap.persistence.StatusDAL;
import com.bavostepbros.leap.persistence.StrategyDAL;
import com.bavostepbros.leap.persistence.StrategyItemDAL;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StrategyItemControllerTest extends ApiIntegrationTest {
	
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
	private StrategyItemDAL strategyItemDAL;
	
	@Autowired
	private StrategyItemService strategyItemService;

	@Autowired
	private RequestFactory requestFactory;

	private String jwt;

	private Status statusFirst;
	private Status statusSecond;
	private Environment environmentFirst;
	private Environment environmentSecond;
	private Strategy strategyFirst;
	private Strategy strategySecond;
	private StrategyItem strategyItemFirst;
	private StrategyItem strategyItemSecond;
	private StrategyItem strategyItemThirth;
	
	static final String PATH = "/api/strategyitem/";

	@BeforeAll
	public void authenticate() throws Exception { super.authenticate(); }

	@BeforeEach
	public void init() {
		statusFirst = statusDAL.save(new Status(1, LocalDate.of(2021, 05, 15)));
		statusSecond = statusDAL.save(new Status(2, LocalDate.of(2021, 05, 20)));
		environmentFirst = environmentDAL.save(new Environment(1, "Test 1"));
		environmentSecond = environmentDAL.save(new Environment(2, "Test 2"));
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
		strategyItemThirth = strategyItemDAL.save(new StrategyItem(3, strategySecond, 
				"StrategyItem 3", "Description 3"));
	}
	
	@AfterEach
	public void close() {
		statusDAL.delete(statusFirst);
		statusDAL.delete(statusSecond);
		environmentDAL.delete(environmentFirst);
		environmentDAL.delete(environmentSecond);
		strategyDAL.delete(strategyFirst);
		strategyDAL.delete(strategySecond);
		strategyItemDAL.delete(strategyItemFirst);
		strategyItemDAL.delete(strategyItemSecond);
		strategyItemDAL.delete(strategyItemThirth);
	}
	
	@Test
	void should_notBeNull() {
		assertNotNull(statusDAL);
		assertNotNull(environmentDAL);
		assertNotNull(strategyDAL);
		assertNotNull(strategyItemDAL);
		assertNotNull(strategyItemService);
		assertNotNull(statusFirst);
		assertNotNull(statusSecond);
		assertNotNull(environmentFirst);
		assertNotNull(environmentSecond);
		assertNotNull(strategyFirst);
		assertNotNull(strategySecond);
		assertNotNull(strategyItemFirst);
		assertNotNull(strategyItemSecond);
		assertNotNull(strategyItemThirth);
	}
	
	@Test
	public void should_postStrategyItem_whenSaveStrategyItem() throws Exception {
		Integer strategyId = strategyFirst.getStrategyId();
		String strategyItemName = "StrategyItem post test";
		String strategyItemDescription = "StrategyItem post test description";

		MvcResult mvcResult = mockMvc.perform(requestFactory.post(PATH, jwt)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("strategyId", strategyId.toString())
				.param("strategyItemName", strategyItemName)
				.param("strategyItemDescription", strategyItemDescription)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		StrategyItemDto resultStrategyItem = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), StrategyItemDto.class);
		
		StrategyItem strategyItem = strategyItemService.getStrategyItemByStrategyItemName(strategyItemName);
		
		assertNotNull(resultStrategyItem);
		testStrategyItem(strategyItem, resultStrategyItem);
	}
	
	@Test
	public void should_getStrategyItem_whenGetStrategyItemById() throws Exception {
		Integer strategyItemId = strategyItemFirst.getItemId();

		MvcResult mvcResult = mockMvc.perform(requestFactory.get(PATH + strategyItemId, jwt))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		StrategyItemDto resultStrategyItem = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), StrategyItemDto.class);
		
		assertNotNull(resultStrategyItem);
		testStrategyItem(strategyItemFirst, resultStrategyItem);
	}
	
	@Test
	public void should_putStrategyItem_whenUpdateStrategyItem() throws Exception {
		Integer strategyItemId = strategyItemFirst.getItemId();
		Integer strategyId = strategyItemFirst.getStrategy().getStrategyId();
		String strategyItemName = "StrategyItem update test";
		String strategyItemDescription = strategyItemFirst.getDescription();

		MvcResult mvcResult = mockMvc.perform(requestFactory.put(PATH + strategyItemId, jwt)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("strategyId", strategyId.toString())
				.param("strategyItemName", strategyItemName)
				.param("description", strategyItemDescription)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		StrategyItemDto resultStrategyItem = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), StrategyItemDto.class);
		
		StrategyItem strategyItem = strategyItemService.getStrategyItemByStrategyItemName(strategyItemName);
		
		assertNotNull(resultStrategyItem);
		testStrategyItem(strategyItem, resultStrategyItem);
	}
	
	@Test
	public void should_deleteStrategyItem_whenDeleteStrategyItem() throws Exception {
		Integer itemId = strategyItemFirst.getItemId();

		mockMvc.perform(requestFactory.delete(PATH + itemId, jwt))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void should_getStrategyItems_whenGetAllStrategyItems() throws Exception {
		MvcResult mvcResult = mockMvc.perform(requestFactory.get(PATH, jwt))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<StrategyItemDto> resultStrategyItems = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<StrategyItemDto>>() {});
		
		assertNotNull(resultStrategyItems);
		assertEquals(3, resultStrategyItems.size());
		testStrategyItem(strategyItemFirst, resultStrategyItems.get(0));
		testStrategyItem(strategyItemSecond, resultStrategyItems.get(1));
		testStrategyItem(strategyItemThirth, resultStrategyItems.get(2));
	}
	
	@Test
	public void should_getBoolean_whenStrategyItemIdExists() throws Exception {
		Integer itemId = strategyItemFirst.getItemId();
		
		mockMvc.perform(requestFactory.get(PATH + "exists-by-id/" + itemId, jwt))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("true"));	
	}
	
	@Test
	public void should_getBoolean_whenStrategyItemNameExists() throws Exception {
		String strategyItemName = strategyItemFirst.getStrategyItemName();
		
		mockMvc.perform(requestFactory.get(PATH + "exists-by-strategyitemname/" + strategyItemName, jwt))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("true"));	
	}
	
	@Test
	public void should_getStrategyItem_whenGetStrategyItemByStrategyitemname() throws Exception {
		String strategyItemName = strategyItemFirst.getStrategyItemName();
		
		MvcResult mvcResult = mockMvc.perform(requestFactory.get(PATH + "strategyitemname/" + strategyItemName, jwt))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		StrategyItemDto resultStrategyItem = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), StrategyItemDto.class);
		
		assertNotNull(resultStrategyItem);
		testStrategyItem(strategyItemFirst, resultStrategyItem);
	}
	
	@Test
	public void should_getStrategyItems_whenGetStrategyItemsByStrategyid() throws Exception {
		Integer strategyId = strategyItemSecond.getStrategy().getStrategyId();
		
		MvcResult mvcResult = mockMvc.perform(requestFactory.get(PATH + "all-strategyitems-by-strategyid/" + strategyId, jwt))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<StrategyItemDto> resultStrategyItems = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<StrategyItemDto>>() {});
		
		assertNotNull(resultStrategyItems);
		assertEquals(2, resultStrategyItems.size());
		testStrategyItem(strategyItemSecond, resultStrategyItems.get(0));
		testStrategyItem(strategyItemThirth, resultStrategyItems.get(1));
	}
	
	@Test
	private void testStrategyItem(StrategyItem expectedObject, StrategyItemDto actualObject) {
		assertEquals(expectedObject.getStrategy().getStrategyId(), actualObject.getStrategy().getStrategyId());
		assertEquals(expectedObject.getStrategy().getEnvironment().getEnvironmentId(), actualObject.getStrategy().getEnvironment().getEnvironmentId());
		assertEquals(expectedObject.getStrategy().getEnvironment().getEnvironmentName(), actualObject.getStrategy().getEnvironment().getEnvironmentName());
		assertEquals(expectedObject.getStrategy().getStatus().getStatusId(), actualObject.getStrategy().getStatus().getStatusId());
		assertEquals(expectedObject.getStrategy().getStatus().getValidityPeriod(), actualObject.getStrategy().getStatus().getValidityPeriod());
		assertEquals(expectedObject.getStrategy().getStrategyName(), actualObject.getStrategy().getStrategyName());
		assertEquals(expectedObject.getStrategy().getTimeFrameStart(), actualObject.getStrategy().getTimeFrameStart());
		assertEquals(expectedObject.getStrategy().getTimeFrameEnd(), actualObject.getStrategy().getTimeFrameEnd());
		assertEquals(expectedObject.getItemId(), actualObject.getItemId());
		assertEquals(expectedObject.getStrategyItemName(), actualObject.getStrategyItemName());
		assertEquals(expectedObject.getDescription(), actualObject.getDescription());
	}
}
