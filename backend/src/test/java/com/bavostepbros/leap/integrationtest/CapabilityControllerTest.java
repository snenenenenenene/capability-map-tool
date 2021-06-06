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

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.dto.CapabilityDto;
import com.bavostepbros.leap.domain.model.paceofchange.PaceOfChange;
import com.bavostepbros.leap.domain.model.targetoperatingmodel.TargetOperatingModel;
import com.bavostepbros.leap.domain.service.capabilityservice.CapabilityService;
import com.bavostepbros.leap.persistence.CapabilityDAL;
import com.bavostepbros.leap.persistence.EnvironmentDAL;
import com.bavostepbros.leap.persistence.StatusDAL;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

	@Autowired
	private RequestFactory requestFactory;

	private String jwt;
	private Status statusFirst;
	private Status statusSecond;
	private Environment environmentFirst;
	private Environment environmentSecond;
	private Capability capabilityFirst;
	private Capability capabilitySecond;
	private Capability capabilityThird;
	
	static final String PATH = "/api/capability/";

	@BeforeAll
	public void authenticate() throws Exception {
		jwt = mockMvc.perform(MockMvcRequestBuilders.post("/api/user/authenticate")
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("email", "super_admin")
				.param("password", "super_admin"))
				.andReturn()
				.getResponse().getContentAsString();
	}


	@BeforeEach
	public void init() {
		statusFirst = statusDAL.save(new Status(1, LocalDate.of(2021, 5, 15)));
		statusSecond = statusDAL.save(new Status(2, LocalDate.of(2021, 5, 20)));
		environmentFirst = environmentDAL.save(new Environment(1, "Test 1"));
		environmentSecond = environmentDAL.save(new Environment(2, "Test 2"));
		capabilityFirst = capabilityDAL.save(new Capability(1, environmentFirst,
				statusFirst, 0, "Capability 1", PaceOfChange.DIFFERENTIATION,
				TargetOperatingModel.COORDINATION, 1, 1, 1));
		capabilitySecond = capabilityDAL.save(new Capability(2, environmentFirst,
				statusFirst, capabilityFirst.getCapabilityId(), "Capability 2",
				PaceOfChange.INNOVATIVE, TargetOperatingModel.DIVERSIFICATION, 
				1, 1, 1));
		capabilityThird = capabilityDAL.save(new Capability(3, environmentSecond,
				statusSecond, capabilityFirst.getCapabilityId(), "Capability 3", 
				PaceOfChange.STANDARD, TargetOperatingModel.REPLICATION, 1, 1, 1));
	}
	
	@AfterEach
	public void close() {
		statusDAL.delete(statusFirst);
		statusDAL.delete(statusSecond);
		environmentDAL.delete(environmentFirst);
		environmentDAL.delete(environmentSecond);
		capabilityDAL.delete(capabilityFirst);
		capabilityDAL.delete(capabilitySecond);
		capabilityDAL.delete(capabilityThird);
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
		Integer parentCapabilityId = 0;
		String capabilityName = "Posttest haha";
		String paceOfChange = PaceOfChange.DIFFERENTIATION.toString();
		String targetOperatingModel = TargetOperatingModel.COORDINATION.toString();
		Integer resourceQuality = 1;
		Integer informationQuality = 1;
		Integer applicationFit = 1;
		
		MvcResult mvcResult = mockMvc.perform(requestFactory.buildRequest(PATH, jwt)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("environmentId", environmentId.toString())
				.param("statusId", statusId.toString())
				.param("parentCapabilityId", parentCapabilityId.toString())
				.param("capabilityName", capabilityName)
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
		testCapability(capability, resultCapability);
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
		testCapability(capabilityFirst, resultCapability);
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
		testCapability(capabilityFirst, resultCapability);
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
		testCapability(capabilityFirst, resultCapabilities.get(0));
		testCapability(capabilitySecond, resultCapabilities.get(1));
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
		testCapability(capabilityFirst, resultCapabilities.get(0));
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
		testCapability(capabilitySecond, resultCapabilities.get(0));
		testCapability(capabilityThird, resultCapabilities.get(1));
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
		testCapability(capabilitySecond, resultCapabilities.get(0));
		testCapability(capabilityThird, resultCapabilities.get(1));
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
		testCapability(capabilityFirst, resultCapabilities.get(0));
		testCapability(capabilitySecond, resultCapabilities.get(1));
		testCapability(capabilityThird, resultCapabilities.get(2));
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
		Integer parentCapabilityId = 0;
		String capabilityName = "Update test";
		String level = "THREE";
		String paceOfChange = PaceOfChange.DIFFERENTIATION.toString();
		String targetOperatingModel = TargetOperatingModel.COORDINATION.toString();
		Integer resourceQuality = 1;
		Integer informationQuality = 1;
		Integer applicationFit = 1;
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(PATH + capabilityId)
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
		testCapability(capability, resultCapability);
	}
	
	@Test
	public void should_deleteCapability_whenDeleteCapability() throws Exception {
		Integer capabilityId = capabilityFirst.getCapabilityId();
		
		mockMvc.perform(MockMvcRequestBuilders.delete(PATH + capabilityId))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	private void testCapability(Capability expectedObject, CapabilityDto actualObject) {
		assertEquals(expectedObject.getCapabilityId(), actualObject.getCapabilityId());
		assertEquals(expectedObject.getEnvironment().getEnvironmentId(), actualObject.getEnvironment().getEnvironmentId());
		assertEquals(expectedObject.getEnvironment().getEnvironmentName(), actualObject.getEnvironment().getEnvironmentName());
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
