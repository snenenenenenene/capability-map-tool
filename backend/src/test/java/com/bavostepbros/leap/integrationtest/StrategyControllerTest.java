package com.bavostepbros.leap.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import com.bavostepbros.leap.integrationtest.testconfiguration.RequestFactory;
import org.junit.jupiter.api.*;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StrategyControllerTest extends ApiIntegrationTest {
	
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
		
		MvcResult mvcResult = mockMvc.perform(post(PATH)
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
		testStrategy(strategy, resultStrategy);
	}
	
	@Test
	public void should_getStrategy_whenGetStrategyById() throws Exception {
		Integer strategyId = strategyFirst.getStrategyId();
		
		MvcResult mvcResult = mockMvc.perform(get(PATH + strategyId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		StrategyDto resultStrategy = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), StrategyDto.class);
		
		assertNotNull(resultStrategy);
		testStrategy(strategyFirst, resultStrategy);
	}
	
	@Test
	public void should_getStrategy_whenGetStrategyByStrategyname() throws Exception {
		String strategyName = strategyFirst.getStrategyName();
		
		MvcResult mvcResult = mockMvc.perform(get(PATH + "strategyname/" + strategyName))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		StrategyDto resultStrategy = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), StrategyDto.class);
		
		assertNotNull(resultStrategy);
		testStrategy(strategyFirst, resultStrategy);
	}
	
	@Test
	public void should_getStrategies_whenGetStrategiesByEnvironmentid() throws Exception {
		Integer environmentId = strategyFirst.getEnvironment().getEnvironmentId();
		
		MvcResult mvcResult = mockMvc.perform(get(PATH + "all-strategies-by-environmentid/" + environmentId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<StrategyDto> resultStrategies = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<StrategyDto>>() {});
		
		assertNotNull(resultStrategies);
		assertEquals(1, resultStrategies.size());
		testStrategy(strategyFirst, resultStrategies.get(0));
	}
	
	@Test
	public void should_getStrategies_whenGetAllStrategies() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get(PATH))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<StrategyDto> resultStrategies = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<StrategyDto>>() {});
		
		assertNotNull(resultStrategies);
		assertEquals(2, resultStrategies.size());
		testStrategy(strategyFirst, resultStrategies.get(0));
		testStrategy(strategySecond, resultStrategies.get(1));
	}
	
	@Test
	public void should_getBoolean_whenStrategyIdExists() throws Exception {
		Integer strategyId = strategyFirst.getStrategyId();
		
		mockMvc.perform(get(PATH + "exists-by-id/" + strategyId))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("true"));	
	}
	
	@Test
	public void should_getBoolean_whenStrategyNameExists() throws Exception {
		String strategyName = strategyFirst.getStrategyName();
		
		mockMvc.perform(get(PATH + "exists-by-strategyname/" + strategyName))
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
		
		MvcResult mvcResult = mockMvc.perform(put(PATH + strategyId)
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
		testStrategy(strategy, resultStrategy);
	}
	
	@Test
	public void should_deleteStrategy_whenDeleteStrategy() throws Exception {
		Integer strategyId = strategyFirst.getStrategyId();
		
		mockMvc.perform(delete(PATH + strategyId))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	private void testStrategy(Strategy expectedObject, StrategyDto actualObject) {
		assertEquals(expectedObject.getStrategyId(), actualObject.getStrategyId());
		assertEquals(expectedObject.getStatus().getStatusId(), actualObject.getStatus().getStatusId());
		assertEquals(expectedObject.getStatus().getValidityPeriod(), actualObject.getStatus().getValidityPeriod());
		assertEquals(expectedObject.getStrategyName(), actualObject.getStrategyName());
		assertEquals(expectedObject.getTimeFrameStart(), actualObject.getTimeFrameStart());
		assertEquals(expectedObject.getTimeFrameEnd(), actualObject.getTimeFrameEnd());
		assertEquals(expectedObject.getEnvironment().getEnvironmentId(), actualObject.getEnvironment().getEnvironmentId());
		assertEquals(expectedObject.getEnvironment().getEnvironmentName(), actualObject.getEnvironment().getEnvironmentName());
	}
}
