package com.bavostepbros.leap.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.bavostepbros.leap.domain.customexceptions.EnumException;
import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.paceofchange.PaceOfChange;
import com.bavostepbros.leap.domain.model.targetoperatingmodel.TargetOperatingModel;
import com.bavostepbros.leap.domain.service.capabilityservice.CapabilityService;
import com.bavostepbros.leap.domain.service.environmentservice.EnvironmentService;
import com.bavostepbros.leap.domain.service.statusservice.StatusService;
import com.bavostepbros.leap.persistence.BusinessProcessDAL;
import com.bavostepbros.leap.persistence.CapabilityDAL;
import com.bavostepbros.leap.persistence.EnvironmentDAL;
import com.bavostepbros.leap.persistence.ProgramDAL;
import com.bavostepbros.leap.persistence.ProjectDAL;
import com.bavostepbros.leap.persistence.ResourceDAL;
import com.bavostepbros.leap.persistence.StatusDAL;

@AutoConfigureMockMvc
@SpringBootTest
public class CapabilityServiceTest {

	@Autowired
	private CapabilityService capabilityService;

	@MockBean
	private EnvironmentService environmentService;

	@MockBean
	private StatusService statusService;

	@MockBean
	private CapabilityDAL capabilityDAL;

	@MockBean
	private StatusDAL statusDAL;

	@MockBean
	private EnvironmentDAL environmentDAL;
	
	@MockBean
	private BusinessProcessDAL businessProcessDAL;
	
	@MockBean
	private ProjectDAL projectDAL;
	
	@MockBean
	private ProgramDAL programDAL;
	
	@MockBean
	private ResourceDAL resourceDAL;

	@SpyBean
	private CapabilityService spyCapabilityService;

	@SpyBean
	private StatusService spyStatusService;

	@SpyBean
	private EnvironmentService spyEnvironmentService;

	private Capability capabilityFirst;
	private Capability capabilitySecond;
	private Status status;
	private Environment environment;
	private List<Capability> capabilities;
	private Optional<Status> optionalStatus;
	private Optional<Capability> optionalCapabilityFirst;
	private Optional<Environment> optionalEnvironment;
	
	private Integer capabilityId;
	private Integer parentCapabilityId;
	private String capabilityName;
	private String capabilityDescription;
	private String paceOfChange;
	private String targetOperatingModel;
	private Integer resourceQuality;
	private Double informationQuality;
	private Double applicationFit;
	private Integer statusId;
	private Integer environmentId;

	@BeforeEach
	public void init() {
		status = new Status(1, LocalDate.of(2021, 5, 9));
		environment = new Environment(1, "Environment test");
		capabilityFirst = new Capability(1, environment, status, 0, "Capability 1", "Description 1",
				PaceOfChange.DIFFERENTIATION, TargetOperatingModel.COORDINATION, 10, 2.0, 3.0);
		capabilitySecond = new Capability(2, environment, status, capabilityFirst.getCapabilityId(), "Capability 2",
				"Description 2", PaceOfChange.DIFFERENTIATION, TargetOperatingModel.COORDINATION, 10, 2.0, 3.0);
		capabilityService.updateLevel(capabilityFirst);
		capabilities = List.of(capabilityFirst, capabilitySecond);
		optionalStatus = Optional.of(status);
		optionalCapabilityFirst = Optional.of(capabilityFirst);
		optionalEnvironment = Optional.of(environment);
		
		capabilityId = capabilityFirst.getCapabilityId();
		parentCapabilityId = capabilityFirst.getParentCapabilityId();
		capabilityName = capabilityFirst.getCapabilityName();
		capabilityDescription = capabilityFirst.getCapabilityDescription();
		paceOfChange = capabilityFirst.getPaceOfChange().toString();
		targetOperatingModel = capabilityFirst.getTargetOperatingModel().toString();
		resourceQuality = capabilityFirst.getResourceQuality();
		informationQuality = capabilityFirst.getInformationQuality();
		applicationFit = capabilityFirst.getApplicationFit();
		statusId = status.getStatusId();
		environmentId = environment.getEnvironmentId();
	}

	@Test
	void should_notBeNull() {
		assertNotNull(capabilityService);
		assertNotNull(environmentService);
		assertNotNull(statusService);
		assertNotNull(statusDAL);
		assertNotNull(environmentDAL);
		assertNotNull(capabilityDAL);
		
		assertNotNull(status);
		assertNotNull(environment);
		assertNotNull(capabilityFirst);
		assertNotNull(capabilities);
		assertNotNull(optionalStatus);
		assertNotNull(optionalEnvironment);
		assertNotNull(optionalCapabilityFirst);
		
		assertNotNull(capabilityId);
		assertNotNull(parentCapabilityId);
		assertNotNull(capabilityName);
		assertNotNull(capabilityDescription);
		assertNotNull(paceOfChange);
		assertNotNull(targetOperatingModel);
		assertNotNull(resourceQuality);
		assertNotNull(informationQuality);
		assertNotNull(applicationFit);
		assertNotNull(statusId);
		assertNotNull(environmentId);
	}
	
