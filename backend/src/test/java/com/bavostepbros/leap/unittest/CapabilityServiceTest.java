package com.bavostepbros.leap.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.bavostepbros.leap.domain.customexceptions.EnumException;
import com.bavostepbros.leap.domain.model.BusinessProcess;
import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Program;
import com.bavostepbros.leap.domain.model.Project;
import com.bavostepbros.leap.domain.model.Resource;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.capabilitylevel.CapabilityLevel;
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

/**
 *
 * @author Bavo Van Meel
 *
 */
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
	private BusinessProcess businessProcessFirst;
	private Program programFirst;
	private Project projectFirst;
	private Resource resourceFirst;
	private List<Capability> capabilities;
	private Set<BusinessProcess> businessProcess;
	private Set<Project> projects;
	private Set<Resource> resources;
	private Optional<Status> optionalStatus;
	private Optional<Capability> optionalCapabilityFirst;
	private Optional<Environment> optionalEnvironment;
	private Optional<BusinessProcess> optionalBusinessProcessFirst;
	private Optional<Project> optionalProjectFirst;
	private Optional<Resource> optionalResourceFirst;
	
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
		businessProcessFirst = new BusinessProcess(1, "BP 1", "Description 1");
		programFirst = new Program(1, "Program 1");
		projectFirst = new Project(1, "Project 1", programFirst, status);
		resourceFirst = new Resource(1, "Resource 1", "Good description 1", 20.0);
		capabilities = List.of(capabilityFirst, capabilitySecond);
		businessProcess = Set.of(businessProcessFirst);
		projects = Set.of(projectFirst);
		resources = Set.of(resourceFirst);
		optionalStatus = Optional.of(status);
		optionalCapabilityFirst = Optional.of(capabilityFirst);
		optionalEnvironment = Optional.of(environment);
		optionalBusinessProcessFirst = Optional.of(businessProcessFirst);
		optionalProjectFirst = Optional.of(projectFirst);
		optionalResourceFirst = Optional.of(resourceFirst);
		
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

//	@Test //7
//	void should_saveCapability_whenCapabilityIsSaved() {
//		BDDMockito.given(statusDAL.findById(BDDMockito.anyInt())).willReturn(optionalStatus);
//		BDDMockito.given(environmentDAL.findById(BDDMockito.anyInt())).willReturn(optionalEnvironment);
//		BDDMockito.given(capabilityDAL.save(BDDMockito.any(Capability.class))).willReturn(capabilityFirst);
//
//		Capability result = capabilityService.save(environmentId, statusId, parentCapabilityId, capabilityName,
//				capabilityDescription, paceOfChange, targetOperatingModel, resourceQuality, informationQuality,
//				applicationFit);
//		// capabilityService.updateLevel(result);
//
//		assertNotNull(result);
//		assertTrue(result instanceof Capability);
//		testCapability(capabilityFirst, result);
//	}
	
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

	@Test //11
	void should_retrieveCapabilityList_whenGetAllIsCalled() {
		BDDMockito.given(capabilityDAL.findAll()).willReturn(capabilities);

		List<Capability> fetchedStrategies = capabilityService.getAll();

		assertNotNull(fetchedStrategies);
		assertEquals(capabilities.size(), fetchedStrategies.size());
	}

