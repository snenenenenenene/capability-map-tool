package com.bavostepbros.leap.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
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

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.CapabilityInformation;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Information;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.dto.CapabilityInformationDto;
import com.bavostepbros.leap.domain.model.paceofchange.PaceOfChange;
import com.bavostepbros.leap.domain.model.strategicimportance.StrategicImportance;
import com.bavostepbros.leap.domain.model.targetoperatingmodel.TargetOperatingModel;
import com.bavostepbros.leap.domain.service.capabilityinformationservice.CapabilityInformationService;
import com.bavostepbros.leap.persistence.CapabilityDAL;
import com.bavostepbros.leap.persistence.CapabilityInformationDAL;
import com.bavostepbros.leap.persistence.EnvironmentDAL;
import com.bavostepbros.leap.persistence.InformationDAL;
import com.bavostepbros.leap.persistence.StatusDAL;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CapabilityInformationControllerTest extends ApiIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private CapabilityInformationDAL capabilityInformationDAL;

	@Autowired
	private StatusDAL statusDAL;

	@Autowired
	private EnvironmentDAL environmentDAL;

	@Autowired
	private CapabilityDAL capabilityDAL;

	@Autowired
	private InformationDAL informationDAL;
	
	@Autowired
	private CapabilityInformationService capabilityInformationService;

	static final String PATH = "/api/capabilityinformation/";

	private Status statusFirst;
	private Status statusSecond;
	private Environment environmentFirst;
	private Environment environmentSecond;
	private Capability capabilityFirst;
	private Capability capabilitySecond;
	private Information informationFirst;
	private Information informationSecond;
	private CapabilityInformation capabilityInformationFirst;
	private CapabilityInformation capabilityInformationSecond;

	@BeforeAll
	public void authenticate() throws Exception {
		super.authenticate();
	}

	@BeforeEach
	public void init() {
		statusFirst = statusDAL.save(new Status(1, LocalDate.of(2021, 05, 15)));
		statusSecond = statusDAL.save(new Status(2, LocalDate.of(2021, 05, 20)));
		environmentFirst = environmentDAL.save(new Environment(1, "Test 1"));
		environmentSecond = environmentDAL.save(new Environment(2, "Test 2"));
		capabilityFirst = capabilityDAL.save(new Capability(1, environmentFirst, statusFirst, 1, "Capability 1",
				"Description 1", PaceOfChange.DIFFERENTIATION, TargetOperatingModel.COORDINATION, 1, 1, 1));
		capabilitySecond = capabilityDAL.save(
				new Capability(2, environmentSecond, statusSecond, capabilityFirst.getCapabilityId(), "Capability 2",
						"Description 2", PaceOfChange.INNOVATIVE, TargetOperatingModel.DIVERSIFICATION, 1, 1, 1));
		informationFirst = informationDAL.save(new Information(1, "Information 1", "Description 1"));
		informationSecond = informationDAL.save(new Information(2, "Information 2", "Description 2"));
		capabilityInformationFirst = capabilityInformationDAL
				.save(new CapabilityInformation(capabilityFirst, informationFirst, StrategicImportance.HIGHEST));
		capabilityInformationSecond = capabilityInformationDAL
				.save(new CapabilityInformation(capabilitySecond, informationSecond, StrategicImportance.LOWEST));
	}
	
	@AfterEach
	public void close() {
		statusDAL.delete(statusFirst);
		statusDAL.delete(statusSecond);
		environmentDAL.delete(environmentFirst);
		environmentDAL.delete(environmentSecond);
		capabilityDAL.delete(capabilityFirst);
		capabilityDAL.delete(capabilitySecond);
		informationDAL.delete(informationFirst);
		informationDAL.delete(informationSecond);
		capabilityInformationDAL.delete(capabilityInformationFirst);
		capabilityInformationDAL.delete(capabilityInformationSecond);
	}
	
	@Test
	void should_notBeNull() {
		assertNotNull(statusDAL);
		assertNotNull(environmentDAL);
		assertNotNull(capabilityDAL);
		assertNotNull(informationDAL);
		assertNotNull(capabilityInformationDAL);
		assertNotNull(capabilityInformationService);
		
		assertNotNull(statusFirst);
		assertNotNull(statusSecond);
		assertNotNull(environmentFirst);
		assertNotNull(environmentSecond);
		assertNotNull(capabilityFirst);
		assertNotNull(capabilitySecond);
		assertNotNull(informationFirst);
		assertNotNull(informationSecond);
		assertNotNull(capabilityInformationFirst);
		assertNotNull(capabilityInformationSecond);
	}
	
	@Test
	public void should_returnCapabilityInformation_whenSaveCapabilityInformation() throws Exception {
		Integer capabilityId = capabilityInformationFirst.getCapability().getCapabilityId();
		Integer informationId = capabilityInformationFirst.getInformation().getInformationId();
		String criticality = capabilityInformationFirst.getCriticality().toString();
		
		MvcResult mvcResult = mockMvc.perform(post(PATH)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("capabilityId", capabilityId.toString())
				.param("informationId", informationId.toString())
				.param("criticality", criticality)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		CapabilityInformationDto capabilityInformationDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), CapabilityInformationDto.class);
		
		List<CapabilityInformation> capabilityInformation = capabilityInformationService.getCapabilityInformationByCapability(capabilityId);
		
		assertNotNull(capabilityInformationDto);
		testCapabilityInformation(capabilityInformation.get(0), capabilityInformationDto);
	}
	
	@Test
	public void should_returnCapabilityInformation_whenGetCapabilityInformationByCapabilityAndInformation() throws Exception {
		Integer capabilityId = capabilityInformationFirst.getCapability().getCapabilityId();
		Integer informationId = capabilityInformationFirst.getInformation().getInformationId();
		
		MvcResult mvcResult = mockMvc.perform(get(PATH + capabilityId + "/" + informationId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		CapabilityInformationDto capabilityInformationDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), CapabilityInformationDto.class);		
		
		assertNotNull(capabilityInformationDto);
		testCapabilityInformation(capabilityInformationFirst, capabilityInformationDto);
	}
	
	@Test
	public void should_returnCapabilityInformation_whenUpdateCapabilityInformation() throws Exception {
		Integer capabilityId = capabilityInformationFirst.getCapability().getCapabilityId();
		Integer informationId = capabilityInformationFirst.getInformation().getInformationId();
		String criticality = capabilityInformationFirst.getCriticality().toString();
		
		MvcResult mvcResult = mockMvc.perform(put(PATH + capabilityId + "/" + informationId)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("criticality", criticality)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		CapabilityInformationDto capabilityInformationDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), CapabilityInformationDto.class);
		
		List<CapabilityInformation> capabilityInformation = capabilityInformationService.getCapabilityInformationByCapability(capabilityId);
		
		assertNotNull(capabilityInformationDto);
		testCapabilityInformation(capabilityInformation.get(0), capabilityInformationDto);
	}
	
	@Test
	public void should_returnOk_whenDeleteCapabilityInformation() throws Exception {
		Integer capabilityId = capabilityInformationFirst.getCapability().getCapabilityId();
		Integer informationId = capabilityInformationFirst.getInformation().getInformationId();
		
		mockMvc.perform(delete(PATH + capabilityId + "/" + informationId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
	}
	
	@Test
	public void should_returnCapabilityInformation_whenGetCapabilityInformationByCapability() throws Exception {
		Integer capabilityId = capabilityInformationFirst.getCapability().getCapabilityId();
		
		MvcResult mvcResult = mockMvc.perform(get(PATH + "all-capabilityinformation-by-capabilityid/" + capabilityId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<CapabilityInformationDto> capabilityInformationDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<CapabilityInformationDto>>() {});		
		
		assertNotNull(capabilityInformationDto);
		assertEquals(1, capabilityInformationDto.size());
		testCapabilityInformation(capabilityInformationFirst, capabilityInformationDto.get(0));
	}
	
	@Test
	public void should_returnCapabilityInformation_whenGetCapabilityInformationByInformation() throws Exception {
		Integer informationId = capabilityInformationFirst.getInformation().getInformationId();
		
		MvcResult mvcResult = mockMvc.perform(get(PATH + "all-capabilityinformation-by-informationid/" + informationId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<CapabilityInformationDto> capabilityInformationDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<CapabilityInformationDto>>() {});		
		
		assertNotNull(capabilityInformationDto);
		assertEquals(1, capabilityInformationDto.size());
		testCapabilityInformation(capabilityInformationFirst, capabilityInformationDto.get(0));
	}
	
	private void testCapabilityInformation(CapabilityInformation expectedObject, CapabilityInformationDto actualObject) {
		assertEquals(expectedObject.getCapability().getCapabilityId(), actualObject.getCapability().getCapabilityId());
		assertEquals(expectedObject.getInformation().getInformationId(), actualObject.getInformation().getInformationId());
		assertEquals(expectedObject.getCriticality(), actualObject.getCriticality());
	}
}
