package com.bavostepbros.leap.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

import com.bavostepbros.leap.domain.model.Technology;
import com.bavostepbros.leap.domain.model.dto.TechnologyDto;
import com.bavostepbros.leap.domain.service.technologyservice.TechnologyService;
import com.bavostepbros.leap.persistence.TechnologyDAL;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TechnologyControllerTest extends ApiIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private TechnologyDAL technologyDAL;

	@Autowired
	private TechnologyService technologyService;

	static final String PATH = "/api/technology/";

	private Technology technologyFirst;
	private Technology technologySecond;

	
	/** 
	 * @param init(
	 * @throws Exceptionpublic void init()
	 */
	@BeforeAll
	public void authenticate() throws Exception { super.authenticate(); }

	@BeforeEach
	public void init() {
		technologyFirst = technologyDAL.save(new Technology(1, "Java"));
		technologySecond = technologyDAL.save(new Technology(2, "c#"));
	}
	
	@AfterEach
	public void close() {
		technologyDAL.delete(technologyFirst);
		technologyDAL.delete(technologySecond);
	}
	
	@Test
	void should_notBeNull() {
		assertNotNull(technologyDAL);
		assertNotNull(technologyService);
		assertNotNull(technologyFirst);
		assertNotNull(technologySecond);
	}
	
	
	/** 
	 * @throws Exception
	 */
	@Test
	public void should_postTechnology_whenSaveTechnology() throws Exception {
		String technologyName = "Javascript";
		
		MvcResult mvcResult = mockMvc.perform(post(PATH)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("technologyName", technologyName)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		TechnologyDto technologyDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), TechnologyDto.class);
		
		Technology technology = technologyService.getByTechnologyName(technologyName);
		
		assertNotNull(technologyDto);
		testTechnology(technology, technologyDto);
	}
	
	
	/** 
	 * @throws Exception
	 */
	@Test
	public void should_getTechnology_whenGetTechnology() throws Exception {
		Integer technologyId = technologyFirst.getTechnologyId();
		
		MvcResult mvcResult = mockMvc.perform(get(PATH + technologyId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		TechnologyDto technologyDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), TechnologyDto.class);
		
		assertNotNull(technologyDto);
		testTechnology(technologyFirst, technologyDto);
	}
	
	
	/** 
	 * @throws Exception
	 */
	@Test
	public void should_putTechnology_whenUpdateTechnology() throws Exception {
		Integer technologyId = technologyFirst.getTechnologyId();
		String technologyName = "Angular";
		
		MvcResult mvcResult = mockMvc.perform(put(PATH + technologyId)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("technologyName", technologyName)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		TechnologyDto technologyDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), TechnologyDto.class);
		
		Technology technology = technologyService.getByTechnologyName(technologyName);
		
		assertNotNull(technologyDto);
		testTechnology(technology, technologyDto);
	}
	
	
	/** 
	 * @throws Exception
	 */
	@Test
	public void should_deleteTechnology_whenDeleteTechnology() throws Exception {
		Integer technologyId = technologyFirst.getTechnologyId();
		
		mockMvc.perform(delete(PATH + technologyId))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	
	/** 
	 * @throws Exception
	 */
	@Test
	public void should_getAllTechnologies_whenGetAllTechnologies() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get(PATH))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<TechnologyDto> technologies = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<TechnologyDto>>() {});
		
		assertNotNull(technologies);
		testTechnology(technologyFirst, technologies.get(0));
		testTechnology(technologySecond, technologies.get(1));
	}
	
	
	/** 
	 * @param expectedObject
	 * @param actualObject
	 */
	@Test
	private void testTechnology(Technology expectedObject, TechnologyDto actualObject) {
		assertEquals(expectedObject.getTechnologyId(), actualObject.getTechnologyId());
		assertEquals(expectedObject.getTechnologyName(), actualObject.getTechnologyName());
	}
}
