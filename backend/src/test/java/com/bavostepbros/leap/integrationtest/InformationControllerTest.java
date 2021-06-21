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

import com.bavostepbros.leap.domain.model.Information;
import com.bavostepbros.leap.domain.model.dto.InformationDto;
import com.bavostepbros.leap.domain.service.informationservice.InformationService;
import com.bavostepbros.leap.persistence.InformationDAL;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InformationControllerTest extends ApiIntegrationTest {
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private InformationDAL informationDAL;
	
	@Autowired
	private InformationService informationService;
	
	static final String PATH = "/api/information/";
	
	private Information informationFirst;
	private Information informationSecond;
	
	@BeforeAll
	public void authenticate() throws Exception { super.authenticate(); }
	
	@BeforeEach
	public void init() {
		informationFirst = informationDAL.save(new Information(1, "Information 1", "Description 1"));
		informationSecond = informationDAL.save(new Information(2, "Information 2", "Description 2"));
	}
	
	@AfterEach
	public void close() {
		informationDAL.delete(informationFirst);
		informationDAL.delete(informationSecond);
	}
	
	@Test
	void should_notBeNull() {
		assertNotNull(informationDAL);
		assertNotNull(informationService);
		
		assertNotNull(informationFirst);
		assertNotNull(informationSecond);
	}
	
	@Test
	public void should_returnInformation_whenSaveInformation() throws Exception {
		String informationName = "Post test";
		String informationDescription = informationFirst.getInformationDescription();
		
		MvcResult mvcResult = mockMvc.perform(post(PATH)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("informationName", informationName)
				.param("informationDescription", informationDescription)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		InformationDto informationDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), InformationDto.class);
		
		Information information = informationService.getInformationByName(informationName);
		
		assertNotNull(informationDto);
		testInformation(information, informationDto);
	}
	
	@Test
	public void should_returnInformation_whenGetInformationById() throws Exception {
		Integer informationId = informationFirst.getInformationId();
		
		MvcResult mvcResult = mockMvc.perform(get(PATH + informationId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		InformationDto informationDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), InformationDto.class);
		
		assertNotNull(informationDto);
		testInformation(informationFirst, informationDto);
	}
	
	@Test
	public void should_returnInformation_whenUpdateInformation() throws Exception {
		Integer informationId = informationFirst.getInformationId();
		String informationName = "Update test";
		String informationDescription = informationFirst.getInformationDescription();
		
		MvcResult mvcResult = mockMvc.perform(put(PATH + informationId)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("informationName", informationName)
				.param("informationDescription", informationDescription)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		InformationDto informationDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), InformationDto.class);
		
		Information information = informationService.getInformationByName(informationName);
		
		assertNotNull(informationDto);
		testInformation(information, informationDto);
	}
	
	@Test
	public void should_returnOk_whenDeleteInformation() throws Exception {
		Integer informationId = informationFirst.getInformationId();
		
		mockMvc.perform(delete(PATH + informationId))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void should_returnInformation_whenGetInformationByName() throws Exception {
		String informationName = informationFirst.getInformationName();
		
		MvcResult mvcResult = mockMvc.perform(get(PATH + "informationName/" + informationName))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		InformationDto informationDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), InformationDto.class);
		
		assertNotNull(informationDto);
		testInformation(informationFirst, informationDto);
	}
	
	@Test
	public void should_returnInformations_whenGetAllInformation() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get(PATH))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<InformationDto> informationDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<InformationDto>>() {});
		
		assertNotNull(informationDto);
		testInformation(informationFirst, informationDto.get(0));
		testInformation(informationSecond, informationDto.get(1));
	}
	
	private void testInformation(Information expectedObject, InformationDto actualObject) {
		assertEquals(expectedObject.getInformationId(), actualObject.getInformationId());
		assertEquals(expectedObject.getInformationName(), actualObject.getInformationName());
		assertEquals(expectedObject.getInformationDescription(), actualObject.getInformationDescription());
	}
	
}