//	@Test
//	void should_throwEnumException_whenUpdateCapabilityLevelIsNotValid() {
//		Integer capabilityId = capability.getCapabilityId();
//		Integer parentCapabilityId = capability.getParentCapabilityId();
//		String capabilityName = capability.getCapabilityName();
//		boolean paceOfChange = capability.isPaceOfChange();
//		String targetOperatingModel = capability.getTargetOperatingModel();
//		Integer resourceQuality = capability.getResourceQuality();
//		Integer informationQuality = capability.getInformationQuality();
//		Integer applicationFit = capability.getApplicationFit();
//		Integer statusId = status.getStatusId();
//		Integer environmentId = environment.getEnvironmentId();
//		String expected = "CapabilityLevel is not valid.";
//
//		BDDMockito.doReturn(true).when(spyCapabilityService).existsById(capabilityId);
//		BDDMockito.doReturn(false).when(spyCapabilityService).existsByCapabilityName(capabilityName);
//		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);
//		BDDMockito.doReturn(true).when(spyEnvironmentService).existsById(environmentId);
//
//		BDDMockito.given(capabilityDAL.findById(BDDMockito.anyInt())).willReturn(optionalCapability);
//
//		Exception exception = assertThrows(EnumException.class,
//				() -> capabilityService.save(environmentId, statusId,
//						parentCapabilityId, capabilityName,
//						paceOfChange, targetOperatingModel, resourceQuality,
//						informationQuality, applicationFit));
//
//		assertEquals(expected, exception.getMessage());
//	}

