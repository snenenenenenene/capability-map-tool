package com.bavostepbros.leap.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.bavostepbros.leap.domain.model.BusinessProcess;
import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.dto.BusinessProcessDto;
import com.bavostepbros.leap.domain.model.dto.CapabilityDto;
import com.bavostepbros.leap.domain.model.paceofchange.PaceOfChange;
import com.bavostepbros.leap.domain.model.targetoperatingmodel.TargetOperatingModel;
import com.bavostepbros.leap.domain.service.businessprocessservice.BusinessProcessService;
import com.bavostepbros.leap.domain.service.capabilityservice.CapabilityService;
import com.bavostepbros.leap.persistence.BusinessProcessDAL;
import com.bavostepbros.leap.persistence.CapabilityDAL;
import com.bavostepbros.leap.persistence.EnvironmentDAL;
import com.bavostepbros.leap.persistence.StatusDAL;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BusinessProcessControllerTest extends ApiIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private BusinessProcessDAL businessProcessDAL;
	
	@Autowired
	private StatusDAL statusDAL;

	@Autowired
	private EnvironmentDAL environmentDAL;
	
	@Autowired
	private CapabilityDAL capabilityDAL;
	
	@Autowired
	private CapabilityService capabilityService;

	@Autowired
	private BusinessProcessService businessProcessService;

	static final String PATH = "/api/businessprocess/";

	private BusinessProcess businessProcessFirst;
	private BusinessProcess businessProcessSecond;
	private Status statusFirst;
	private Status statusSecond;
	private Environment environmentFirst;
	private Environment environmentSecond;
	private Capability capabilityFirst;
	private Capability capabilitySecond;

	@BeforeAll
	public void authenticate() throws Exception { super.authenticate(); }

	@BeforeEach
	public void init() {
		businessProcessFirst = businessProcessDAL.save(new BusinessProcess(1, "Name", "This is a description"));
		businessProcessSecond = businessProcessDAL.save(new BusinessProcess(2, "Another name", "This is a description"));
		statusFirst = statusDAL.save(new Status(1, LocalDate.of(2021, 5, 15)));
		statusSecond = statusDAL.save(new Status(2, LocalDate.of(2021, 5, 20)));
		environmentFirst = environmentDAL.save(new Environment(1, "Test 1"));
		environmentSecond = environmentDAL.save(new Environment(2, "Test 2"));
		capabilityFirst = capabilityDAL.save(new Capability(1, environmentFirst, statusFirst, 0, "Capability 1",
				"Description 1", PaceOfChange.DIFFERENTIATION, TargetOperatingModel.COORDINATION, 1, 1, 1));
		capabilityService.updateLevel(capabilityFirst);
		capabilitySecond = capabilityDAL.save(
				new Capability(2, environmentSecond, statusSecond, capabilityFirst.getCapabilityId(), "Capability 2",
						"Description 2", PaceOfChange.INNOVATIVE, TargetOperatingModel.DIVERSIFICATION, 1, 1, 1));
		capabilityService.updateLevel(capabilitySecond);
		// businessProcessService.addCapability(businessProcessSecond.getBusinessProcessId(), capabilitySecond.getCapabilityId());
	}
	
	@AfterEach
	public void close() {
		businessProcessDAL.delete(businessProcessFirst);
		businessProcessDAL.delete(businessProcessSecond);
	}
	
	@Test
	void should_notBeNull() {
		assertNotNull(businessProcessDAL);
		assertNotNull(statusDAL);
		assertNotNull(environmentDAL);
		assertNotNull(capabilityDAL);
		assertNotNull(capabilityService);
		assertNotNull(businessProcessService);
		assertNotNull(businessProcessFirst);
		assertNotNull(businessProcessSecond);
		assertNotNull(statusFirst);
		assertNotNull(statusSecond);
		assertNotNull(environmentFirst);
		assertNotNull(environmentSecond);
		assertNotNull(capabilityFirst);
		assertNotNull(capabilitySecond);
	}
	
	@Test
	public void should_throwInvalidInput_whenSaveBusinessProcessInvalidName() throws Exception {
		String newBusinessProcessName = "";
		String newBusinessProcessDescription = businessProcessFirst.getBusinessProcessDescription();
		
		mockMvc.perform(post(PATH)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("businessProcessName", newBusinessProcessName)
				.param("businessProcessDescription", newBusinessProcessDescription)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	public void should_throwInvalidInput_whenSaveBusinessProcessInvalidDescription() throws Exception {
		String newBusinessProcessName = "Very unique name";
		String newBusinessProcessDescription = "";
		
		mockMvc.perform(post(PATH)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("businessProcessName", newBusinessProcessName)
				.param("businessProcessDescription", newBusinessProcessDescription)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	public void should_throwInternalServerError_whenSaveBusinessProcessDuplicateName() throws Exception {
		String newBusinessProcessName = businessProcessFirst.getBusinessProcessName();
		String newBusinessProcessDescription = businessProcessFirst.getBusinessProcessDescription();
		
		mockMvc.perform(post(PATH)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("businessProcessName", newBusinessProcessName)
				.param("businessProcessDescription", newBusinessProcessDescription)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}
	
	@Test
	public void should_postBusinessProcess_whenSaveBusinessProcess() throws Exception {
		String newBusinessProcessName = "Post test";
		String newBusinessProcessDescription = businessProcessFirst.getBusinessProcessDescription();
		
		MvcResult mvcResult = mockMvc.perform(post(PATH)
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
	public void should_throwInvalidInput_whenGetBusinessProcessInvalidId() throws Exception {
		Integer businessProcessId = 0;
		
		mockMvc.perform(get(PATH + businessProcessId))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	public void should_getBusinessProcess_whenGetBusinessProcess() throws Exception {
		Integer businessProcessId = businessProcessFirst.getBusinessProcessId();
		
		MvcResult mvcResult = mockMvc.perform(get(PATH + businessProcessId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		BusinessProcessDto businessProcessDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), BusinessProcessDto.class);
		
		assertNotNull(businessProcessDto);
		testBusinessProcess(businessProcessFirst, businessProcessDto);
	}
	
	@Test
	public void should_throwInvalidInput_whenUpdateBusinessProcessInvalidId() throws Exception {
		Integer businessProcessId = 0;
		String newBusinessProcessName = "Update test";
		String businessProcessDescription = businessProcessFirst.getBusinessProcessDescription();
		
		mockMvc.perform(put(PATH + businessProcessId)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("businessProcessName", newBusinessProcessName)
				.param("businessProcessDescription", businessProcessDescription)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	public void should_throwInvalidInput_whenUpdateBusinessProcessInvalidName() throws Exception {
		Integer businessProcessId = businessProcessFirst.getBusinessProcessId();
		String newBusinessProcessName = "";
		String businessProcessDescription = businessProcessFirst.getBusinessProcessDescription();
		
		mockMvc.perform(put(PATH + businessProcessId)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("businessProcessName", newBusinessProcessName)
				.param("businessProcessDescription", businessProcessDescription)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	public void should_throwInvalidInput_whenUpdateBusinessProcessInvalidDescription() throws Exception {
		Integer businessProcessId = businessProcessFirst.getBusinessProcessId();
		String newBusinessProcessName = "Update test";
		String businessProcessDescription = "";
		
		mockMvc.perform(put(PATH + businessProcessId)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("businessProcessName", newBusinessProcessName)
				.param("businessProcessDescription", businessProcessDescription)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	public void should_throwInternalServerError_whenUpdateBusinessProcessDuplicateName() throws Exception {
		Integer businessProcessId = businessProcessFirst.getBusinessProcessId();
		String newBusinessProcessName = businessProcessSecond.getBusinessProcessName();
		String businessProcessDescription = businessProcessFirst.getBusinessProcessDescription();
		
		mockMvc.perform(put(PATH + businessProcessId)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("businessProcessName", newBusinessProcessName)
				.param("businessProcessDescription", businessProcessDescription)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}
	
	@Test
	public void should_putBusinessProcess_whenUpdateBusinessProcess() throws Exception {
		Integer businessProcessId = businessProcessFirst.getBusinessProcessId();
		String newBusinessProcessName = "Update test";
		String businessProcessDescription = businessProcessFirst.getBusinessProcessDescription();
		
		MvcResult mvcResult = mockMvc.perform(put(PATH + businessProcessId)
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
	public void should_throwInvalidInput_whenDeleteBusinessProcessInvalidId() throws Exception {
		Integer businessProcessId = 0;
		
		mockMvc.perform(delete(PATH + businessProcessId))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	public void should_deleteBusinessProcess_whenDeleteBusinessProcess() throws Exception {
		Integer businessProcessId = businessProcessFirst.getBusinessProcessId();
		
		mockMvc.perform(delete(PATH + businessProcessId))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void should_throwInvalidInput_whenGetBusinessProcessByNameInvalidName() throws Exception {
		String businessProcessName = "";
		
		mockMvc.perform(get(PATH + "businessProcessName/" + businessProcessName))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	public void should_getBusinessProcess_whenGetBusinessProcessByName() throws Exception {
		String businessProcessName = businessProcessFirst.getBusinessProcessName();
		
		MvcResult mvcResult = mockMvc.perform(get(PATH + "businessProcessName/" + businessProcessName))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		BusinessProcessDto businessProcessDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), BusinessProcessDto.class);
		
		assertNotNull(businessProcessDto);
		testBusinessProcess(businessProcessFirst, businessProcessDto);
	}
	
	@Test
	public void should_getAllBusinessProcess_whenGetAllBusinessProcessByName() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get(PATH))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<BusinessProcessDto> businessProcessDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<BusinessProcessDto>>() {});
		
		assertNotNull(businessProcessDto);
		testBusinessProcess(businessProcessFirst, businessProcessDto.get(0));
		testBusinessProcess(businessProcessSecond, businessProcessDto.get(1));
	}
	
	@Test
	public void should_returnOk_whenLinkCapability() throws Exception {
		Integer businessProcessId = businessProcessFirst.getBusinessProcessId();
		Integer capabilityId = capabilityFirst.getCapabilityId();
		
		mockMvc.perform(put(PATH + "link-capability/")
			.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
			.param("businessProcessId", businessProcessId.toString())
			.param("capabilityId", capabilityId.toString())
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	private void testBusinessProcess(BusinessProcess expectedObject, BusinessProcessDto actualObject) {
		assertEquals(expectedObject.getBusinessProcessId(), actualObject.getBusinessProcessId());
		assertEquals(expectedObject.getBusinessProcessName(), actualObject.getBusinessProcessName());
		assertEquals(expectedObject.getBusinessProcessDescription(), actualObject.getBusinessProcessDescription());
	}
	
	@Test
	private void testCapability(Capability expectedObject, CapabilityDto actualObject) {
		assertEquals(expectedObject.getCapabilityId(), actualObject.getCapabilityId());
		assertEquals(expectedObject.getEnvironment().getEnvironmentId(),
				actualObject.getEnvironment().getEnvironmentId());
		assertEquals(expectedObject.getEnvironment().getEnvironmentName(),
				actualObject.getEnvironment().getEnvironmentName());
		assertEquals(expectedObject.getStatus().getStatusId(), actualObject.getStatus().getStatusId());
		assertEquals(expectedObject.getStatus().getValidityPeriod(), actualObject.getStatus().getValidityPeriod());
		assertEquals(expectedObject.getParentCapabilityId(), actualObject.getParentCapabilityId());
		assertEquals(expectedObject.getCapabilityName(), actualObject.getCapabilityName());
		assertEquals(expectedObject.getLevel(), actualObject.getLevel());
		assertEquals(expectedObject.getPaceOfChange(), actualObject.getPaceOfChange());
		assertEquals(expectedObject.getTargetOperatingModel(), actualObject.getTargetOperatingModel());
		assertEquals(expectedObject.getResourceQuality(), actualObject.getResourceQuality());
		assertEquals(expectedObject.getInformationQuality(), actualObject.getInformationQuality());
		assertEquals(expectedObject.getApplicationFit(), actualObject.getApplicationFit());
	}
}
