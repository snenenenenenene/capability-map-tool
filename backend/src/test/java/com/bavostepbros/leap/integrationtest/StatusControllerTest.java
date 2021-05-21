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

import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.dto.StatusDto;
import com.bavostepbros.leap.domain.service.statusservice.StatusService;
import com.bavostepbros.leap.persistence.StatusDAL;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class StatusControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private StatusDAL statusDAL;
	
	@Autowired
	private StatusService statusService;
	
	private Status statusFirst;
	private Status statusSecond;
	
	static final String PATH = "/api/status/";
	
	@BeforeEach
	public void init() {
		statusFirst = statusDAL.save(new Status(1, LocalDate.of(2021, 05, 15)));
		statusSecond = statusDAL.save(new Status(2, LocalDate.of(2021, 05, 20)));
	}
	
	@AfterEach
	public void close() {
		statusDAL.delete(statusFirst);
		statusDAL.delete(statusSecond);
	}
	
	@Test
	void should_notBeNull() {
		assertNotNull(statusService);
		assertNotNull(statusDAL);
		assertNotNull(statusFirst);
		assertNotNull(statusSecond);
	}
	
	@Test
	public void should_postStatus_whenSaveStatus() throws Exception {
		LocalDate validityPeriod = LocalDate.of(2021, 05, 27);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("validityPeriod", validityPeriod.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		StatusDto resultStatus = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), StatusDto.class);
		
		Status status = statusService.getByValidityPeriod(validityPeriod);
		
		assertNotNull(resultStatus);
		testStatus(status, resultStatus);
	}
	
	@Test
	public void should_getStatus_whenGetStatusById() throws Exception {
		Integer statusId = statusFirst.getStatusId();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH + statusId))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();
		
		StatusDto resultStatus = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), StatusDto.class);
		
		assertNotNull(resultStatus);
		testStatus(statusFirst, resultStatus);
	}
	
	@Test
	public void should_getStatus_whenGetStatusByValidityPeriod() throws Exception {
		LocalDate validityPeriod = statusFirst.getValidityPeriod();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH + "validityperiod/" + validityPeriod))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();
		
		StatusDto resultStatus = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), StatusDto.class);
		
		assertNotNull(resultStatus);
		testStatus(statusFirst, resultStatus);
	}
	
	@Test
	public void should_getAllStatus_whenGetAllStatus() throws Exception {		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();
		
		List<StatusDto> resultStatus = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<StatusDto>>() {});
		
		assertNotNull(resultStatus);
		assertEquals(2, resultStatus.size());
		testStatus(statusFirst, resultStatus.get(0));
		testStatus(statusSecond, resultStatus.get(1));
	}
	
	@Test
	public void should_getBoolean_whenStatusIdExists() throws Exception {
		Integer statusId = statusFirst.getStatusId();
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "exists-by-id/" + statusId))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("true"));	
	}
	
	@Test
	public void should_getBoolean_whenValidityPeriodExists() throws Exception {
		LocalDate validityPeriod = statusFirst.getValidityPeriod();
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "exists-by-validityperiod/" + validityPeriod))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("true"));	
	}
	
	@Test
	public void should_putStatus_whenUpdateStatus() throws Exception {
		Integer statusId = statusFirst.getStatusId();
		LocalDate validityPeriod = LocalDate.of(2021, 12, 13);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(PATH + statusId)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("validityPeriod", validityPeriod.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		StatusDto resultStatus = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), StatusDto.class);
		
		Status status = statusService.getByValidityPeriod(validityPeriod);
		
		assertNotNull(resultStatus);
		testStatus(status, resultStatus);
	}
	
	@Test
	public void should_deleteStatus_whenDeleteStatus() throws Exception {
		Integer statusId = statusFirst.getStatusId();
		
		mockMvc.perform(MockMvcRequestBuilders.delete(PATH + statusId))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	private void testStatus(Status expectedObject, StatusDto actualObject) {
		assertEquals(expectedObject.getStatusId(), actualObject.getStatusId());
		assertEquals(expectedObject.getValidityPeriod(), actualObject.getValidityPeriod());
	}
}
