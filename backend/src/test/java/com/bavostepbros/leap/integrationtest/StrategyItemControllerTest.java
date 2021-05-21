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

import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.Strategy;
import com.bavostepbros.leap.domain.model.StrategyItem;
import com.bavostepbros.leap.domain.model.dto.StrategyItemDto;
import com.bavostepbros.leap.domain.service.strategyitemservice.StrategyItemService;
import com.bavostepbros.leap.persistence.EnvironmentDAL;
import com.bavostepbros.leap.persistence.StatusDAL;
import com.bavostepbros.leap.persistence.StrategyDAL;
import com.bavostepbros.leap.persistence.StrategyItemDAL;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class StrategyItemControllerTest {
	
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
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
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
		assertEquals(strategyItem.getStrategy().getStrategyId(), resultStrategyItem.getStrategy().getStrategyId());
		assertEquals(strategyItem.getStrategy().getEnvironment().getEnvironmentId(), resultStrategyItem.getStrategy().getEnvironment().getEnvironmentId());
		assertEquals(strategyItem.getStrategy().getEnvironment().getEnvironmentName(), resultStrategyItem.getStrategy().getEnvironment().getEnvironmentName());
		assertEquals(strategyItem.getStrategy().getStatus().getStatusId(), resultStrategyItem.getStrategy().getStatus().getStatusId());
		assertEquals(strategyItem.getStrategy().getStatus().getValidityPeriod(), resultStrategyItem.getStrategy().getStatus().getValidityPeriod());
		assertEquals(strategyItem.getStrategy().getStrategyName(), resultStrategyItem.getStrategy().getStrategyName());
		assertEquals(strategyItem.getStrategy().getTimeFrameStart(), resultStrategyItem.getStrategy().getTimeFrameStart());
		assertEquals(strategyItem.getStrategy().getTimeFrameEnd(), resultStrategyItem.getStrategy().getTimeFrameEnd());
		assertEquals(strategyItem.getItemId(), resultStrategyItem.getItemId());
		assertEquals(strategyItem.getStrategyItemName(), resultStrategyItem.getStrategyItemName());
		assertEquals(strategyItem.getDescription(), resultStrategyItem.getDescription());
	}
	
	@Test
	public void should_getStrategyItem_whenGetStrategyItemById() throws Exception {
		Integer strategyItemId = strategyItemFirst.getItemId();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH + strategyItemId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		StrategyItemDto resultStrategyItem = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), StrategyItemDto.class);
		
		assertNotNull(resultStrategyItem);
		assertEquals(strategyItemFirst.getStrategy().getStrategyId(), resultStrategyItem.getStrategy().getStrategyId());
		assertEquals(strategyItemFirst.getStrategy().getEnvironment().getEnvironmentId(), resultStrategyItem.getStrategy().getEnvironment().getEnvironmentId());
		assertEquals(strategyItemFirst.getStrategy().getEnvironment().getEnvironmentName(), resultStrategyItem.getStrategy().getEnvironment().getEnvironmentName());
		assertEquals(strategyItemFirst.getStrategy().getStatus().getStatusId(), resultStrategyItem.getStrategy().getStatus().getStatusId());
		assertEquals(strategyItemFirst.getStrategy().getStatus().getValidityPeriod(), resultStrategyItem.getStrategy().getStatus().getValidityPeriod());
		assertEquals(strategyItemFirst.getStrategy().getStrategyName(), resultStrategyItem.getStrategy().getStrategyName());
		assertEquals(strategyItemFirst.getStrategy().getTimeFrameStart(), resultStrategyItem.getStrategy().getTimeFrameStart());
		assertEquals(strategyItemFirst.getStrategy().getTimeFrameEnd(), resultStrategyItem.getStrategy().getTimeFrameEnd());
		assertEquals(strategyItemFirst.getItemId(), resultStrategyItem.getItemId());
		assertEquals(strategyItemFirst.getStrategyItemName(), resultStrategyItem.getStrategyItemName());
		assertEquals(strategyItemFirst.getDescription(), resultStrategyItem.getDescription());
	}
	
	@Test
	public void should_putStrategyItem_whenUpdateStrategyItem() throws Exception {
		Integer strategyItemId = strategyItemFirst.getItemId();
		Integer strategyId = strategyItemFirst.getStrategy().getStrategyId();
		String strategyItemName = "StrategyItem update test";
		String strategyItemDescription = strategyItemFirst.getDescription();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(PATH + strategyItemId)
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
		assertEquals(strategyItem.getStrategy().getStrategyId(), resultStrategyItem.getStrategy().getStrategyId());
		assertEquals(strategyItem.getStrategy().getEnvironment().getEnvironmentId(), resultStrategyItem.getStrategy().getEnvironment().getEnvironmentId());
		assertEquals(strategyItem.getStrategy().getEnvironment().getEnvironmentName(), resultStrategyItem.getStrategy().getEnvironment().getEnvironmentName());
		assertEquals(strategyItem.getStrategy().getStatus().getStatusId(), resultStrategyItem.getStrategy().getStatus().getStatusId());
		assertEquals(strategyItem.getStrategy().getStatus().getValidityPeriod(), resultStrategyItem.getStrategy().getStatus().getValidityPeriod());
		assertEquals(strategyItem.getStrategy().getStrategyName(), resultStrategyItem.getStrategy().getStrategyName());
		assertEquals(strategyItem.getStrategy().getTimeFrameStart(), resultStrategyItem.getStrategy().getTimeFrameStart());
		assertEquals(strategyItem.getStrategy().getTimeFrameEnd(), resultStrategyItem.getStrategy().getTimeFrameEnd());
		assertEquals(strategyItem.getItemId(), resultStrategyItem.getItemId());
		assertEquals(strategyItem.getStrategyItemName(), resultStrategyItem.getStrategyItemName());
		assertEquals(strategyItem.getDescription(), resultStrategyItem.getDescription());
	}
	
	@Test
	public void should_deleteStrategyItem_whenDeleteStrategyItem() throws Exception {
		Integer itemId = strategyItemFirst.getItemId();
		
		mockMvc.perform(MockMvcRequestBuilders.delete(PATH + itemId))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void should_getStrategyItems_whenGetAllStrategyItems() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<StrategyItemDto> resultStrategyItems = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<StrategyItemDto>>() {});
		
		assertNotNull(resultStrategyItems);
		assertEquals(3, resultStrategyItems.size());
		assertEquals(strategyItemFirst.getStrategy().getStrategyId(), resultStrategyItems.get(0).getStrategy().getStrategyId());
		assertEquals(strategyItemFirst.getStrategy().getEnvironment().getEnvironmentId(), resultStrategyItems.get(0).getStrategy().getEnvironment().getEnvironmentId());
		assertEquals(strategyItemFirst.getStrategy().getEnvironment().getEnvironmentName(), resultStrategyItems.get(0).getStrategy().getEnvironment().getEnvironmentName());
		assertEquals(strategyItemFirst.getStrategy().getStatus().getStatusId(), resultStrategyItems.get(0).getStrategy().getStatus().getStatusId());
		assertEquals(strategyItemFirst.getStrategy().getStatus().getValidityPeriod(), resultStrategyItems.get(0).getStrategy().getStatus().getValidityPeriod());
		assertEquals(strategyItemFirst.getStrategy().getStrategyName(), resultStrategyItems.get(0).getStrategy().getStrategyName());
		assertEquals(strategyItemFirst.getStrategy().getTimeFrameStart(), resultStrategyItems.get(0).getStrategy().getTimeFrameStart());
		assertEquals(strategyItemFirst.getStrategy().getTimeFrameEnd(), resultStrategyItems.get(0).getStrategy().getTimeFrameEnd());
		assertEquals(strategyItemFirst.getItemId(), resultStrategyItems.get(0).getItemId());
		assertEquals(strategyItemFirst.getStrategyItemName(), resultStrategyItems.get(0).getStrategyItemName());
		assertEquals(strategyItemFirst.getDescription(), resultStrategyItems.get(0).getDescription());
		
		assertEquals(strategyItemSecond.getStrategy().getStrategyId(), resultStrategyItems.get(1).getStrategy().getStrategyId());
		assertEquals(strategyItemSecond.getStrategy().getEnvironment().getEnvironmentId(), resultStrategyItems.get(1).getStrategy().getEnvironment().getEnvironmentId());
		assertEquals(strategyItemSecond.getStrategy().getEnvironment().getEnvironmentName(), resultStrategyItems.get(1).getStrategy().getEnvironment().getEnvironmentName());
		assertEquals(strategyItemSecond.getStrategy().getStatus().getStatusId(), resultStrategyItems.get(1).getStrategy().getStatus().getStatusId());
		assertEquals(strategyItemSecond.getStrategy().getStatus().getValidityPeriod(), resultStrategyItems.get(1).getStrategy().getStatus().getValidityPeriod());
		assertEquals(strategyItemSecond.getStrategy().getStrategyName(), resultStrategyItems.get(1).getStrategy().getStrategyName());
		assertEquals(strategyItemSecond.getStrategy().getTimeFrameStart(), resultStrategyItems.get(1).getStrategy().getTimeFrameStart());
		assertEquals(strategyItemSecond.getStrategy().getTimeFrameEnd(), resultStrategyItems.get(1).getStrategy().getTimeFrameEnd());
		assertEquals(strategyItemSecond.getItemId(), resultStrategyItems.get(1).getItemId());
		assertEquals(strategyItemSecond.getStrategyItemName(), resultStrategyItems.get(1).getStrategyItemName());
		assertEquals(strategyItemSecond.getDescription(), resultStrategyItems.get(1).getDescription());
		
		assertEquals(strategyItemThirth.getStrategy().getStrategyId(), resultStrategyItems.get(2).getStrategy().getStrategyId());
		assertEquals(strategyItemThirth.getStrategy().getEnvironment().getEnvironmentId(), resultStrategyItems.get(2).getStrategy().getEnvironment().getEnvironmentId());
		assertEquals(strategyItemThirth.getStrategy().getEnvironment().getEnvironmentName(), resultStrategyItems.get(2).getStrategy().getEnvironment().getEnvironmentName());
		assertEquals(strategyItemThirth.getStrategy().getStatus().getStatusId(), resultStrategyItems.get(2).getStrategy().getStatus().getStatusId());
		assertEquals(strategyItemThirth.getStrategy().getStatus().getValidityPeriod(), resultStrategyItems.get(2).getStrategy().getStatus().getValidityPeriod());
		assertEquals(strategyItemThirth.getStrategy().getStrategyName(), resultStrategyItems.get(2).getStrategy().getStrategyName());
		assertEquals(strategyItemThirth.getStrategy().getTimeFrameStart(), resultStrategyItems.get(2).getStrategy().getTimeFrameStart());
		assertEquals(strategyItemThirth.getStrategy().getTimeFrameEnd(), resultStrategyItems.get(2).getStrategy().getTimeFrameEnd());
		assertEquals(strategyItemThirth.getItemId(), resultStrategyItems.get(2).getItemId());
		assertEquals(strategyItemThirth.getStrategyItemName(), resultStrategyItems.get(2).getStrategyItemName());
		assertEquals(strategyItemThirth.getDescription(), resultStrategyItems.get(2).getDescription());
	}
	
	@Test
	public void should_getBoolean_whenStrategyItemIdExists() throws Exception {
		Integer itemId = strategyItemFirst.getItemId();
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "exists-by-id/" + itemId))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("true"));	
	}
	
	@Test
	public void should_getBoolean_whenStrategyItemNameExists() throws Exception {
		String strategyItemName = strategyItemFirst.getStrategyItemName();
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "exists-by-strategyitemname/" + strategyItemName))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("true"));	
	}
	
	@Test
	public void should_getStrategyItem_whenGetStrategyItemByStrategyitemname() throws Exception {
		String strategyItemName = strategyItemFirst.getStrategyItemName();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH + "strategyitemname/" + strategyItemName))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		StrategyItemDto resultStrategyItem = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), StrategyItemDto.class);
		
		assertNotNull(resultStrategyItem);
		assertEquals(strategyItemFirst.getStrategy().getStrategyId(), resultStrategyItem.getStrategy().getStrategyId());
		assertEquals(strategyItemFirst.getStrategy().getEnvironment().getEnvironmentId(), resultStrategyItem.getStrategy().getEnvironment().getEnvironmentId());
		assertEquals(strategyItemFirst.getStrategy().getEnvironment().getEnvironmentName(), resultStrategyItem.getStrategy().getEnvironment().getEnvironmentName());
		assertEquals(strategyItemFirst.getStrategy().getStatus().getStatusId(), resultStrategyItem.getStrategy().getStatus().getStatusId());
		assertEquals(strategyItemFirst.getStrategy().getStatus().getValidityPeriod(), resultStrategyItem.getStrategy().getStatus().getValidityPeriod());
		assertEquals(strategyItemFirst.getStrategy().getStrategyName(), resultStrategyItem.getStrategy().getStrategyName());
		assertEquals(strategyItemFirst.getStrategy().getTimeFrameStart(), resultStrategyItem.getStrategy().getTimeFrameStart());
		assertEquals(strategyItemFirst.getStrategy().getTimeFrameEnd(), resultStrategyItem.getStrategy().getTimeFrameEnd());
		assertEquals(strategyItemFirst.getItemId(), resultStrategyItem.getItemId());
		assertEquals(strategyItemFirst.getStrategyItemName(), resultStrategyItem.getStrategyItemName());
		assertEquals(strategyItemFirst.getDescription(), resultStrategyItem.getDescription());
	}
	
	@Test
	public void should_getStrategyItems_whenGetStrategyItemsByStrategyid() throws Exception {
		Integer strategyId = strategyItemSecond.getStrategy().getStrategyId();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH + "all-strategyitems-by-strategyid/" + strategyId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<StrategyItemDto> resultStrategyItems = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<StrategyItemDto>>() {});
		
		assertNotNull(resultStrategyItems);
		assertEquals(2, resultStrategyItems.size());
		assertEquals(strategyItemSecond.getStrategy().getStrategyId(), resultStrategyItems.get(0).getStrategy().getStrategyId());
		assertEquals(strategyItemSecond.getStrategy().getEnvironment().getEnvironmentId(), resultStrategyItems.get(0).getStrategy().getEnvironment().getEnvironmentId());
		assertEquals(strategyItemSecond.getStrategy().getEnvironment().getEnvironmentName(), resultStrategyItems.get(0).getStrategy().getEnvironment().getEnvironmentName());
		assertEquals(strategyItemSecond.getStrategy().getStatus().getStatusId(), resultStrategyItems.get(0).getStrategy().getStatus().getStatusId());
		assertEquals(strategyItemSecond.getStrategy().getStatus().getValidityPeriod(), resultStrategyItems.get(0).getStrategy().getStatus().getValidityPeriod());
		assertEquals(strategyItemSecond.getStrategy().getStrategyName(), resultStrategyItems.get(0).getStrategy().getStrategyName());
		assertEquals(strategyItemSecond.getStrategy().getTimeFrameStart(), resultStrategyItems.get(0).getStrategy().getTimeFrameStart());
		assertEquals(strategyItemSecond.getStrategy().getTimeFrameEnd(), resultStrategyItems.get(0).getStrategy().getTimeFrameEnd());
		assertEquals(strategyItemSecond.getItemId(), resultStrategyItems.get(0).getItemId());
		assertEquals(strategyItemSecond.getStrategyItemName(), resultStrategyItems.get(0).getStrategyItemName());
		assertEquals(strategyItemSecond.getDescription(), resultStrategyItems.get(0).getDescription());
		
		assertEquals(strategyItemThirth.getStrategy().getStrategyId(), resultStrategyItems.get(1).getStrategy().getStrategyId());
		assertEquals(strategyItemThirth.getStrategy().getEnvironment().getEnvironmentId(), resultStrategyItems.get(1).getStrategy().getEnvironment().getEnvironmentId());
		assertEquals(strategyItemThirth.getStrategy().getEnvironment().getEnvironmentName(), resultStrategyItems.get(1).getStrategy().getEnvironment().getEnvironmentName());
		assertEquals(strategyItemThirth.getStrategy().getStatus().getStatusId(), resultStrategyItems.get(1).getStrategy().getStatus().getStatusId());
		assertEquals(strategyItemThirth.getStrategy().getStatus().getValidityPeriod(), resultStrategyItems.get(1).getStrategy().getStatus().getValidityPeriod());
		assertEquals(strategyItemThirth.getStrategy().getStrategyName(), resultStrategyItems.get(1).getStrategy().getStrategyName());
		assertEquals(strategyItemThirth.getStrategy().getTimeFrameStart(), resultStrategyItems.get(1).getStrategy().getTimeFrameStart());
		assertEquals(strategyItemThirth.getStrategy().getTimeFrameEnd(), resultStrategyItems.get(1).getStrategy().getTimeFrameEnd());
		assertEquals(strategyItemThirth.getItemId(), resultStrategyItems.get(1).getItemId());
		assertEquals(strategyItemThirth.getStrategyItemName(), resultStrategyItems.get(1).getStrategyItemName());
		assertEquals(strategyItemThirth.getDescription(), resultStrategyItems.get(1).getDescription());
	}
}