//	@Test //19
//	void should_retrieveValidCapability_whenCapabilityIsUpdated() {
//		BDDMockito.doReturn(true).when(spyCapabilityService).existsById(capabilityId);
//		BDDMockito.doReturn(false).when(spyCapabilityService).existsByCapabilityName(capabilityName);
//		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);
//		BDDMockito.doReturn(true).when(spyEnvironmentService).existsById(environmentId);
//
//		BDDMockito.given(statusDAL.findById(BDDMockito.anyInt())).willReturn(optionalStatus);
//		BDDMockito.given(environmentDAL.findById(BDDMockito.anyInt())).willReturn(optionalEnvironment);
//		BDDMockito.given(capabilityDAL.save(capabilityFirst)).willReturn(capabilityFirst);
//		BDDMockito.given(capabilityDAL.findById(BDDMockito.anyInt())).willReturn(optionalCapability);
//
//		Capability fetchedCapability = capabilityService.update(capabilityId, environmentId, statusId,
//				parentCapabilityId, capabilityName, capabilityDescription, paceOfChange, targetOperatingModel,
//				resourceQuality, informationQuality, applicationFit);
//
//		assertNotNull(fetchedCapability);
//		assertTrue(fetchedCapability instanceof Capability);
//		testCapability(capabilityFirst, fetchedCapability);
//	}

	@Test 
	void should_throwNullPointerException_whenGetCapabilitiesByEnvironmentIdInvalidEnvironmentId() {
		BDDMockito.given(environmentDAL.findById(BDDMockito.anyInt())).willReturn(Optional.empty());
		String expectedErrorMessage = "Environment does not exist.";
		
		Exception exception = assertThrows(NullPointerException.class, 
				() -> capabilityService.getCapabilitiesByEnvironment(environmentId));
		
		assertEquals(expectedErrorMessage, exception.getMessage());
	}
	
	@Test 
	void should_returnCapabilities_whenGetCapabilitiesByEnvironment() {
		BDDMockito.given(environmentDAL.findById(BDDMockito.anyInt())).willReturn(optionalEnvironment);
		BDDMockito.given(capabilityDAL.findByEnvironment(BDDMockito.any(Environment.class))).willReturn(capabilities);
		
		List<Capability> capabilityResults = capabilityService.getCapabilitiesByEnvironment(environmentId);
		
		assertNotNull(capabilityResults);
		assertEquals(capabilities.size(), capabilityResults.size());
		testCapability(capabilityFirst, capabilityResults.get(0));
		testCapability(capabilitySecond, capabilityResults.get(1));
	}

	@Test
	void should_throwEnumException_whenGetCapabilitiesByLevelInputIsInvalid() {
		String level = "FOUR";
		String expected = "CapabilityLevel is not valid.";

		Exception exception = assertThrows(EnumException.class, () -> capabilityService.getCapabilitiesByLevel(level));

		assertEquals(exception.getMessage(), expected);
	}

	@Test 
	void should_retrieveValidCapabilities_whenGetCapabilitiesByLevel() {
		String level = "ONE";
		CapabilityLevel capabilityLevel = capabilityFirst.getLevel();

		BDDMockito.given(capabilityDAL.findByLevel(capabilityLevel)).willReturn(capabilities);
		List<Capability> fetchedCapablities = capabilityService.getCapabilitiesByLevel(level);

		assertNotNull(fetchedCapablities);
		assertEquals(capabilities.size(), fetchedCapablities.size());
	}

	@Test //32
	void should_retrieveValidCapabilities_whenGetCapabilityChildren() {
		Integer parentId = capabilitySecond.getParentCapabilityId();

		BDDMockito.doReturn(true).when(spyCapabilityService).existsById(parentId);

		BDDMockito.given(capabilityDAL.findByParentCapabilityId(parentId)).willReturn(capabilities);
		List<Capability> fetchedCapablities = capabilityService.getCapabilityChildren(parentId);

		assertNotNull(fetchedCapablities);
		assertEquals(capabilities.size(), fetchedCapablities.size());
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

	@Test
	void should_retrieveValidCapabilities_whenGetCapabilitiesByParentIdAndLevel() {
		Integer parentId = capabilityFirst.getParentCapabilityId();
		String level = "ONE";
		CapabilityLevel capabilityLevel = capabilityFirst.getLevel();		
		BDDMockito.given(capabilityDAL.findByParentCapabilityIdAndLevel(parentId, capabilityLevel)).willReturn(capabilities);
		
		List<Capability> fetchedCapablities = capabilityService.getCapabilitiesByParentIdAndLevel(parentId, level);

		assertNotNull(fetchedCapablities);
		assertEquals(capabilities.size(), fetchedCapablities.size());
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
		BDDMockito.doReturn(true).when(spyCapabilityService).existsById(capabilityId);
		BDDMockito.given(capabilityDAL.findByCapabilityName(capabilityName)).willReturn(optionalCapabilityFirst);
		
		Capability fetchedCapability = capabilityService.getCapabilityByCapabilityName(capabilityName);

		assertNotNull(fetchedCapability);
		assertTrue(fetchedCapability instanceof Capability);
		testCapability(capabilityFirst, fetchedCapability);
	}
	
	@Test 
	void should_returnCapability_whenAddBusinessProcess() {
		BDDMockito.given(businessProcessDAL.findById(BDDMockito.anyInt())).willReturn(optionalBusinessProcessFirst);
		BDDMockito.given(capabilityDAL.findById(capabilityFirst.getCapabilityId())).willReturn(optionalCapabilityFirst);
		BDDMockito.given(capabilityDAL.save(BDDMockito.any(Capability.class))).willReturn(capabilityFirst);
		
		Capability capability = capabilityService.addBusinessProcess(capabilityId, businessProcessFirst.getBusinessProcessId());
		
		assertNotNull(capability);
		assertTrue(capability instanceof Capability);
		testCapability(capabilityFirst, capability);
	}
	
	@Test 
	void should_verifyDeleted_whenDeleteBusinessProcess() {
		BDDMockito.given(businessProcessDAL.findById(BDDMockito.anyInt())).willReturn(optionalBusinessProcessFirst);
		BDDMockito.given(capabilityDAL.findById(capabilityFirst.getCapabilityId())).willReturn(optionalCapabilityFirst);
		
		capabilityService.deleteBusinessProcess(capabilityId, businessProcessFirst.getBusinessProcessId());
		
		Mockito.verify(capabilityDAL, Mockito.times(1)).save(capabilityFirst);
	}
	
	@Test 
	void should_returnCapabilities_whenGetAllBusinessProcessByCapabilityId() {
		BDDMockito.given(businessProcessDAL.findById(BDDMockito.anyInt())).willReturn(optionalBusinessProcessFirst);
		BDDMockito.given(capabilityDAL.findById(capabilityFirst.getCapabilityId())).willReturn(optionalCapabilityFirst);
		capabilityService.addBusinessProcess(capabilityId, businessProcessFirst.getBusinessProcessId());
		
		Set<BusinessProcess> businessProcessResult = capabilityService.getAllBusinessProcessByCapabilityId(capabilityId);
		
		assertNotNull(businessProcessResult);
		assertEquals(businessProcess.size(), businessProcessResult.size());
	}
	
	@Test 
	void should_returnCapability_whenAddProject() {
		BDDMockito.given(projectDAL.findById(BDDMockito.anyInt())).willReturn(optionalProjectFirst);
		BDDMockito.given(capabilityDAL.findById(capabilityFirst.getCapabilityId())).willReturn(optionalCapabilityFirst);
		BDDMockito.given(capabilityDAL.save(BDDMockito.any(Capability.class))).willReturn(capabilityFirst);
		
		Capability capability = capabilityService.addProject(capabilityId, projectFirst.getProjectId());
		
		assertNotNull(capability);
		assertTrue(capability instanceof Capability);
		testCapability(capabilityFirst, capability);
	}
	
	@Test 
	void should_verifyDeleted_whenDeleteProject() {
		BDDMockito.given(projectDAL.findById(BDDMockito.anyInt())).willReturn(optionalProjectFirst);
		BDDMockito.given(capabilityDAL.findById(capabilityFirst.getCapabilityId())).willReturn(optionalCapabilityFirst);
		
		capabilityService.deleteProject(capabilityId, projectFirst.getProjectId());
		
		Mockito.verify(capabilityDAL, Mockito.times(1)).save(capabilityFirst);
	}
	
	@Test 
	void should_returnCapabilities_whenGetAllProjectsByCapabilityId() {
		BDDMockito.given(projectDAL.findById(BDDMockito.anyInt())).willReturn(optionalProjectFirst);
		BDDMockito.given(capabilityDAL.findById(capabilityFirst.getCapabilityId())).willReturn(optionalCapabilityFirst);
		capabilityService.addProject(capabilityId, projectFirst.getProjectId());
		
		Set<Project> projectResult = capabilityService.getAllProjectsByCapabilityId(capabilityId);
		
		assertNotNull(projectResult);
		assertEquals(projects.size(), projectResult.size());
	}
	
	@Test 
	void should_returnCapability_whenAddResource() {
		BDDMockito.given(resourceDAL.findById(BDDMockito.anyInt())).willReturn(optionalResourceFirst);
		BDDMockito.given(capabilityDAL.findById(capabilityFirst.getCapabilityId())).willReturn(optionalCapabilityFirst);
		BDDMockito.given(capabilityDAL.save(BDDMockito.any(Capability.class))).willReturn(capabilityFirst);
		
		Capability capability = capabilityService.addResource(capabilityId, resourceFirst.getResourceId());
		
		assertNotNull(capability);
		assertTrue(capability instanceof Capability);
		testCapability(capabilityFirst, capability);
	}
	
	@Test 
	void should_verifyDeleted_whenDeleteResource() {
		BDDMockito.given(resourceDAL.findById(BDDMockito.anyInt())).willReturn(optionalResourceFirst);
		BDDMockito.given(capabilityDAL.findById(capabilityFirst.getCapabilityId())).willReturn(optionalCapabilityFirst);
		
		capabilityService.deleteResource(capabilityId, resourceFirst.getResourceId());
		
		Mockito.verify(capabilityDAL, Mockito.times(1)).save(capabilityFirst);
	}
	
	@Test 
	void should_returnCapabilities_whenGetAllResourcesByCapabilityId() {
		BDDMockito.given(resourceDAL.findById(BDDMockito.anyInt())).willReturn(optionalResourceFirst);
		BDDMockito.given(capabilityDAL.findById(capabilityFirst.getCapabilityId())).willReturn(optionalCapabilityFirst);
		capabilityService.addResource(capabilityId, resourceFirst.getResourceId());
		
		Set<Resource> resourceResult = capabilityService.getAllResourceByResourceId(capabilityId);
		
		assertNotNull(resourceResult);
		assertEquals(resources.size(), resourceResult.size());
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
