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
import com.bavostepbros.leap.domain.model.CapabilityApplication;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.ITApplication;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.capabilitylevel.CapabilityLevel;
import com.bavostepbros.leap.domain.model.dto.CapabilityApplicationDto;
import com.bavostepbros.leap.domain.service.capabilityapplicationservice.CapabilityApplicationService;
import com.bavostepbros.leap.persistence.CapabilityApplicationDAL;
import com.bavostepbros.leap.persistence.CapabilityDAL;
import com.bavostepbros.leap.persistence.EnvironmentDAL;
import com.bavostepbros.leap.persistence.ITApplicationDAL;
import com.bavostepbros.leap.persistence.StatusDAL;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class CapabilityApplicationControllerTest {

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
	private ITApplicationDAL itApplicationDAL;

	@Autowired
	private CapabilityApplicationDAL capabilityApplicationDAL;

	@Autowired
	private CapabilityApplicationService capabilityApplicationService;
	
	static final String PATH = "/api/capabilityapplication/";

	private Status statusFirst;
	private Status statusSecond;
	private Environment environmentFirst;
	private Environment environmentSecond;
	private Capability capabilityFirst;
	private Capability capabilitySecond;
	private ITApplication itApplicationFirst;
	private ITApplication itApplicationSecond;
	private CapabilityApplication capabilityApplicationFirst;
	private CapabilityApplication capabilityApplicationSecond;
	private CapabilityApplication capabilityApplicationThirth;

	@BeforeEach
	public void init() {
		statusFirst = statusDAL.save(new Status(1, LocalDate.of(2021, 05, 15)));
		statusSecond = statusDAL.save(new Status(2, LocalDate.of(2021, 05, 20)));
		environmentFirst = environmentDAL.save(new Environment(1, "Test 1"));
		environmentSecond = environmentDAL.save(new Environment(2, "Test 2"));
		capabilityFirst = capabilityDAL.save(new Capability(1, environmentFirst, statusFirst, 1, "Capability 1",
				CapabilityLevel.ONE, true, "Target Operating Model", 1, 1, 1));
		capabilitySecond = capabilityDAL
				.save(new Capability(2, environmentSecond, statusSecond, capabilityFirst.getCapabilityId(),
						"Capability 2", CapabilityLevel.TWO, true, "Target Operating Model", 1, 1, 1));
		itApplicationFirst = itApplicationDAL.save(new ITApplication(1, statusFirst, "application 1", "1.20.1",
				LocalDate.of(2021, 01, 20), LocalDate.of(2025, 05, 20), 1, 2, 3, 4, 5, 6, 7, 8, "EUR", 1000.0, 100.0,
				70.0, 100.0, LocalDate.of(2021, 05, 20)));
		itApplicationSecond = itApplicationDAL.save(new ITApplication(2, statusSecond, "application 2", "1.20.1",
				LocalDate.of(2021, 01, 20), LocalDate.of(2025, 05, 20), 2, 3, 4, 5, 6, 7, 8, 9, "EUR", 1000.0, 100.0,
				70.0, 100.0, LocalDate.of(2021, 05, 20)));
		capabilityApplicationFirst = capabilityApplicationDAL
				.save(new CapabilityApplication(capabilityFirst, itApplicationFirst, 0, 1, 2, 3, 4, 5, 4, 3));
		capabilityApplicationSecond = capabilityApplicationDAL
				.save(new CapabilityApplication(capabilitySecond, itApplicationFirst, 0, 2, 3, 4, 5, 4, 3, 2));
		capabilityApplicationThirth = capabilityApplicationDAL
				.save(new CapabilityApplication(capabilitySecond, itApplicationSecond, 0, 5, 4, 3, 2, 1, 2, 3));
	}
	
	@AfterEach
	public void close() {
		statusDAL.delete(statusFirst);
		statusDAL.delete(statusSecond);
		environmentDAL.delete(environmentFirst);
		environmentDAL.delete(environmentSecond);
		capabilityDAL.delete(capabilityFirst);
		capabilityDAL.delete(capabilitySecond);
		itApplicationDAL.delete(itApplicationFirst);
		itApplicationDAL.delete(itApplicationSecond);
		capabilityApplicationDAL.delete(capabilityApplicationFirst);
		capabilityApplicationDAL.delete(capabilityApplicationSecond);
		capabilityApplicationDAL.delete(capabilityApplicationThirth);
	}
	
	@Test
	void should_notBeNull() {
		assertNotNull(statusDAL);
		assertNotNull(environmentDAL);
		assertNotNull(capabilityDAL);
		assertNotNull(itApplicationDAL);
		assertNotNull(capabilityApplicationDAL);
		assertNotNull(capabilityApplicationService);
		assertNotNull(statusFirst);
		assertNotNull(statusSecond);
		assertNotNull(environmentFirst);
		assertNotNull(environmentSecond);
		assertNotNull(capabilityFirst);
		assertNotNull(capabilitySecond);
		assertNotNull(itApplicationFirst);
		assertNotNull(itApplicationSecond);
		assertNotNull(capabilityApplicationFirst);
		assertNotNull(capabilityApplicationSecond);
		assertNotNull(capabilityApplicationThirth);
	}
	
	@Test
	public void should_postCapabilityApplication_whenSaveCapabilityApplication() throws Exception {
		Integer capabilityId = capabilityFirst.getCapabilityId();
		Integer applicationId = itApplicationSecond.getItApplicationId();
		Integer efficiencySupport = capabilityApplicationFirst.getEfficiencySupport();
		Integer functionalCoverage = capabilityApplicationFirst.getFunctionalCoverage();
		Integer correctnessBusinessFit = capabilityApplicationFirst.getCorrectnessBusinessFit();
		Integer futurePotential = capabilityApplicationFirst.getFuturePotential();
		Integer completeness = capabilityApplicationFirst.getCompleteness();
		Integer correctnessInformationFit = capabilityApplicationFirst.getCorrectnessInformationFit();
		Integer availability = capabilityApplicationFirst.getAvailability();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(PATH + capabilityId + "/" + applicationId)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("efficiencySupport", efficiencySupport.toString())
				.param("functionalCoverage", functionalCoverage.toString())
				.param("correctnessBusinessFit", correctnessBusinessFit.toString())
				.param("futurePotential", futurePotential.toString())
				.param("completeness", completeness.toString())
				.param("correctnessInformationFit", correctnessInformationFit.toString())
				.param("availability", availability.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		CapabilityApplicationDto capabilityApplicationDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), CapabilityApplicationDto.class);
		
		CapabilityApplication capabilityApplication = capabilityApplicationService.get(capabilityId, applicationId);
		
		assertNotNull(capabilityApplicationDto);
		testCapabilityApplication(capabilityApplication, capabilityApplicationDto);
	}
	
	@Test
	public void should_getCapabilityApplication_whenGetCapabilityApplication() throws Exception {
		Integer capabilityId = capabilityApplicationFirst.getCapability().getCapabilityId();
		Integer applicationId = capabilityApplicationFirst.getApplication().getItApplicationId();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH + capabilityId + "/" + applicationId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		CapabilityApplicationDto capabilityApplicationDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), CapabilityApplicationDto.class);
		
		assertNotNull(capabilityApplicationDto);
		testCapabilityApplication(capabilityApplicationFirst, capabilityApplicationDto);
	}
	
	@Test
	public void should_putCapabilityApplication_whenUpdateCapabilityApplication() throws Exception {
		Integer capabilityId = capabilityApplicationFirst.getCapability().getCapabilityId();
		Integer applicationId = capabilityApplicationFirst.getApplication().getItApplicationId();
		Integer efficiencySupport = 5;
		Integer functionalCoverage = capabilityApplicationFirst.getFunctionalCoverage();
		Integer correctnessBusinessFit = capabilityApplicationFirst.getCorrectnessBusinessFit();
		Integer futurePotential = capabilityApplicationFirst.getFuturePotential();
		Integer completeness = capabilityApplicationFirst.getCompleteness();
		Integer correctnessInformationFit = capabilityApplicationFirst.getCorrectnessInformationFit();
		Integer availability = capabilityApplicationFirst.getAvailability();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(PATH + capabilityId + "/" + applicationId)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("efficiencySupport", efficiencySupport.toString())
				.param("functionalCoverage", functionalCoverage.toString())
				.param("correctnessBusinessFit", correctnessBusinessFit.toString())
				.param("futurePotential", futurePotential.toString())
				.param("completeness", completeness.toString())
				.param("correctnessInformationFit", correctnessInformationFit.toString())
				.param("availability", availability.toString())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		CapabilityApplicationDto capabilityApplicationDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), CapabilityApplicationDto.class);
		
		CapabilityApplication capabilityApplication = capabilityApplicationService.get(capabilityId, applicationId);
		
		assertNotNull(capabilityApplicationDto);
		testCapabilityApplication(capabilityApplication, capabilityApplicationDto);
	}
	
	@Test
	public void should_deleteCapabilityApplication_whenDeleteCapabilityApplication() throws Exception {
		Integer capabilityId = capabilityApplicationFirst.getCapability().getCapabilityId();
		Integer applicationId = capabilityApplicationFirst.getApplication().getItApplicationId();
		
		mockMvc.perform(MockMvcRequestBuilders.delete(PATH + capabilityId + "/" + applicationId))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void should_getCapabilityApplications_whenGetlAllByCapabilityIdCapabilityApplication() throws Exception {
		Integer capabilityId = capabilityApplicationSecond.getCapability().getCapabilityId();
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(PATH + "all-capabilityApplications-by-capabilityid/" + capabilityId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
		
		List<CapabilityApplicationDto> capabilityApplicationDto = objectMapper.readValue(
				mvcResult.getResponse().getContentAsString(), new TypeReference<List<CapabilityApplicationDto>>() {});
		
		assertNotNull(capabilityApplicationDto);
		assertEquals(2, capabilityApplicationDto.size());
		testCapabilityApplication(capabilityApplicationSecond, capabilityApplicationDto.get(0));
		testCapabilityApplication(capabilityApplicationThirth, capabilityApplicationDto.get(1));
	}
	
	@Test
	private void testCapabilityApplication(CapabilityApplication expectedObject, CapabilityApplicationDto actualObject) {
		assertEquals(expectedObject.getCapability().getCapabilityId(), actualObject.getCapability().getCapabilityId());
		assertEquals(expectedObject.getCapability().getEnvironment().getEnvironmentId(), actualObject.getCapability().getEnvironment().getEnvironmentId());
		assertEquals(expectedObject.getCapability().getEnvironment().getEnvironmentName(), actualObject.getCapability().getEnvironment().getEnvironmentName());
		assertEquals(expectedObject.getCapability().getStatus().getStatusId(), actualObject.getCapability().getStatus().getStatusId());
		assertEquals(expectedObject.getCapability().getStatus().getValidityPeriod(), actualObject.getCapability().getStatus().getValidityPeriod());
		assertEquals(expectedObject.getCapability().getParentCapabilityId(), actualObject.getCapability().getParentCapabilityId());
		assertEquals(expectedObject.getCapability().getCapabilityName(), actualObject.getCapability().getCapabilityName());
		assertEquals(expectedObject.getCapability().getLevel(), actualObject.getCapability().getLevel());
		assertEquals(expectedObject.getCapability().isPaceOfChange(), actualObject.getCapability().isPaceOfChange());
		assertEquals(expectedObject.getCapability().getTargetOperatingModel(), actualObject.getCapability().getTargetOperatingModel());
		assertEquals(expectedObject.getCapability().getResourceQuality(), actualObject.getCapability().getResourceQuality());
		assertEquals(expectedObject.getCapability().getInformationQuality(), actualObject.getCapability().getInformationQuality());
		assertEquals(expectedObject.getCapability().getApplicationFit(), actualObject.getCapability().getApplicationFit());
		
		assertEquals(expectedObject.getApplication().getItApplicationId(), actualObject.getApplication().getItApplicationId());
		assertEquals(expectedObject.getApplication().getStatus().getStatusId(), actualObject.getApplication().getStatus().getStatusId());
		assertEquals(expectedObject.getApplication().getStatus().getValidityPeriod(), actualObject.getApplication().getStatus().getValidityPeriod());
		assertEquals(expectedObject.getApplication().getName(), actualObject.getApplication().getName());
		assertEquals(expectedObject.getApplication().getVersion(), actualObject.getApplication().getVersion());
		assertEquals(expectedObject.getApplication().getPurchaseDate(), actualObject.getApplication().getPurchaseDate());
		assertEquals(expectedObject.getApplication().getEndOfLife(), actualObject.getApplication().getEndOfLife());
		assertEquals(expectedObject.getApplication().getCurrentScalability(), actualObject.getApplication().getCurrentScalability());
		assertEquals(expectedObject.getApplication().getExpectedScalability(), actualObject.getApplication().getExpectedScalability());
		assertEquals(expectedObject.getApplication().getCurrentPerformance(), actualObject.getApplication().getCurrentPerformance());
		assertEquals(expectedObject.getApplication().getExpectedPerformance(), actualObject.getApplication().getExpectedPerformance());
		assertEquals(expectedObject.getApplication().getCurrentSecurityLevel(), actualObject.getApplication().getCurrentSecurityLevel());
		assertEquals(expectedObject.getApplication().getExpectedSecurityLevel(), actualObject.getApplication().getExpectedSecurityLevel());
		assertEquals(expectedObject.getApplication().getCurrentStability(), actualObject.getApplication().getCurrentStability());
		assertEquals(expectedObject.getApplication().getExpectedStability(), actualObject.getApplication().getExpectedStability());
		assertEquals(expectedObject.getApplication().getCurrencyType(), actualObject.getApplication().getCurrencyType());
		assertEquals(expectedObject.getApplication().getCostCurrency(), actualObject.getApplication().getCostCurrency());
		assertEquals(expectedObject.getApplication().getCurrentValue(), actualObject.getApplication().getCurrentValue());
		assertEquals(expectedObject.getApplication().getCurrentYearlyCost(), actualObject.getApplication().getCurrentYearlyCost());
		assertEquals(expectedObject.getApplication().getAcceptedYearlyCost(), actualObject.getApplication().getAcceptedYearlyCost());
		assertEquals(expectedObject.getApplication().getTimeValue(), actualObject.getApplication().getTimeValue());
		
		assertEquals(expectedObject.getImportance(), actualObject.getImportance());
		assertEquals(expectedObject.getEfficiencySupport(), actualObject.getEfficiencySupport());
		assertEquals(expectedObject.getFunctionalCoverage(), actualObject.getFunctionalCoverage());
		assertEquals(expectedObject.getCorrectnessBusinessFit(), actualObject.getCorrectnessBusinessFit());
		assertEquals(expectedObject.getFuturePotential(), actualObject.getFuturePotential());
		assertEquals(expectedObject.getCompleteness(), actualObject.getCompleteness());
		assertEquals(expectedObject.getCorrectnessInformationFit(), actualObject.getCorrectnessInformationFit());
		assertEquals(expectedObject.getAvailability(), actualObject.getAvailability());
	}
}
