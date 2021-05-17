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

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.capabilitylevel.CapabilityLevel;
import com.bavostepbros.leap.domain.model.dto.CapabilityDto;
import com.bavostepbros.leap.domain.service.capabilityservice.CapabilityService;
import com.bavostepbros.leap.persistence.CapabilityDAL;
import com.bavostepbros.leap.persistence.EnvironmentDAL;
import com.bavostepbros.leap.persistence.StatusDAL;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class CapabilityControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private StatusDAL statusDAL;
	
	@Autowired
	private EnvironmentDAL environmentDAL;
	
	@Autowired
	private CapabilityDAL capabilityDAL;
	
	@Autowired
	private CapabilityService capabilityService;
	
	private Status statusFirst;
	private Status statusSecond;
	private Environment environmentFirst;
	private Environment environmentSecond;
	private Capability capabilityFirst;
	private Capability capabilitySecond;
	private Capability capabilityThirth;
	
	static final String PATH = "/api/capability/";
	
	@BeforeEach
	public void init() {
		statusFirst = statusDAL.save(new Status(1, LocalDate.of(2021, 05, 15)));
		statusSecond = statusDAL.save(new Status(2, LocalDate.of(2021, 05, 20)));
		environmentFirst = environmentDAL.save(new Environment("Test 1"));
		environmentSecond = environmentDAL.save(new Environment("Test 2"));
		capabilityFirst = capabilityDAL.save(new Capability(1, environmentFirst,
				statusFirst, 1, "Capability 1", CapabilityLevel.ONE, true, 
				"Target Operating Model", 1, 1, 1));
		capabilitySecond = capabilityDAL.save(new Capability(2, environmentFirst,
				statusFirst, capabilityFirst.getCapabilityId(), "Capability 2", 
				CapabilityLevel.TWO, true, "Target Operating Model", 1, 1, 1));
		capabilityThirth = capabilityDAL.save(new Capability(3, environmentSecond,
				statusSecond, capabilityFirst.getCapabilityId(), "Capability 3", 
				CapabilityLevel.TWO, true, "Target Operating Model", 1, 1, 1));
	}
	
	@AfterEach
	public void close() {
		statusDAL.delete(statusFirst);
		statusDAL.delete(statusSecond);
		environmentDAL.delete(environmentFirst);
		environmentDAL.delete(environmentSecond);
		capabilityDAL.delete(capabilityFirst);
		capabilityDAL.delete(capabilitySecond);
		capabilityDAL.delete(capabilityThirth);
	}
	
	@Test
	void should_notBeNull() {
		assertNotNull(statusDAL);
		assertNotNull(environmentDAL);
		assertNotNull(capabilityDAL);
		assertNotNull(capabilityService);
		assertNotNull(statusFirst);
		assertNotNull(statusSecond);
		assertNotNull(environmentFirst);
		assertNotNull(environmentSecond);
		assertNotNull(capabilityFirst);
		assertNotNull(capabilitySecond);
	}
	
	@Test
	public void should_postCapability_whenSaveCapability() throws Exception {
		Integer environmentId = environmentFirst.getEnvironmentId();
		Integer statusId = statusFirst.getStatusId();
		Integer parentCapabilityId = 1;
		String capabilityName = "Posttest";
		String level = "THREE";
		String paceOfChange = "true";
		String targetOperatingModel = "Target Operating Model";
		Integer resourceQuality = 1;
		Integer informationQuality = 1;
		Integer applicationFit = 1;
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(PATH)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("environmentId", environmentId.toString())
				.param("statusId", statusId.toString())
				.param("parentCapabilityId", parentCapabilityId.toString())
				.param("capabilityName", capabilityName)
				.param("level", level)
				.param("paceOfChange", paceOfChange)
				.param("targetOperatingModel", targetOperatingModel)
				.param("resourceQuality", resourceQuality.toString())
				.param("informationQuality", informationQuality.toString())
				.param("applicationFit", applicationFit.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		CapabilityDto resultCapability = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), CapabilityDto.class);
		
		Capability capability = capabilityService.getCapabilityByCapabilityName(capabilityName);
		
		assertNotNull(resultCapability);
		assertEquals(capability.getCapabilityId(), resultCapability.getCapabilityId());
		assertEquals(capability.getEnvironment().getEnvironmentId(), resultCapability.getEnvironment().getEnvironmentId());
		assertEquals(capability.getEnvironment().getEnvironmentName(), resultCapability.getEnvironment().getEnvironmentName());
		assertEquals(capability.getStatus().getStatusId(), resultCapability.getStatus().getStatusId());
		assertEquals(capability.getStatus().getValidityPeriod(), resultCapability.getStatus().getValidityPeriod());
		assertEquals(capability.getParentCapabilityId(), resultCapability.getParentCapabilityId());
		assertEquals(capability.getCapabilityName(), resultCapability.getCapabilityName());
		assertEquals(capability.getLevel(), resultCapability.getLevel());
		assertEquals(capability.isPaceOfChange(), resultCapability.isPaceOfChange());
		assertEquals(capability.getTargetOperatingModel(), resultCapability.getTargetOperatingModel());
		assertEquals(capability.getResourceQuality(), resultCapability.getResourceQuality());
		assertEquals(capability.getInformationQuality(), resultCapability.getInformationQuality());
		assertEquals(capability.getApplicationFit(), resultCapability.getApplicationFit());
	}
	
	@Test
	public void should_getCapability_whenGetCapabilityById() throws Exception {
		Integer capabilityId = capabilityFirst.getCapabilityId();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH + capabilityId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		CapabilityDto resultCapability = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), CapabilityDto.class);
		
		assertNotNull(resultCapability);
		assertEquals(capabilityFirst.getCapabilityId(), resultCapability.getCapabilityId());
		assertEquals(capabilityFirst.getEnvironment().getEnvironmentId(), resultCapability.getEnvironment().getEnvironmentId());
		assertEquals(capabilityFirst.getEnvironment().getEnvironmentName(), resultCapability.getEnvironment().getEnvironmentName());
		assertEquals(capabilityFirst.getStatus().getStatusId(), resultCapability.getStatus().getStatusId());
		assertEquals(capabilityFirst.getStatus().getValidityPeriod(), resultCapability.getStatus().getValidityPeriod());
		assertEquals(capabilityFirst.getParentCapabilityId(), resultCapability.getParentCapabilityId());
		assertEquals(capabilityFirst.getCapabilityName(), resultCapability.getCapabilityName());
		assertEquals(capabilityFirst.getLevel(), resultCapability.getLevel());
		assertEquals(capabilityFirst.isPaceOfChange(), resultCapability.isPaceOfChange());
		assertEquals(capabilityFirst.getTargetOperatingModel(), resultCapability.getTargetOperatingModel());
		assertEquals(capabilityFirst.getResourceQuality(), resultCapability.getResourceQuality());
		assertEquals(capabilityFirst.getInformationQuality(), resultCapability.getInformationQuality());
		assertEquals(capabilityFirst.getApplicationFit(), resultCapability.getApplicationFit());
	}
	
	@Test
	public void should_getCapability_whenGetCapabilityByCapabilityName() throws Exception {
		String capabilityName = capabilityFirst.getCapabilityName();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH + "capabilityname/" + capabilityName))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		CapabilityDto resultCapability = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), CapabilityDto.class);
		
		assertNotNull(resultCapability);
		assertEquals(capabilityFirst.getCapabilityId(), resultCapability.getCapabilityId());
		assertEquals(capabilityFirst.getEnvironment().getEnvironmentId(), resultCapability.getEnvironment().getEnvironmentId());
		assertEquals(capabilityFirst.getEnvironment().getEnvironmentName(), resultCapability.getEnvironment().getEnvironmentName());
		assertEquals(capabilityFirst.getStatus().getStatusId(), resultCapability.getStatus().getStatusId());
		assertEquals(capabilityFirst.getStatus().getValidityPeriod(), resultCapability.getStatus().getValidityPeriod());
		assertEquals(capabilityFirst.getParentCapabilityId(), resultCapability.getParentCapabilityId());
		assertEquals(capabilityFirst.getCapabilityName(), resultCapability.getCapabilityName());
		assertEquals(capabilityFirst.getLevel(), resultCapability.getLevel());
		assertEquals(capabilityFirst.isPaceOfChange(), resultCapability.isPaceOfChange());
		assertEquals(capabilityFirst.getTargetOperatingModel(), resultCapability.getTargetOperatingModel());
		assertEquals(capabilityFirst.getResourceQuality(), resultCapability.getResourceQuality());
		assertEquals(capabilityFirst.getInformationQuality(), resultCapability.getInformationQuality());
		assertEquals(capabilityFirst.getApplicationFit(), resultCapability.getApplicationFit());
	}
	
	@Test
	public void should_getCapabilities_whenGetCapabilitiesByEnvironmentid() throws Exception {
		Integer environmentId = capabilityFirst.getEnvironment().getEnvironmentId();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH + "all-capabilities-by-environmentid/" + environmentId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<CapabilityDto> resultCapabilities = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<CapabilityDto>>() {});
		
		assertNotNull(resultCapabilities);
		assertEquals(2, resultCapabilities.size());
		assertEquals(capabilityFirst.getCapabilityId(), resultCapabilities.get(0).getCapabilityId());
		assertEquals(capabilityFirst.getEnvironment().getEnvironmentId(), resultCapabilities.get(0).getEnvironment().getEnvironmentId());
		assertEquals(capabilityFirst.getEnvironment().getEnvironmentName(), resultCapabilities.get(0).getEnvironment().getEnvironmentName());
		assertEquals(capabilityFirst.getStatus().getStatusId(), resultCapabilities.get(0).getStatus().getStatusId());
		assertEquals(capabilityFirst.getStatus().getValidityPeriod(), resultCapabilities.get(0).getStatus().getValidityPeriod());
		assertEquals(capabilityFirst.getParentCapabilityId(), resultCapabilities.get(0).getParentCapabilityId());
		assertEquals(capabilityFirst.getCapabilityName(), resultCapabilities.get(0).getCapabilityName());
		assertEquals(capabilityFirst.getLevel(), resultCapabilities.get(0).getLevel());
		assertEquals(capabilityFirst.isPaceOfChange(), resultCapabilities.get(0).isPaceOfChange());
		assertEquals(capabilityFirst.getTargetOperatingModel(), resultCapabilities.get(0).getTargetOperatingModel());
		assertEquals(capabilityFirst.getResourceQuality(), resultCapabilities.get(0).getResourceQuality());
		assertEquals(capabilityFirst.getInformationQuality(), resultCapabilities.get(0).getInformationQuality());
		assertEquals(capabilityFirst.getApplicationFit(), resultCapabilities.get(0).getApplicationFit());
		assertEquals(capabilitySecond.getCapabilityId(), resultCapabilities.get(1).getCapabilityId());
		assertEquals(capabilitySecond.getEnvironment().getEnvironmentId(), resultCapabilities.get(1).getEnvironment().getEnvironmentId());
		assertEquals(capabilitySecond.getEnvironment().getEnvironmentName(), resultCapabilities.get(1).getEnvironment().getEnvironmentName());
		assertEquals(capabilitySecond.getStatus().getStatusId(), resultCapabilities.get(1).getStatus().getStatusId());
		assertEquals(capabilitySecond.getStatus().getValidityPeriod(), resultCapabilities.get(1).getStatus().getValidityPeriod());
		assertEquals(capabilitySecond.getParentCapabilityId(), resultCapabilities.get(1).getParentCapabilityId());
		assertEquals(capabilitySecond.getCapabilityName(), resultCapabilities.get(1).getCapabilityName());
		assertEquals(capabilitySecond.getLevel(), resultCapabilities.get(1).getLevel());
		assertEquals(capabilitySecond.isPaceOfChange(), resultCapabilities.get(1).isPaceOfChange());
		assertEquals(capabilitySecond.getTargetOperatingModel(), resultCapabilities.get(1).getTargetOperatingModel());
		assertEquals(capabilitySecond.getResourceQuality(), resultCapabilities.get(1).getResourceQuality());
		assertEquals(capabilitySecond.getInformationQuality(), resultCapabilities.get(1).getInformationQuality());
		assertEquals(capabilitySecond.getApplicationFit(), resultCapabilities.get(1).getApplicationFit());
	}
	
	@Test
	public void should_getCapabilities_whenGetCapabilitiesByLevel() throws Exception {
		String level = capabilityFirst.getLevel().toString();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH + "all-capabilities-by-level/" + level))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<CapabilityDto> resultCapabilities = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<CapabilityDto>>() {});
		
		assertNotNull(resultCapabilities);
		assertEquals(1, resultCapabilities.size());
		assertEquals(capabilityFirst.getCapabilityId(), resultCapabilities.get(0).getCapabilityId());
		assertEquals(capabilityFirst.getEnvironment().getEnvironmentId(), resultCapabilities.get(0).getEnvironment().getEnvironmentId());
		assertEquals(capabilityFirst.getEnvironment().getEnvironmentName(), resultCapabilities.get(0).getEnvironment().getEnvironmentName());
		assertEquals(capabilityFirst.getStatus().getStatusId(), resultCapabilities.get(0).getStatus().getStatusId());
		assertEquals(capabilityFirst.getStatus().getValidityPeriod(), resultCapabilities.get(0).getStatus().getValidityPeriod());
		assertEquals(capabilityFirst.getParentCapabilityId(), resultCapabilities.get(0).getParentCapabilityId());
		assertEquals(capabilityFirst.getCapabilityName(), resultCapabilities.get(0).getCapabilityName());
		assertEquals(capabilityFirst.getLevel(), resultCapabilities.get(0).getLevel());
		assertEquals(capabilityFirst.isPaceOfChange(), resultCapabilities.get(0).isPaceOfChange());
		assertEquals(capabilityFirst.getTargetOperatingModel(), resultCapabilities.get(0).getTargetOperatingModel());
		assertEquals(capabilityFirst.getResourceQuality(), resultCapabilities.get(0).getResourceQuality());
		assertEquals(capabilityFirst.getInformationQuality(), resultCapabilities.get(0).getInformationQuality());
		assertEquals(capabilityFirst.getApplicationFit(), resultCapabilities.get(0).getApplicationFit());
	}
	
	@Test
	public void should_getCapabilities_whenGetCapabilitiesByParentcapabilityid() throws Exception {
		Integer parentCapabilityId = capabilitySecond.getParentCapabilityId();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH + "all-capabilities-by-parentcapabilityid/" + parentCapabilityId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<CapabilityDto> resultCapabilities = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<CapabilityDto>>() {});
		
		assertNotNull(resultCapabilities);
		assertEquals(2, resultCapabilities.size());
		assertEquals(capabilitySecond.getCapabilityId(), resultCapabilities.get(0).getCapabilityId());
		assertEquals(capabilitySecond.getEnvironment().getEnvironmentId(), resultCapabilities.get(0).getEnvironment().getEnvironmentId());
		assertEquals(capabilitySecond.getEnvironment().getEnvironmentName(), resultCapabilities.get(0).getEnvironment().getEnvironmentName());
		assertEquals(capabilitySecond.getStatus().getStatusId(), resultCapabilities.get(0).getStatus().getStatusId());
		assertEquals(capabilitySecond.getStatus().getValidityPeriod(), resultCapabilities.get(0).getStatus().getValidityPeriod());
		assertEquals(capabilitySecond.getParentCapabilityId(), resultCapabilities.get(0).getParentCapabilityId());
		assertEquals(capabilitySecond.getCapabilityName(), resultCapabilities.get(0).getCapabilityName());
		assertEquals(capabilitySecond.getLevel(), resultCapabilities.get(0).getLevel());
		assertEquals(capabilitySecond.isPaceOfChange(), resultCapabilities.get(0).isPaceOfChange());
		assertEquals(capabilitySecond.getTargetOperatingModel(), resultCapabilities.get(0).getTargetOperatingModel());
		assertEquals(capabilitySecond.getResourceQuality(), resultCapabilities.get(0).getResourceQuality());
		assertEquals(capabilitySecond.getInformationQuality(), resultCapabilities.get(0).getInformationQuality());
		assertEquals(capabilitySecond.getApplicationFit(), resultCapabilities.get(0).getApplicationFit());		
		assertEquals(capabilityThirth.getCapabilityId(), resultCapabilities.get(1).getCapabilityId());
		assertEquals(capabilityThirth.getEnvironment().getEnvironmentId(), resultCapabilities.get(1).getEnvironment().getEnvironmentId());
		assertEquals(capabilityThirth.getEnvironment().getEnvironmentName(), resultCapabilities.get(1).getEnvironment().getEnvironmentName());
		assertEquals(capabilityThirth.getStatus().getStatusId(), resultCapabilities.get(1).getStatus().getStatusId());
		assertEquals(capabilityThirth.getStatus().getValidityPeriod(), resultCapabilities.get(1).getStatus().getValidityPeriod());
		assertEquals(capabilityThirth.getParentCapabilityId(), resultCapabilities.get(1).getParentCapabilityId());
		assertEquals(capabilityThirth.getCapabilityName(), resultCapabilities.get(1).getCapabilityName());
		assertEquals(capabilityThirth.getLevel(), resultCapabilities.get(1).getLevel());
		assertEquals(capabilityThirth.isPaceOfChange(), resultCapabilities.get(1).isPaceOfChange());
		assertEquals(capabilityThirth.getTargetOperatingModel(), resultCapabilities.get(1).getTargetOperatingModel());
		assertEquals(capabilityThirth.getResourceQuality(), resultCapabilities.get(1).getResourceQuality());
		assertEquals(capabilityThirth.getInformationQuality(), resultCapabilities.get(1).getInformationQuality());
		assertEquals(capabilityThirth.getApplicationFit(), resultCapabilities.get(1).getApplicationFit());
	}
	
	@Test
	public void should_getCapabilities_whenGetCapabilitiesByParentcapabilityidAndLevel() throws Exception {
		String level = capabilitySecond.getLevel().toString();
		Integer parentCapabilityId = capabilitySecond.getParentCapabilityId();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH + "all-capabilities-by-parentcapabilityid-and-level/" + parentCapabilityId + "/" + level))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<CapabilityDto> resultCapabilities = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<CapabilityDto>>() {});
		
		assertNotNull(resultCapabilities);
		assertEquals(2, resultCapabilities.size());
		assertEquals(capabilitySecond.getCapabilityId(), resultCapabilities.get(0).getCapabilityId());
		assertEquals(capabilitySecond.getEnvironment().getEnvironmentId(), resultCapabilities.get(0).getEnvironment().getEnvironmentId());
		assertEquals(capabilitySecond.getEnvironment().getEnvironmentName(), resultCapabilities.get(0).getEnvironment().getEnvironmentName());
		assertEquals(capabilitySecond.getStatus().getStatusId(), resultCapabilities.get(0).getStatus().getStatusId());
		assertEquals(capabilitySecond.getStatus().getValidityPeriod(), resultCapabilities.get(0).getStatus().getValidityPeriod());
		assertEquals(capabilitySecond.getParentCapabilityId(), resultCapabilities.get(0).getParentCapabilityId());
		assertEquals(capabilitySecond.getCapabilityName(), resultCapabilities.get(0).getCapabilityName());
		assertEquals(capabilitySecond.getLevel(), resultCapabilities.get(0).getLevel());
		assertEquals(capabilitySecond.isPaceOfChange(), resultCapabilities.get(0).isPaceOfChange());
		assertEquals(capabilitySecond.getTargetOperatingModel(), resultCapabilities.get(0).getTargetOperatingModel());
		assertEquals(capabilitySecond.getResourceQuality(), resultCapabilities.get(0).getResourceQuality());
		assertEquals(capabilitySecond.getInformationQuality(), resultCapabilities.get(0).getInformationQuality());
		assertEquals(capabilitySecond.getApplicationFit(), resultCapabilities.get(0).getApplicationFit());		
		assertEquals(capabilityThirth.getCapabilityId(), resultCapabilities.get(1).getCapabilityId());
		assertEquals(capabilityThirth.getEnvironment().getEnvironmentId(), resultCapabilities.get(1).getEnvironment().getEnvironmentId());
		assertEquals(capabilityThirth.getEnvironment().getEnvironmentName(), resultCapabilities.get(1).getEnvironment().getEnvironmentName());
		assertEquals(capabilityThirth.getStatus().getStatusId(), resultCapabilities.get(1).getStatus().getStatusId());
		assertEquals(capabilityThirth.getStatus().getValidityPeriod(), resultCapabilities.get(1).getStatus().getValidityPeriod());
		assertEquals(capabilityThirth.getParentCapabilityId(), resultCapabilities.get(1).getParentCapabilityId());
		assertEquals(capabilityThirth.getCapabilityName(), resultCapabilities.get(1).getCapabilityName());
		assertEquals(capabilityThirth.getLevel(), resultCapabilities.get(1).getLevel());
		assertEquals(capabilityThirth.isPaceOfChange(), resultCapabilities.get(1).isPaceOfChange());
		assertEquals(capabilityThirth.getTargetOperatingModel(), resultCapabilities.get(1).getTargetOperatingModel());
		assertEquals(capabilityThirth.getResourceQuality(), resultCapabilities.get(1).getResourceQuality());
		assertEquals(capabilityThirth.getInformationQuality(), resultCapabilities.get(1).getInformationQuality());
		assertEquals(capabilityThirth.getApplicationFit(), resultCapabilities.get(1).getApplicationFit());
	}
	
	@Test
	public void should_getCapabilities_whenGetAllCapabilities() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<CapabilityDto> resultCapabilities = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<CapabilityDto>>() {});
		
		assertNotNull(resultCapabilities);
		assertEquals(3, resultCapabilities.size());
		assertEquals(capabilityFirst.getCapabilityId(), resultCapabilities.get(0).getCapabilityId());
		assertEquals(capabilityFirst.getEnvironment().getEnvironmentId(), resultCapabilities.get(0).getEnvironment().getEnvironmentId());
		assertEquals(capabilityFirst.getEnvironment().getEnvironmentName(), resultCapabilities.get(0).getEnvironment().getEnvironmentName());
		assertEquals(capabilityFirst.getStatus().getStatusId(), resultCapabilities.get(0).getStatus().getStatusId());
		assertEquals(capabilityFirst.getStatus().getValidityPeriod(), resultCapabilities.get(0).getStatus().getValidityPeriod());
		assertEquals(capabilityFirst.getParentCapabilityId(), resultCapabilities.get(0).getParentCapabilityId());
		assertEquals(capabilityFirst.getCapabilityName(), resultCapabilities.get(0).getCapabilityName());
		assertEquals(capabilityFirst.getLevel(), resultCapabilities.get(0).getLevel());
		assertEquals(capabilityFirst.isPaceOfChange(), resultCapabilities.get(0).isPaceOfChange());
		assertEquals(capabilityFirst.getTargetOperatingModel(), resultCapabilities.get(0).getTargetOperatingModel());
		assertEquals(capabilityFirst.getResourceQuality(), resultCapabilities.get(0).getResourceQuality());
		assertEquals(capabilityFirst.getInformationQuality(), resultCapabilities.get(0).getInformationQuality());
		assertEquals(capabilityFirst.getApplicationFit(), resultCapabilities.get(0).getApplicationFit());
		assertEquals(capabilitySecond.getCapabilityId(), resultCapabilities.get(1).getCapabilityId());
		assertEquals(capabilitySecond.getEnvironment().getEnvironmentId(), resultCapabilities.get(1).getEnvironment().getEnvironmentId());
		assertEquals(capabilitySecond.getEnvironment().getEnvironmentName(), resultCapabilities.get(1).getEnvironment().getEnvironmentName());
		assertEquals(capabilitySecond.getStatus().getStatusId(), resultCapabilities.get(1).getStatus().getStatusId());
		assertEquals(capabilitySecond.getStatus().getValidityPeriod(), resultCapabilities.get(1).getStatus().getValidityPeriod());
		assertEquals(capabilitySecond.getParentCapabilityId(), resultCapabilities.get(1).getParentCapabilityId());
		assertEquals(capabilitySecond.getCapabilityName(), resultCapabilities.get(1).getCapabilityName());
		assertEquals(capabilitySecond.getLevel(), resultCapabilities.get(1).getLevel());
		assertEquals(capabilitySecond.isPaceOfChange(), resultCapabilities.get(1).isPaceOfChange());
		assertEquals(capabilitySecond.getTargetOperatingModel(), resultCapabilities.get(1).getTargetOperatingModel());
		assertEquals(capabilitySecond.getResourceQuality(), resultCapabilities.get(1).getResourceQuality());
		assertEquals(capabilitySecond.getInformationQuality(), resultCapabilities.get(1).getInformationQuality());
		assertEquals(capabilitySecond.getApplicationFit(), resultCapabilities.get(1).getApplicationFit());		
		assertEquals(capabilityThirth.getCapabilityId(), resultCapabilities.get(2).getCapabilityId());
		assertEquals(capabilityThirth.getEnvironment().getEnvironmentId(), resultCapabilities.get(2).getEnvironment().getEnvironmentId());
		assertEquals(capabilityThirth.getEnvironment().getEnvironmentName(), resultCapabilities.get(2).getEnvironment().getEnvironmentName());
		assertEquals(capabilityThirth.getStatus().getStatusId(), resultCapabilities.get(2).getStatus().getStatusId());
		assertEquals(capabilityThirth.getStatus().getValidityPeriod(), resultCapabilities.get(2).getStatus().getValidityPeriod());
		assertEquals(capabilityThirth.getParentCapabilityId(), resultCapabilities.get(2).getParentCapabilityId());
		assertEquals(capabilityThirth.getCapabilityName(), resultCapabilities.get(2).getCapabilityName());
		assertEquals(capabilityThirth.getLevel(), resultCapabilities.get(2).getLevel());
		assertEquals(capabilityThirth.isPaceOfChange(), resultCapabilities.get(2).isPaceOfChange());
		assertEquals(capabilityThirth.getTargetOperatingModel(), resultCapabilities.get(2).getTargetOperatingModel());
		assertEquals(capabilityThirth.getResourceQuality(), resultCapabilities.get(2).getResourceQuality());
		assertEquals(capabilityThirth.getInformationQuality(), resultCapabilities.get(2).getInformationQuality());
		assertEquals(capabilityThirth.getApplicationFit(), resultCapabilities.get(2).getApplicationFit());
	}
	
	@Test
	public void should_getBoolean_whenCapabilityIdExists() throws Exception {
		Integer capabilityId = capabilityFirst.getCapabilityId();
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "exists-by-id/" + capabilityId))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("true"));	
	}
	
	@Test
	public void should_getBoolean_whenCapabilityNameExists() throws Exception {
		String capabilityName = capabilityFirst.getCapabilityName();
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "exists-by-capabilityname/" + capabilityName))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("true"));	
	}
	
	@Test
	public void should_putCapability_whenUpdateCapability() throws Exception {
		Integer capabilityId = capabilityFirst.getCapabilityId();
		Integer environmentId = environmentFirst.getEnvironmentId();
		Integer statusId = statusFirst.getStatusId();
		Integer parentCapabilityId = 1;
		String capabilityName = "Update test";
		String level = "THREE";
		String paceOfChange = "true";
		String targetOperatingModel = "Target Operating Model";
		Integer resourceQuality = 1;
		Integer informationQuality = 1;
		Integer applicationFit = 1;
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(PATH)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("capabilityId", capabilityId.toString())
				.param("environmentId", environmentId.toString())
				.param("statusId", statusId.toString())
				.param("parentCapabilityId", parentCapabilityId.toString())
				.param("capabilityName", capabilityName)
				.param("level", level)
				.param("paceOfChange", paceOfChange)
				.param("targetOperatingModel", targetOperatingModel)
				.param("resourceQuality", resourceQuality.toString())
				.param("informationQuality", informationQuality.toString())
				.param("applicationFit", applicationFit.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		CapabilityDto resultCapability = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), CapabilityDto.class);
		
		Capability capability = capabilityService.getCapabilityByCapabilityName(capabilityName);
		
		assertNotNull(resultCapability);
		assertEquals(capability.getCapabilityId(), resultCapability.getCapabilityId());
		assertEquals(capability.getEnvironment().getEnvironmentId(), resultCapability.getEnvironment().getEnvironmentId());
		assertEquals(capability.getEnvironment().getEnvironmentName(), resultCapability.getEnvironment().getEnvironmentName());
		assertEquals(capability.getStatus().getStatusId(), resultCapability.getStatus().getStatusId());
		assertEquals(capability.getStatus().getValidityPeriod(), resultCapability.getStatus().getValidityPeriod());
		assertEquals(capability.getParentCapabilityId(), resultCapability.getParentCapabilityId());
		assertEquals(capability.getCapabilityName(), resultCapability.getCapabilityName());
		assertEquals(capability.getLevel(), resultCapability.getLevel());
		assertEquals(capability.isPaceOfChange(), resultCapability.isPaceOfChange());
		assertEquals(capability.getTargetOperatingModel(), resultCapability.getTargetOperatingModel());
		assertEquals(capability.getResourceQuality(), resultCapability.getResourceQuality());
		assertEquals(capability.getInformationQuality(), resultCapability.getInformationQuality());
		assertEquals(capability.getApplicationFit(), resultCapability.getApplicationFit());
	}
	
	@Test
	public void should_deleteCapability_whenDeleteCapability() throws Exception {
		Integer capabilityId = capabilityFirst.getCapabilityId();
		
		mockMvc.perform(MockMvcRequestBuilders.delete(PATH + capabilityId))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}
}
