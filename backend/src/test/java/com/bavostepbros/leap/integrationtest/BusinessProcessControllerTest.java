package com.bavostepbros.leap.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

import com.bavostepbros.leap.domain.model.BusinessProcess;
import com.bavostepbros.leap.domain.model.dto.BusinessProcessDto;
import com.bavostepbros.leap.domain.service.businessprocessservice.BusinessProcessService;
import com.bavostepbros.leap.persistence.BusinessProcessDAL;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class BusinessProcessControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private BusinessProcessDAL businessProcessDAL;

	@Autowired
	private BusinessProcessService businessProcessService;

	static final String PATH = "/api/businessprocess/";

	private BusinessProcess businessProcessFirst;
	private BusinessProcess businessProcessSecond;

	@BeforeEach
	public void init() {
		businessProcessFirst = businessProcessDAL.save(new BusinessProcess(1, "Name", "This is a description"));
		businessProcessSecond = businessProcessDAL.save(new BusinessProcess(2, "Another name", "This is a description"));
	}
	
	@AfterEach
	public void close() {
		businessProcessDAL.delete(businessProcessFirst);
		businessProcessDAL.delete(businessProcessSecond);
	}
	
	@Test
	void should_notBeNull() {
		assertNotNull(businessProcessDAL);
		assertNotNull(businessProcessService);
		assertNotNull(businessProcessFirst);
		assertNotNull(businessProcessSecond);
	}
	
	@Test
	public void should_postBusinessProcess_whenSaveBusinessProcess() throws Exception {
		String newBusinessProcessName = "Post test";
		String newBusinessProcessDescription = businessProcessFirst.getBusinessProcessDescription();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("businessProcessName", newBusinessProcessName)
				.param("businessProcessDescription", newBusinessProcessDescription)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		BusinessProcessDto businessProcessDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), BusinessProcessDto.class);
		
		BusinessProcess businessProcess = businessProcessService.getBusinessProcessByName(newBusinessProcessName);
		
		assertNotNull(businessProcessDto);
		testBusinessProcess(businessProcess, businessProcessDto);
	}
	
	@Test
	public void should_getBusinessProcess_whenGetBusinessProcess() throws Exception {
		Integer businessProcessId = businessProcessFirst.getBusinessProcessId();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH + businessProcessId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		BusinessProcessDto businessProcessDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), BusinessProcessDto.class);
		
		assertNotNull(businessProcessDto);
		testBusinessProcess(businessProcessFirst, businessProcessDto);
	}
	
	@Test
	public void should_putBusinessProcess_whenUpdateBusinessProcess() throws Exception {
		Integer businessProcessId = businessProcessFirst.getBusinessProcessId();
		String newBusinessProcessName = "Update test";
		String businessProcessDescription = businessProcessFirst.getBusinessProcessDescription();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(PATH + businessProcessId)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("businessProcessName", newBusinessProcessName)
				.param("businessProcessDescription", businessProcessDescription)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		BusinessProcessDto businessProcessDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), BusinessProcessDto.class);
		
		BusinessProcess businessProcess = businessProcessService.getBusinessProcessByName(newBusinessProcessName);
		
		assertNotNull(businessProcessDto);
		testBusinessProcess(businessProcess, businessProcessDto);
	}
	
	@Test
	public void should_deleteBusinessProcess_whenDeleteBusinessProcess() throws Exception {
		Integer businessProcessId = businessProcessFirst.getBusinessProcessId();
		
		mockMvc.perform(MockMvcRequestBuilders.delete(PATH + businessProcessId))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void should_getBusinessProcess_whenGetBusinessProcessByName() throws Exception {
		String businessProcessName = businessProcessFirst.getBusinessProcessName();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH + "businessProcessName/" + businessProcessName))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		BusinessProcessDto businessProcessDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), BusinessProcessDto.class);
		
		assertNotNull(businessProcessDto);
		testBusinessProcess(businessProcessFirst, businessProcessDto);
	}
	
	@Test
	public void should_getAllBusinessProcess_whenGetAllBusinessProcessByName() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<BusinessProcessDto> businessProcessDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<BusinessProcessDto>>() {});
		
		assertNotNull(businessProcessDto);
		testBusinessProcess(businessProcessFirst, businessProcessDto.get(0));
		testBusinessProcess(businessProcessSecond, businessProcessDto.get(1));
	}
	
	@Test
	private void testBusinessProcess(BusinessProcess expectedObject, BusinessProcessDto actualObject) {
		assertEquals(expectedObject.getBusinessProcessId(), actualObject.getBusinessProcessId());
		assertEquals(expectedObject.getBusinessProcessName(), actualObject.getBusinessProcessName());
		assertEquals(expectedObject.getBusinessProcessDescription(), actualObject.getBusinessProcessDescription());
	}
}
