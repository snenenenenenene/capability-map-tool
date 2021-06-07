package com.bavostepbros.leap.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.dto.EnvironmentDto;
import com.bavostepbros.leap.domain.service.environmentservice.EnvironmentService;
import com.bavostepbros.leap.persistence.EnvironmentDAL;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EnvironmentControllerTestApi extends ApiIntegrationTest {

	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private EnvironmentDAL environmentDAL;
	
	@Autowired
	private EnvironmentService environmentService;
	
	private Environment environmentFirst;
	private Environment environmentSecond;
	
	static final String PATH = "/api/environment/";
	
	// private static final Logger logger = LoggerFactory.getLogger(EnvironmentControllerIntegrationTest.class);

	@BeforeAll
	public void authenticate() throws Exception { super.authenticate(); }

	@BeforeEach
	public void init() {
		environmentFirst = environmentDAL.save(new Environment(1, "Test 1"));
		environmentSecond = environmentDAL.save(new Environment(2, "Test 2"));
	}
	
	@AfterEach
	public void close() {
		environmentDAL.delete(environmentFirst);
		environmentDAL.delete(environmentSecond);
	}
	
	@Test
	void should_notBeNull() {
		assertNotNull(environmentService);
		assertNotNull(environmentDAL);
		assertNotNull(environmentFirst);
		assertNotNull(environmentSecond);
	}
	
	@Test
	public void should_postEnvironment_whenSaveEnvironment() throws Exception {		
		MvcResult mvcResult = mockMvc.perform(post(PATH)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("environmentName", "Senne")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		EnvironmentDto resultEnvironment = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), EnvironmentDto.class);
		
		Environment environment = environmentService.getByEnvironmentName("Senne");
		
		assertNotNull(resultEnvironment);
		testEnvironment(environment, resultEnvironment);
	}
	
	@Test
	public void should_getEnvironment_whenGetEnvironmentById() throws Exception {
		Integer environmentId = environmentFirst.getEnvironmentId();
		
		MvcResult mvcResult = mockMvc.perform(get(PATH + environmentId))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();
		
		EnvironmentDto resultEnvironment = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), EnvironmentDto.class);
		
		assertNotNull(resultEnvironment);
		testEnvironment(environmentFirst, resultEnvironment);
	}
	
	@Test
	public void should_getEnvironment_whenGetEnvironmentByEnvironmentName() throws Exception {
		String environmentName = environmentFirst.getEnvironmentName();
		
		MvcResult mvcResult = mockMvc.perform(get(PATH + "environmentname/" + environmentName))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();
		
		EnvironmentDto resultEnvironment = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), EnvironmentDto.class);
		
		assertNotNull(resultEnvironment);
		testEnvironment(environmentFirst, resultEnvironment);
	}
	
	@Test
	public void should_getBoolean_whenEnvironmentExistsById() throws Exception {
		Integer environmentId = environmentFirst.getEnvironmentId();
		
		mockMvc.perform(get(PATH + "exists-by-id/" + environmentId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("true"));		
	}
	
	@Test
	public void should_getBoolean_whenEnvironmentNameExists() throws Exception {
		String environmentName = environmentFirst.getEnvironmentName();
		
		mockMvc.perform(get(PATH + "exists-by-environmentname/" + environmentName))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("true"));		
	}
	
	@Test
	public void should_getAllEnvironments_whenGetAllEnvironments() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get(PATH))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<EnvironmentDto> resultEnvironments = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<EnvironmentDto>>() {});
		
		assertNotNull(resultEnvironments);
		assertEquals(2, resultEnvironments.size());
		testEnvironment(environmentFirst, resultEnvironments.get(0));
		testEnvironment(environmentSecond, resultEnvironments.get(1));
	}
	
	@Test
	public void should_putEnvironment_whenUpdateEnvironment() throws Exception {
		Integer environmentId = environmentFirst.getEnvironmentId();
		String newName = "Senne";
		
		MvcResult mvcResult = mockMvc.perform(put(PATH + environmentId)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("environmentName", newName)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		EnvironmentDto resultEnvironment = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), EnvironmentDto.class);
		
		Environment environment = environmentService.getByEnvironmentName(newName);
		
		assertNotNull(resultEnvironment);
		testEnvironment(environment, resultEnvironment);
	}
	
	@Test
	public void should_deleteEnvironment_whenDeleteEnvironment() throws Exception {
		Integer environmentId = environmentFirst.getEnvironmentId();
		
		mockMvc.perform(delete(PATH + environmentId))
				.andExpect(MockMvcResultMatchers.status().isOk());	
	}
	
	@Test
	private void testEnvironment(Environment expectedObject, EnvironmentDto actualObject) {
		assertEquals(expectedObject.getEnvironmentId(), actualObject.getEnvironmentId());
		assertEquals(expectedObject.getEnvironmentName(), actualObject.getEnvironmentName());
	}
}
