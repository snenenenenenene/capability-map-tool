package com.bavostepbros.leap.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;

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

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Resource;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.dto.CapabilityDto;
import com.bavostepbros.leap.domain.model.dto.ResourceDto;
import com.bavostepbros.leap.domain.model.paceofchange.PaceOfChange;
import com.bavostepbros.leap.domain.model.targetoperatingmodel.TargetOperatingModel;
import com.bavostepbros.leap.domain.service.capabilityservice.CapabilityService;
import com.bavostepbros.leap.domain.service.resourceservice.ResourceService;
import com.bavostepbros.leap.persistence.CapabilityDAL;
import com.bavostepbros.leap.persistence.EnvironmentDAL;
import com.bavostepbros.leap.persistence.ResourceDAL;
import com.bavostepbros.leap.persistence.StatusDAL;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ResourceControllerTest extends ApiIntegrationTest {
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ResourceDAL resourceDAL;
	
	@Autowired
	private StatusDAL statusDAL;

	@Autowired
	private EnvironmentDAL environmentDAL;
	
	@Autowired
	private CapabilityDAL capabilityDAL;
	
	@Autowired
	private CapabilityService capabilityService;

	@Autowired
	private ResourceService resourceService;
	
	static final String PATH = "/api/resource/";
	
	private Resource resourceFirst;
	private Resource resourceSecond;
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
		resourceFirst = resourceDAL.save(new Resource(1, "Resource 1", "Description 1", 20.0));
		resourceSecond = resourceDAL.save(new Resource(2, "Resource 2", "Description 2", 40.0));
		statusFirst = statusDAL.save(new Status(1, LocalDate.of(2021, 5, 15)));
		statusSecond = statusDAL.save(new Status(2, LocalDate.of(2021, 5, 20)));
		environmentFirst = environmentDAL.save(new Environment(1, "Test 1"));
		environmentSecond = environmentDAL.save(new Environment(2, "Test 2"));
		capabilityFirst = capabilityDAL.save(new Capability(1, environmentFirst, statusFirst, 0, "Capability 1",
				"Description 1", PaceOfChange.DIFFERENTIATION, TargetOperatingModel.COORDINATION, 1, 2.0, 3.0));
		capabilityService.updateLevel(capabilityFirst);
		capabilitySecond = capabilityDAL.save(
				new Capability(2, environmentSecond, statusSecond, capabilityFirst.getCapabilityId(), "Capability 2",
						"Description 2", PaceOfChange.INNOVATIVE, TargetOperatingModel.DIVERSIFICATION, 1, 2.0, 3.0));
		capabilityService.updateLevel(capabilitySecond);
		resourceService.addCapability(resourceSecond.getResourceId(), capabilitySecond.getCapabilityId());
	}
	
	@Test
	void should_notBeNull() {
		assertNotNull(resourceDAL);
		assertNotNull(statusDAL);
		assertNotNull(environmentDAL);
		assertNotNull(capabilityDAL);
		assertNotNull(capabilityService);
		assertNotNull(resourceService);
		
		assertNotNull(resourceFirst);
		assertNotNull(resourceSecond);
		assertNotNull(statusFirst);
		assertNotNull(statusSecond);
		assertNotNull(environmentFirst);
		assertNotNull(environmentSecond);
		assertNotNull(capabilityFirst);
		assertNotNull(capabilitySecond);
	}
	
	@Test
	public void should_returnResource_whenSaveResource() throws Exception {
		String resourceName = "Post test";
		String resourceDescription = resourceFirst.getResourceDescription();
		Double fullTimeEquivalentYearlyValue = resourceFirst.getFullTimeEquivalentYearlyValue();
		
		MvcResult mvcResult = mockMvc.perform(post(PATH)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("resourceName", resourceName)
				.param("resourceDescription", resourceDescription)
				.param("fullTimeEquivalentYearlyValue", fullTimeEquivalentYearlyValue.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		ResourceDto resourceDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), ResourceDto.class);
		
		Resource resource = resourceService.getResourceByName(resourceName);
		
		assertNotNull(resourceDto);
		testResource(resource, resourceDto);
	}
	
	@Test
	public void should_returnResource_whenGetResourceById() throws Exception {
		Integer resourceId = resourceFirst.getResourceId();
		
		MvcResult mvcResult = mockMvc.perform(get(PATH + resourceId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		ResourceDto resourceDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), ResourceDto.class);
		
		assertNotNull(resourceDto);
		testResource(resourceFirst, resourceDto);
	}
	
	@Test
	public void should_returnResource_whenUpdateResource() throws Exception {
		Integer resourceId = resourceFirst.getResourceId();
		String resourceName = "Update test";
		String resourceDescription = resourceFirst.getResourceDescription();
		Double fullTimeEquivalentYearlyValue = resourceFirst.getFullTimeEquivalentYearlyValue();
		
		MvcResult mvcResult = mockMvc.perform(put(PATH + resourceId)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("resourceName", resourceName)
				.param("resourceDescription", resourceDescription)
				.param("fullTimeEquivalentYearlyValue", fullTimeEquivalentYearlyValue.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		ResourceDto resourceDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), ResourceDto.class);
		
		Resource resource = resourceService.getResourceByName(resourceName);
		
		assertNotNull(resourceDto);
		testResource(resource, resourceDto);
	}
	
	@Test
	public void should_returnOk_whenDeleteResource() throws Exception {
		Integer resourceId = resourceFirst.getResourceId();
		
		mockMvc.perform(delete(PATH + resourceId))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void should_returnResource_whenGetResourceByName() throws Exception {
		String resourceName = resourceFirst.getResourceName();
		
		MvcResult mvcResult = mockMvc.perform(get(PATH + "resourcename/" + resourceName))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		ResourceDto resourceDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), ResourceDto.class);
		
		assertNotNull(resourceDto);
		testResource(resourceFirst, resourceDto);
	}
	
	@Test
	public void should_returnResources_whenGetAllResource() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get(PATH))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<ResourceDto> resourceDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<ResourceDto>>() {});
		
		assertNotNull(resourceDto);
		testResource(resourceFirst, resourceDto.get(0));
		testResource(resourceSecond, resourceDto.get(1));
	}
	
	@Test
	public void should_returnOk_whenLinkCapability() throws Exception {
		Integer resourceId = resourceFirst.getResourceId();
		Integer capabilityId = capabilityFirst.getCapabilityId();
		
		mockMvc.perform(put(PATH + "link-capability/")
			.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
			.param("resourceId", resourceId.toString())
			.param("capabilityId", capabilityId.toString())
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void should_returnOk_whenUnlinkCapability() throws Exception {
		Integer resourceId = resourceFirst.getResourceId();
		Integer capabilityId = capabilityFirst.getCapabilityId();
		
		mockMvc.perform(delete(PATH + "unlink-capability/")
			.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
			.param("resourceId", resourceId.toString())
			.param("capabilityId", capabilityId.toString())
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void should_getAllCapabilities_whenGetAllCapabilitiesByResourceId() throws Exception {
		Integer resourceId = resourceSecond.getResourceId();
		
		MvcResult mvcResult = mockMvc.perform(get(PATH + "get-capabilities/" + resourceId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<CapabilityDto> resultCapabilities = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), 
				new TypeReference<List<CapabilityDto>>() {});
		
		assertNotNull(resultCapabilities);
		testCapability(capabilitySecond, resultCapabilities.get(0));
	}
	
	private void testResource(Resource expectedObject, ResourceDto actualObject) {
		assertEquals(expectedObject.getResourceId(), actualObject.getResourceId());
		assertEquals(expectedObject.getResourceName(), actualObject.getResourceName());
		assertEquals(expectedObject.getResourceDescription(), actualObject.getResourceDescription());
		assertEquals(expectedObject.getFullTimeEquivalentYearlyValue(), actualObject.getFullTimeEquivalentYearlyValue());
	}
	
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