	@Test
	void should_retrieveValidCapability_whenGetCapabilityNotFound() {
		String expectedErrorMessage = "Capability does not exist.";
		
		Exception exception = assertThrows(NullPointerException.class, 
				() -> capabilityService.get(capabilityId));
		
		assertEquals(expectedErrorMessage, exception.getMessage());
	}

	@Test //10
	void should_retrieveValidCapability_whenGetCapability() {
		BDDMockito.doReturn(true).when(spyCapabilityService).existsById(capabilityId);
		BDDMockito.given(capabilityDAL.findById(capabilityId)).willReturn(optionalCapabilityFirst);
		
		Capability fetchedCapability = capabilityService.get(capabilityId);

		assertNotNull(fetchedCapability);
		assertTrue(fetchedCapability instanceof Capability);
		testCapability(capabilityFirst, fetchedCapability);
	}

	@Test 
	void should_throwNullPointerException_whenGetCapabilitiesByEnvironmentIdInvalidEnvironmentId() {
		BDDMockito.given(environmentDAL.findById(BDDMockito.anyInt())).willReturn(Optional.empty());
		String expectedErrorMessage = "Environment does not exist.";
		
		Exception exception = assertThrows(NullPointerException.class, 
				() -> capabilityService.getCapabilitiesByEnvironment(environmentId));
		
		assertEquals(expectedErrorMessage, exception.getMessage());
	}

	@Test
	void should_throwEnumException_whenGetCapabilitiesByLevelInputIsInvalid() {
		String level = "FOUR";
		String expected = "CapabilityLevel is not valid.";

		Exception exception = assertThrows(EnumException.class, () -> capabilityService.getCapabilitiesByLevel(level));

		assertEquals(exception.getMessage(), expected);
	}

	@Test //36
	void should_throwEnumException_whenGetCapabilitiesByParentIdAndLevelInputIsInvalid() {
		Integer parentId = capabilitySecond.getParentCapabilityId();
		String level = "FOUR";
		String expected = "CapabilityLevel is not valid.";

		Exception exception = assertThrows(EnumException.class,
				() -> capabilityService.getCapabilitiesByParentIdAndLevel(parentId, level));

		assertEquals(exception.getMessage(), expected);
	}

	@Test //37
	void should_ReturnFalse_whenCapabilityDoesNotExistById() {
		BDDMockito.given(capabilityDAL.existsById(BDDMockito.anyInt())).willReturn(false);

		boolean result = capabilityService.existsById(capabilityFirst.getCapabilityId());

		assertFalse(result);
	}

	@Test //38
	void should_ReturnTrue_whenCapabilityDoesExistById() {
		BDDMockito.given(capabilityDAL.existsById(BDDMockito.anyInt())).willReturn(true);

		boolean result = capabilityService.existsById(capabilityFirst.getCapabilityId());

		assertTrue(result);
	}

	@Test //39
	void should_ReturnTrue_whenCapabilityDoesExistByCapabilityName() {
		BDDMockito.given(capabilityDAL.findByCapabilityName(BDDMockito.any(String.class)))
				.willReturn(optionalCapabilityFirst);

		boolean result = capabilityService.existsByCapabilityName(capabilityFirst.getCapabilityName());

		assertTrue(result);
	}

	@Test //40
	void should_ReturnFalse_whenCapabilityDoesNotExistByCapabilityName() {
		BDDMockito.given(capabilityDAL.findByCapabilityName(BDDMockito.any(String.class))).willReturn(Optional.empty());

		boolean result = capabilityService.existsByCapabilityName(capabilityFirst.getCapabilityName());

		assertFalse(result);
	}
	
	@Test
	void should_retrieveValidCapability_whenGetCapabilityByNameNotFound() {
		String expectedErrorMessage = "Capability does not exist.";
		
		Exception exception = assertThrows(NullPointerException.class, 
				() -> capabilityService.getCapabilityByCapabilityName(capabilityName));
		
		assertEquals(expectedErrorMessage, exception.getMessage());
	}

	@Test
	void should_retrieveValidCapability_whenGetCapabilityByName() {
		BDDMockito.given(capabilityDAL.findById(capabilityId)).willReturn(optionalCapabilityFirst);
		BDDMockito.doReturn(true).when(spyCapabilityService).existsById(capabilityId);
		BDDMockito.given(capabilityDAL.findByCapabilityName(capabilityName)).willReturn(optionalCapabilityFirst);
		
		Capability fetchedCapability = capabilityService.getCapabilityByCapabilityName(capabilityName);

		assertNotNull(fetchedCapability);
		assertTrue(fetchedCapability instanceof Capability);
		testCapability(capabilityFirst, fetchedCapability);
	}
	
	private void testCapability(Capability expectedObject, Capability actualObject) {
		assertEquals(expectedObject.getCapabilityId(), actualObject.getCapabilityId());
		assertEquals(expectedObject.getStatus().getStatusId(), actualObject.getStatus().getStatusId());
		assertEquals(expectedObject.getStatus().getValidityPeriod(), actualObject.getStatus().getValidityPeriod());
		assertEquals(expectedObject.getEnvironment().getEnvironmentId(), actualObject.getEnvironment().getEnvironmentId());
		assertEquals(expectedObject.getEnvironment().getEnvironmentName(),
				actualObject.getEnvironment().getEnvironmentName());
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
