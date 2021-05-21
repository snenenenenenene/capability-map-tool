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
import com.bavostepbros.leap.domain.model.dto.StrategyDto;
import com.bavostepbros.leap.domain.service.strategyservice.StrategyService;
import com.bavostepbros.leap.persistence.EnvironmentDAL;
import com.bavostepbros.leap.persistence.StatusDAL;
import com.bavostepbros.leap.persistence.StrategyDAL;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class StrategyControllerTest {
	
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
	private StrategyService strategyService;
	
	private Status statusFirst;
	private Status statusSecond;
	private Environment environmentFirst;
	private Environment environmentSecond;
	private Strategy strategyFirst;
	private Strategy strategySecond;
	
	static final String PATH = "/api/strategy/";
	
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
	}
	
	@AfterEach
	public void close() {
		statusDAL.delete(statusFirst);
		statusDAL.delete(statusSecond);
		environmentDAL.delete(environmentFirst);
		environmentDAL.delete(environmentSecond);
		strategyDAL.delete(strategyFirst);
		strategyDAL.delete(strategySecond);
	}
	
	@Test
	void should_notBeNull() {
		assertNotNull(statusDAL);
		assertNotNull(environmentDAL);
		assertNotNull(strategyDAL);
		assertNotNull(strategyService);
		assertNotNull(statusFirst);
		assertNotNull(statusSecond);
		assertNotNull(environmentFirst);
		assertNotNull(environmentSecond);
		assertNotNull(strategyFirst);
		assertNotNull(strategySecond);
	}
	
	@Test
	public void should_postStrategy_whenSaveStrategy() throws Exception {
		Integer statusId = strategyFirst.getStatus().getStatusId();
		String strategyName = "Strategy post test";
		LocalDate timeFrameStart = strategyFirst.getTimeFrameStart();
		LocalDate timeFrameEnd = strategyFirst.getTimeFrameEnd();
		Integer environmentId = strategyFirst.getEnvironment().getEnvironmentId();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("statusId", statusId.toString())
				.param("strategyName", strategyName)
				.param("timeFrameStart", timeFrameStart.toString())
				.param("timeFrameEnd", timeFrameEnd.toString())
				.param("environmentId", environmentId.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		StrategyDto resultStrategy = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), StrategyDto.class);
		
		Strategy strategy = strategyService.getStrategyByStrategyname(strategyName);
		
		assertNotNull(resultStrategy);
		assertEquals(strategy.getStrategyId(), resultStrategy.getStrategyId());
		assertEquals(strategy.getStatus().getStatusId(), resultStrategy.getStatus().getStatusId());
		assertEquals(strategy.getStatus().getValidityPeriod(), resultStrategy.getStatus().getValidityPeriod());
		assertEquals(strategy.getStrategyName(), resultStrategy.getStrategyName());
		assertEquals(strategy.getTimeFrameStart(), resultStrategy.getTimeFrameStart());
		assertEquals(strategy.getTimeFrameEnd(), resultStrategy.getTimeFrameEnd());
		assertEquals(strategy.getEnvironment().getEnvironmentId(), resultStrategy.getEnvironment().getEnvironmentId());
		assertEquals(strategy.getEnvironment().getEnvironmentName(), resultStrategy.getEnvironment().getEnvironmentName());
	}
	
	@Test
	public void should_getStrategy_whenGetStrategyById() throws Exception {
		Integer strategyId = strategyFirst.getStrategyId();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH + strategyId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		StrategyDto resultStrategy = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), StrategyDto.class);
		
		assertNotNull(resultStrategy);
		assertEquals(strategyFirst.getStrategyId(), resultStrategy.getStrategyId());
		assertEquals(strategyFirst.getStatus().getStatusId(), resultStrategy.getStatus().getStatusId());
		assertEquals(strategyFirst.getStatus().getValidityPeriod(), resultStrategy.getStatus().getValidityPeriod());
		assertEquals(strategyFirst.getStrategyName(), resultStrategy.getStrategyName());
		assertEquals(strategyFirst.getTimeFrameStart(), resultStrategy.getTimeFrameStart());
		assertEquals(strategyFirst.getTimeFrameEnd(), resultStrategy.getTimeFrameEnd());
		assertEquals(strategyFirst.getEnvironment().getEnvironmentId(), resultStrategy.getEnvironment().getEnvironmentId());
		assertEquals(strategyFirst.getEnvironment().getEnvironmentName(), resultStrategy.getEnvironment().getEnvironmentName());
	}
	
	@Test
	public void should_getStrategy_whenGetStrategyByStrategyname() throws Exception {
		String strategyName = strategyFirst.getStrategyName();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH + "strategyname/" + strategyName))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		StrategyDto resultStrategy = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), StrategyDto.class);
		
		assertNotNull(resultStrategy);
		assertEquals(strategyFirst.getStrategyId(), resultStrategy.getStrategyId());
		assertEquals(strategyFirst.getStatus().getStatusId(), resultStrategy.getStatus().getStatusId());
		assertEquals(strategyFirst.getStatus().getValidityPeriod(), resultStrategy.getStatus().getValidityPeriod());
		assertEquals(strategyFirst.getStrategyName(), resultStrategy.getStrategyName());
		assertEquals(strategyFirst.getTimeFrameStart(), resultStrategy.getTimeFrameStart());
		assertEquals(strategyFirst.getTimeFrameEnd(), resultStrategy.getTimeFrameEnd());
		assertEquals(strategyFirst.getEnvironment().getEnvironmentId(), resultStrategy.getEnvironment().getEnvironmentId());
		assertEquals(strategyFirst.getEnvironment().getEnvironmentName(), resultStrategy.getEnvironment().getEnvironmentName());
	}
	
	@Test
	public void should_getStrategies_whenGetStrategiesByEnvironmentid() throws Exception {
		Integer environmentId = strategyFirst.getEnvironment().getEnvironmentId();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH + "all-strategies-by-environmentid/" + environmentId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<StrategyDto> resultStrategies = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<StrategyDto>>() {});
		
		assertNotNull(resultStrategies);
		assertEquals(1, resultStrategies.size());
		assertEquals(strategyFirst.getStrategyId(), resultStrategies.get(0).getStrategyId());
		assertEquals(strategyFirst.getStatus().getStatusId(), resultStrategies.get(0).getStatus().getStatusId());
		assertEquals(strategyFirst.getStatus().getValidityPeriod(), resultStrategies.get(0).getStatus().getValidityPeriod());
		assertEquals(strategyFirst.getStrategyName(), resultStrategies.get(0).getStrategyName());
		assertEquals(strategyFirst.getTimeFrameStart(), resultStrategies.get(0).getTimeFrameStart());
		assertEquals(strategyFirst.getTimeFrameEnd(), resultStrategies.get(0).getTimeFrameEnd());
		assertEquals(strategyFirst.getEnvironment().getEnvironmentId(), resultStrategies.get(0).getEnvironment().getEnvironmentId());
		assertEquals(strategyFirst.getEnvironment().getEnvironmentName(), resultStrategies.get(0).getEnvironment().getEnvironmentName());
	}
	
	@Test
	public void should_getStrategies_whenGetAllStrategies() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<StrategyDto> resultStrategies = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<StrategyDto>>() {});
		
		assertNotNull(resultStrategies);
		assertEquals(2, resultStrategies.size());
		assertEquals(strategyFirst.getStrategyId(), resultStrategies.get(0).getStrategyId());
		assertEquals(strategyFirst.getStatus().getStatusId(), resultStrategies.get(0).getStatus().getStatusId());
		assertEquals(strategyFirst.getStatus().getValidityPeriod(), resultStrategies.get(0).getStatus().getValidityPeriod());
		assertEquals(strategyFirst.getStrategyName(), resultStrategies.get(0).getStrategyName());
		assertEquals(strategyFirst.getTimeFrameStart(), resultStrategies.get(0).getTimeFrameStart());
		assertEquals(strategyFirst.getTimeFrameEnd(), resultStrategies.get(0).getTimeFrameEnd());
		assertEquals(strategyFirst.getEnvironment().getEnvironmentId(), resultStrategies.get(0).getEnvironment().getEnvironmentId());
		assertEquals(strategyFirst.getEnvironment().getEnvironmentName(), resultStrategies.get(0).getEnvironment().getEnvironmentName());
		assertEquals(strategySecond.getStrategyId(), resultStrategies.get(1).getStrategyId());
		assertEquals(strategySecond.getStatus().getStatusId(), resultStrategies.get(1).getStatus().getStatusId());
		assertEquals(strategySecond.getStatus().getValidityPeriod(), resultStrategies.get(1).getStatus().getValidityPeriod());
		assertEquals(strategySecond.getStrategyName(), resultStrategies.get(1).getStrategyName());
		assertEquals(strategySecond.getTimeFrameStart(), resultStrategies.get(1).getTimeFrameStart());
		assertEquals(strategySecond.getTimeFrameEnd(), resultStrategies.get(1).getTimeFrameEnd());
		assertEquals(strategySecond.getEnvironment().getEnvironmentId(), resultStrategies.get(1).getEnvironment().getEnvironmentId());
		assertEquals(strategySecond.getEnvironment().getEnvironmentName(), resultStrategies.get(1).getEnvironment().getEnvironmentName());
	}
	
	@Test
	public void should_getBoolean_whenStrategyIdExists() throws Exception {
		Integer strategyId = strategyFirst.getStrategyId();
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "exists-by-id/" + strategyId))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("true"));	
	}
	
	@Test
	public void should_getBoolean_whenStrategyNameExists() throws Exception {
		String strategyName = strategyFirst.getStrategyName();
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "exists-by-strategyname/" + strategyName))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("true"));	
	}
	
	@Test
	public void should_putStrategy_whenUpdateStrategy() throws Exception {
		Integer strategyId = strategyFirst.getStrategyId();
		Integer statusId = strategyFirst.getStatus().getStatusId();
		String strategyName = "Strategy update test";
		LocalDate timeFrameStart = strategyFirst.getTimeFrameStart();
		LocalDate timeFrameEnd = strategyFirst.getTimeFrameEnd();
		Integer environmentId = strategyFirst.getEnvironment().getEnvironmentId();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(PATH + strategyId)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("statusId", statusId.toString())
				.param("strategyName", strategyName)
				.param("timeFrameStart", timeFrameStart.toString())
				.param("timeFrameEnd", timeFrameEnd.toString())
				.param("environmentId", environmentId.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		StrategyDto resultStrategy = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), StrategyDto.class);
		
		Strategy strategy = strategyService.getStrategyByStrategyname(strategyName);
		
		assertNotNull(resultStrategy);
		assertEquals(strategy.getStrategyId(), resultStrategy.getStrategyId());
		assertEquals(strategy.getStatus().getStatusId(), resultStrategy.getStatus().getStatusId());
		assertEquals(strategy.getStatus().getValidityPeriod(), resultStrategy.getStatus().getValidityPeriod());
		assertEquals(strategy.getStrategyName(), resultStrategy.getStrategyName());
		assertEquals(strategy.getTimeFrameStart(), resultStrategy.getTimeFrameStart());
		assertEquals(strategy.getTimeFrameEnd(), resultStrategy.getTimeFrameEnd());
		assertEquals(strategy.getEnvironment().getEnvironmentId(), resultStrategy.getEnvironment().getEnvironmentId());
		assertEquals(strategy.getEnvironment().getEnvironmentName(), resultStrategy.getEnvironment().getEnvironmentName());
	}
	
	@Test
	public void should_deleteStrategy_whenDeleteStrategy() throws Exception {
		Integer strategyId = strategyFirst.getStrategyId();
		
		mockMvc.perform(MockMvcRequestBuilders.delete(PATH + strategyId))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}
}
