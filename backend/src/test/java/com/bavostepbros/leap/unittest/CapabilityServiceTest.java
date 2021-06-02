package com.bavostepbros.leap.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.bavostepbros.leap.domain.customexceptions.CapabilityException;
import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.EnumException;
import com.bavostepbros.leap.domain.customexceptions.ForeignKeyException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.capabilitylevel.CapabilityLevel;
import com.bavostepbros.leap.domain.model.paceofchange.PaceOfChange;
import com.bavostepbros.leap.domain.model.targetoperatingmodel.TargetOperatingModel;
import com.bavostepbros.leap.domain.service.capabilityservice.CapabilityService;
import com.bavostepbros.leap.domain.service.environmentservice.EnvironmentService;
import com.bavostepbros.leap.domain.service.statusservice.StatusService;
import com.bavostepbros.leap.persistence.CapabilityDAL;
import com.bavostepbros.leap.persistence.EnvironmentDAL;
import com.bavostepbros.leap.persistence.StatusDAL;

/**
*
* @author Bavo Van Meel
*
*/
@SpringBootTest
public class CapabilityServiceTest {
	
	@Autowired
	private CapabilityService capabilityService;
	
	@Autowired
	private EnvironmentService environmentService;

	@Autowired
	private StatusService statusService;
	
	@MockBean
	private CapabilityDAL capabilityDAL;
	
	@MockBean
	private StatusDAL statusDAL;

	@MockBean
	private EnvironmentDAL environmentDAL;
	
	@SpyBean
	private CapabilityService spyCapabilityService;
	
	@SpyBean
	private StatusService spyStatusService;

	@SpyBean
	private EnvironmentService spyEnvironmentService;
	
	private Capability capability;
	private Status status;
	private Environment environment;
	private List<Capability> capabilities;
	private Optional<Status> optionalStatus;
	private Optional<Capability> optionalCapability;
	private Optional<Environment> optionalEnvironment;	
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@BeforeEach
	public void init() {
		status = new Status(1, LocalDate.of(2021, 5, 9));
		environment = new Environment(1, "Environment test");
		capability = new Capability(1, environment, status, 0, "Capability 1",
				PaceOfChange.DIFFERENTIATION, TargetOperatingModel.COORDINATION, 
				10, 9, 8);
		capabilityService.updateLevel(capability);
		capabilities = List.of(
				new Capability(1, environment, status, 1, "Capability 1", 
						PaceOfChange.INNOVATIVE, TargetOperatingModel.DIVERSIFICATION, 
						10, 9, 8),
				new Capability(2, environment, status, 1, "Capability 2", 
						PaceOfChange.STANDARD, TargetOperatingModel.REPLICATION, 
						10, 9, 8));
		optionalStatus = Optional.of(status);
		optionalCapability = Optional.of(capability);
		optionalEnvironment = Optional.of(environment);
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
		assertNotNull(capability);
		assertNotNull(capabilities);
		assertNotNull(optionalStatus);
		assertNotNull(optionalEnvironment);
		assertNotNull(optionalCapability);
	}
	
	@Test
	void should_throwInvalidInputException_whenSavedInputIsInvalid() {
		Integer parentCapabilityId = capability.getParentCapabilityId();
		String capabilityName = "";
		String paceOfChange = capability.getPaceOfChange().toString();
		String targetOperatingModel = capability.getTargetOperatingModel().toString();
		Integer resourceQuality = capability.getResourceQuality();
		Integer informationQuality = capability.getInformationQuality();
		Integer applicationFit = capability.getApplicationFit();
		Integer statusId = status.getStatusId();
		Integer environmentId = environment.getEnvironmentId();
		String expected = "Invalid input.";
		
		Exception exception = assertThrows(InvalidInputException.class,
				() -> capabilityService.save(environmentId, statusId, 
						parentCapabilityId, capabilityName,
						paceOfChange, targetOperatingModel, resourceQuality, 
						informationQuality, applicationFit));

		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_throwForeignKeyException_whenSavedEnvironmentIdIsInvalid() {
		Integer parentCapabilityId = capability.getParentCapabilityId();
		String capabilityName = capability.getCapabilityName();
		String paceOfChange = capability.getPaceOfChange().toString();
		String targetOperatingModel = capability.getTargetOperatingModel().toString();
		Integer resourceQuality = capability.getResourceQuality();
		Integer informationQuality = capability.getInformationQuality();
		Integer applicationFit = capability.getApplicationFit();
		Integer statusId = status.getStatusId();
		Integer environmentId = 0;
		String expected = "Environment ID is invalid.";
		
		Exception exception = assertThrows(ForeignKeyException.class,
				() -> capabilityService.save(environmentId, statusId, 
						parentCapabilityId, capabilityName,
						paceOfChange, targetOperatingModel, resourceQuality, 
						informationQuality, applicationFit));

		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_throwForeignKeyException_whenSavedStatusIdIsInvalid() {
		Integer parentCapabilityId = capability.getParentCapabilityId();
		String capabilityName = capability.getCapabilityName();
		String paceOfChange = capability.getPaceOfChange().toString();
		String targetOperatingModel = capability.getTargetOperatingModel().toString();
		Integer resourceQuality = capability.getResourceQuality();
		Integer informationQuality = capability.getInformationQuality();
		Integer applicationFit = capability.getApplicationFit();
		Integer statusId = 0;
		Integer environmentId = environment.getEnvironmentId();
		String expected = "Status ID is invalid.";
		
		Exception exception = assertThrows(ForeignKeyException.class,
				() -> capabilityService.save(environmentId, statusId, 
						parentCapabilityId, capabilityName,
						paceOfChange, targetOperatingModel, resourceQuality, 
						informationQuality, applicationFit));

		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_throwDuplicateValueException_whenSavedCapabilityNameExists() {
		Integer parentCapabilityId = capability.getParentCapabilityId();
		String capabilityName = capability.getCapabilityName();
		String paceOfChange = capability.getPaceOfChange().toString();
		String targetOperatingModel = capability.getTargetOperatingModel().toString();
		Integer resourceQuality = capability.getResourceQuality();
		Integer informationQuality = capability.getInformationQuality();
		Integer applicationFit = capability.getApplicationFit();
		Integer statusId = status.getStatusId();
		Integer environmentId = environment.getEnvironmentId();
		String expected = "Capability name already exists.";
		
		BDDMockito.doReturn(true).when(spyCapabilityService).existsByCapabilityName(capabilityName);
		
		Exception exception = assertThrows(DuplicateValueException.class,
				() -> capabilityService.save(environmentId, statusId, 
						parentCapabilityId, capabilityName,
						paceOfChange, targetOperatingModel, resourceQuality, 
						informationQuality, applicationFit));

		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_throwForeignKeyException_whenSavedStatusDoesNotExists() {
		Integer parentCapabilityId = capability.getParentCapabilityId();
		String capabilityName = capability.getCapabilityName();
		String paceOfChange = capability.getPaceOfChange().toString();
		String targetOperatingModel = capability.getTargetOperatingModel().toString();
		Integer resourceQuality = capability.getResourceQuality();
		Integer informationQuality = capability.getInformationQuality();
		Integer applicationFit = capability.getApplicationFit();
		Integer statusId = status.getStatusId();
		Integer environmentId = environment.getEnvironmentId();
		String expected = "Status ID does not exists.";
		
		BDDMockito.doReturn(false).when(spyCapabilityService).existsByCapabilityName(capabilityName);
		BDDMockito.doReturn(false).when(spyStatusService).existsById(statusId);
		
		Exception exception = assertThrows(ForeignKeyException.class,
				() -> capabilityService.save(environmentId, statusId, 
						parentCapabilityId, capabilityName,
						paceOfChange, targetOperatingModel, resourceQuality, 
						informationQuality, applicationFit));

		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_throwForeignKeyException_whenSavedEnvironmentDoesNotExists() {
		Integer parentCapabilityId = capability.getParentCapabilityId();
		String capabilityName = capability.getCapabilityName();
		String paceOfChange = capability.getPaceOfChange().toString();
		String targetOperatingModel = capability.getTargetOperatingModel().toString();
		Integer resourceQuality = capability.getResourceQuality();
		Integer informationQuality = capability.getInformationQuality();
		Integer applicationFit = capability.getApplicationFit();
		Integer statusId = status.getStatusId();
		Integer environmentId = environment.getEnvironmentId();
		String expected = "Environment ID does not exists.";
		
		BDDMockito.doReturn(false).when(spyCapabilityService).existsByCapabilityName(capabilityName);
		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);
		BDDMockito.doReturn(false).when(spyEnvironmentService).existsById(environmentId);
		
		Exception exception = assertThrows(ForeignKeyException.class,
				() -> capabilityService.save(environmentId, statusId, 
						parentCapabilityId, capabilityName,
						paceOfChange, targetOperatingModel, resourceQuality, 
						informationQuality, applicationFit));

		assertEquals(expected, exception.getMessage());
	}
	
//	@Test
//	void should_throwEnumException_whenSavedCapabilityLevelIsNotValid() {
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
//		BDDMockito.doReturn(false).when(spyCapabilityService).existsByCapabilityName(capabilityName);
//		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);
//		BDDMockito.doReturn(true).when(spyEnvironmentService).existsById(environmentId);
//
//		Exception exception = assertThrows(EnumException.class,
//				() -> capabilityService.save(environmentId, statusId,
//						parentCapabilityId, capabilityName,
//						paceOfChange, targetOperatingModel, resourceQuality,
//						informationQuality, applicationFit));
//
//		assertEquals(expected, exception.getMessage());
//	}
	
	@Test
	void should_saveCapability_whenCapabilityIsSaved() {
		Integer parentCapabilityId = capability.getParentCapabilityId();
		String capabilityName = capability.getCapabilityName();
		String paceOfChange = capability.getPaceOfChange().toString();
		String targetOperatingModel = capability.getTargetOperatingModel().toString();
		Integer resourceQuality = capability.getResourceQuality();
		Integer informationQuality = capability.getInformationQuality();
		Integer applicationFit = capability.getApplicationFit();
		Integer statusId = status.getStatusId();
		Integer environmentId = environment.getEnvironmentId();
		
		BDDMockito.doReturn(false).when(spyCapabilityService).existsByCapabilityName(capabilityName);
		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);
		BDDMockito.doReturn(true).when(spyEnvironmentService).existsById(environmentId);
		
		BDDMockito.given(statusDAL.findById(BDDMockito.anyInt())).willReturn(optionalStatus);
		BDDMockito.given(environmentDAL.findById(BDDMockito.anyInt())).willReturn(optionalEnvironment);
		BDDMockito.given(capabilityDAL.save(BDDMockito.any(Capability.class))).willReturn(capability);
		
		Capability result = capabilityService.save(environmentId, statusId, parentCapabilityId, 
				capabilityName, paceOfChange, targetOperatingModel,
				resourceQuality, informationQuality, applicationFit);
		capabilityService.updateLevel(result);
		
		assertNotNull(result);
		assertTrue(result instanceof Capability);
		assertEquals(capability.getCapabilityId(), result.getCapabilityId());
		assertEquals(capability.getStatus().getStatusId(), result.getStatus().getStatusId());
		assertEquals(capability.getStatus().getValidityPeriod(), result.getStatus().getValidityPeriod());
		assertEquals(capability.getEnvironment().getEnvironmentId(), result.getEnvironment().getEnvironmentId());
		assertEquals(capability.getEnvironment().getEnvironmentName(), result.getEnvironment().getEnvironmentName());
		assertEquals(capability.getParentCapabilityId(), result.getParentCapabilityId());
		assertEquals(capability.getCapabilityName(), result.getCapabilityName());
		assertEquals(capability.getLevel(), result.getLevel());
		assertEquals(capability.getPaceOfChange(), result.getPaceOfChange());
		assertEquals(capability.getTargetOperatingModel(), result.getTargetOperatingModel());
		assertEquals(capability.getResourceQuality(), result.getResourceQuality());
		assertEquals(capability.getInformationQuality(), result.getInformationQuality());
		assertEquals(capability.getApplicationFit(), result.getApplicationFit());
	}
	
	@Test
	void should_throwInvalidInputException_whenGetCapabilityIdNotValid() {
		Integer capabilityId = 0;
		String expected = "Capability ID is not valid.";
		
		Exception exception = assertThrows(InvalidInputException.class, 
				() -> capabilityService.get(capabilityId));

		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_throwIndexDoesNotExistException_whenGetCapabilityIdDoesNotExists() {
		Integer capabilityId = capability.getCapabilityId();
		String expected = "Capability ID does not exists.";
		
		Exception exception = assertThrows(IndexDoesNotExistException.class, 
				() -> capabilityService.get(capabilityId));

		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_retrieveValidCapability_whenIdIsValidAndIdExists() {
		Integer capabilityId = capability.getCapabilityId();
		
		BDDMockito.doReturn(true).when(spyCapabilityService).existsById(capabilityId);
		BDDMockito.given(capabilityDAL.findById(capabilityId)).willReturn(optionalCapability);
		Capability fetchedCapability = capabilityService.get(capabilityId);
		
		assertNotNull(fetchedCapability);
		assertTrue(fetchedCapability instanceof Capability);
		assertEquals(capability.getCapabilityId(), fetchedCapability.getCapabilityId());
		assertEquals(capability.getStatus().getStatusId(), fetchedCapability.getStatus().getStatusId());
		assertEquals(capability.getStatus().getValidityPeriod(), fetchedCapability.getStatus().getValidityPeriod());
		assertEquals(capability.getEnvironment().getEnvironmentId(), fetchedCapability.getEnvironment().getEnvironmentId());
		assertEquals(capability.getEnvironment().getEnvironmentName(), fetchedCapability.getEnvironment().getEnvironmentName());
		assertEquals(capability.getParentCapabilityId(), fetchedCapability.getParentCapabilityId());
		assertEquals(capability.getCapabilityName(), fetchedCapability.getCapabilityName());
		assertEquals(capability.getLevel(), fetchedCapability.getLevel());
		assertEquals(capability.getPaceOfChange(), fetchedCapability.getPaceOfChange());
		assertEquals(capability.getTargetOperatingModel(), fetchedCapability.getTargetOperatingModel());
		assertEquals(capability.getResourceQuality(), fetchedCapability.getResourceQuality());
		assertEquals(capability.getInformationQuality(), fetchedCapability.getInformationQuality());
		assertEquals(capability.getApplicationFit(), fetchedCapability.getApplicationFit());
	}
	
	@Test
	void should_retrieveCapabilityList_whenGetAllIsCalled() {
		BDDMockito.given(capabilityDAL.findAll()).willReturn(capabilities);

		List<Capability> fetchedStrategies = capabilityService.getAll();

		assertNotNull(fetchedStrategies);
		assertEquals(capabilities.size(), fetchedStrategies.size());
	}
	
	@Test
	void should_throwInvalidInputException_whenUpdatedInputIsInvalid() {
		Integer capabilityId = capability.getCapabilityId();
		Integer parentCapabilityId = capability.getParentCapabilityId();
		String capabilityName = "";
		String paceOfChange = capability.getPaceOfChange().toString();
		String targetOperatingModel = capability.getTargetOperatingModel().toString();
		Integer resourceQuality = capability.getResourceQuality();
		Integer informationQuality = capability.getInformationQuality();
		Integer applicationFit = capability.getApplicationFit();
		Integer statusId = status.getStatusId();
		Integer environmentId = environment.getEnvironmentId();
		String expected = "Invalid input.";
		
		Exception exception = assertThrows(InvalidInputException.class,
				() -> capabilityService.update(capabilityId, environmentId, statusId, 
						parentCapabilityId, capabilityName,
						paceOfChange, targetOperatingModel, resourceQuality, 
						informationQuality, applicationFit));

		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_throwForeignKeyException_whenUpdatedEnvironmentIdIsInvalid() {
		Integer capabilityId = capability.getCapabilityId();
		Integer parentCapabilityId = capability.getParentCapabilityId();
		String capabilityName = capability.getCapabilityName();
		String paceOfChange = capability.getPaceOfChange().toString();
		String targetOperatingModel = capability.getTargetOperatingModel().toString();
		Integer resourceQuality = capability.getResourceQuality();
		Integer informationQuality = capability.getInformationQuality();
		Integer applicationFit = capability.getApplicationFit();
		Integer statusId = status.getStatusId();
		Integer environmentId = 0;
		String expected = "Environment ID is invalid.";
		
		Exception exception = assertThrows(ForeignKeyException.class,
				() -> capabilityService.update(capabilityId, environmentId, statusId, 
						parentCapabilityId, capabilityName,
						paceOfChange, targetOperatingModel, resourceQuality, 
						informationQuality, applicationFit));

		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_throwForeignKeyException_whenUpdatedStatusIdIsInvalid() {
		Integer capabilityId = capability.getCapabilityId();
		Integer parentCapabilityId = capability.getParentCapabilityId();
		String capabilityName = capability.getCapabilityName();
		String paceOfChange = capability.getPaceOfChange().toString();
		String targetOperatingModel = capability.getTargetOperatingModel().toString();
		Integer resourceQuality = capability.getResourceQuality();
		Integer informationQuality = capability.getInformationQuality();
		Integer applicationFit = capability.getApplicationFit();
		Integer statusId = 0;
		Integer environmentId = environment.getEnvironmentId();
		String expected = "Status ID is invalid.";
		
		Exception exception = assertThrows(ForeignKeyException.class,
				() -> capabilityService.update(capabilityId, environmentId, statusId, 
						parentCapabilityId, capabilityName,
						paceOfChange, targetOperatingModel, resourceQuality, 
						informationQuality, applicationFit));

		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_throwCapabilityException_whenUpdateCapabilityIdDoesNotExist() {
		Integer capabilityId = capability.getCapabilityId();
		Integer parentCapabilityId = capability.getParentCapabilityId();
		String capabilityName = capability.getCapabilityName();
		String paceOfChange = capability.getPaceOfChange().toString();
		String targetOperatingModel = capability.getTargetOperatingModel().toString();
		Integer resourceQuality = capability.getResourceQuality();
		Integer informationQuality = capability.getInformationQuality();
		Integer applicationFit = capability.getApplicationFit();
		Integer statusId = status.getStatusId();
		Integer environmentId = environment.getEnvironmentId();
		String expected = "Can not update capability if it does not exist.";
		
		BDDMockito.doReturn(false).when(spyCapabilityService).existsById(capabilityId);
		
		Exception exception = assertThrows(CapabilityException.class,
				() -> capabilityService.update(capabilityId, environmentId, statusId, 
						parentCapabilityId, capabilityName,
						paceOfChange, targetOperatingModel, resourceQuality, 
						informationQuality, applicationFit));

		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_throwDuplicateValueException_whenUpdateCapabilityNameExist() {
		Integer capabilityId = capability.getCapabilityId();
		Integer parentCapabilityId = capability.getParentCapabilityId();
		String capabilityName = "xyz";
		String paceOfChange = capability.getPaceOfChange().toString();
		String targetOperatingModel = capability.getTargetOperatingModel().toString();
		Integer resourceQuality = capability.getResourceQuality();
		Integer informationQuality = capability.getInformationQuality();
		Integer applicationFit = capability.getApplicationFit();
		Integer statusId = status.getStatusId();
		Integer environmentId = environment.getEnvironmentId();
		String expected = "Capability name already exists.";
		
		BDDMockito.doReturn(true).when(spyCapabilityService).existsById(capabilityId);
		BDDMockito.doReturn(true).when(spyCapabilityService).existsByCapabilityName(capabilityName);
		
		BDDMockito.given(capabilityDAL.findById(BDDMockito.anyInt())).willReturn(optionalCapability);
		
		Exception exception = assertThrows(DuplicateValueException.class,
				() -> capabilityService.update(capabilityId, environmentId, statusId, 
						parentCapabilityId, capabilityName,
						paceOfChange, targetOperatingModel, resourceQuality, 
						informationQuality, applicationFit));

		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_throwForeignKeyException_whenUpdateStatusDoesNotExists() {
		Integer capabilityId = capability.getCapabilityId();
		Integer parentCapabilityId = capability.getParentCapabilityId();
		String capabilityName = capability.getCapabilityName();
		String paceOfChange = capability.getPaceOfChange().toString();
		String targetOperatingModel = capability.getTargetOperatingModel().toString();
		Integer resourceQuality = capability.getResourceQuality();
		Integer informationQuality = capability.getInformationQuality();
		Integer applicationFit = capability.getApplicationFit();
		Integer statusId = status.getStatusId();
		Integer environmentId = environment.getEnvironmentId();
		String expected = "Status ID does not exists.";
		
		BDDMockito.doReturn(true).when(spyCapabilityService).existsById(capabilityId);
		BDDMockito.doReturn(false).when(spyCapabilityService).existsByCapabilityName(capabilityName);
		BDDMockito.doReturn(false).when(spyStatusService).existsById(statusId);
		
		BDDMockito.given(capabilityDAL.findById(BDDMockito.anyInt())).willReturn(optionalCapability);
		
		Exception exception = assertThrows(ForeignKeyException.class,
				() -> capabilityService.save(environmentId, statusId, 
						parentCapabilityId, capabilityName,
						paceOfChange, targetOperatingModel, resourceQuality, 
						informationQuality, applicationFit));

		assertEquals(expected, exception.getMessage());
	}
	
	@Test
	void should_throwForeignKeyException_whenUpdateEnvironmentDoesNotExists() {
		Integer capabilityId = capability.getCapabilityId();
		Integer parentCapabilityId = capability.getParentCapabilityId();
		String capabilityName = capability.getCapabilityName();
		String paceOfChange = capability.getPaceOfChange().toString();
		String targetOperatingModel = capability.getTargetOperatingModel().toString();
		Integer resourceQuality = capability.getResourceQuality();
		Integer informationQuality = capability.getInformationQuality();
		Integer applicationFit = capability.getApplicationFit();
		Integer statusId = status.getStatusId();
		Integer environmentId = environment.getEnvironmentId();
		String expected = "Environment ID does not exists.";
		
		BDDMockito.doReturn(true).when(spyCapabilityService).existsById(capabilityId);
		BDDMockito.doReturn(false).when(spyCapabilityService).existsByCapabilityName(capabilityName);
		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);
		BDDMockito.doReturn(false).when(spyEnvironmentService).existsById(environmentId);
		
		BDDMockito.given(capabilityDAL.findById(BDDMockito.anyInt())).willReturn(optionalCapability);
		
		Exception exception = assertThrows(ForeignKeyException.class,
				() -> capabilityService.save(environmentId, statusId, 
						parentCapabilityId, capabilityName,
						paceOfChange, targetOperatingModel, resourceQuality, 
						informationQuality, applicationFit));

		assertEquals(expected, exception.getMessage());
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
	
	@Test
	void should_retrieveValidCapability_whenSCapabilityIsUpdated() {
		Integer capabilityId = capability.getCapabilityId();
		Integer parentCapabilityId = capability.getParentCapabilityId();
		String capabilityName = capability.getCapabilityName();
		String paceOfChange = capability.getPaceOfChange().toString();
		String targetOperatingModel = capability.getTargetOperatingModel().toString();
		Integer resourceQuality = capability.getResourceQuality();
		Integer informationQuality = capability.getInformationQuality();
		Integer applicationFit = capability.getApplicationFit();
		Integer statusId = status.getStatusId();
		Integer environmentId = environment.getEnvironmentId();
		capabilityDAL.save(capability);
		
		BDDMockito.doReturn(true).when(spyCapabilityService).existsById(capabilityId);
		BDDMockito.doReturn(false).when(spyCapabilityService).existsByCapabilityName(capabilityName);
		BDDMockito.doReturn(true).when(spyStatusService).existsById(statusId);
		BDDMockito.doReturn(true).when(spyEnvironmentService).existsById(environmentId);
		
		BDDMockito.given(statusDAL.findById(BDDMockito.anyInt())).willReturn(optionalStatus);
		BDDMockito.given(environmentDAL.findById(BDDMockito.anyInt())).willReturn(optionalEnvironment);
		BDDMockito.given(capabilityDAL.save(capability)).willReturn(capability);
		BDDMockito.given(capabilityDAL.findById(BDDMockito.anyInt())).willReturn(optionalCapability);
		
		Capability fetchedCapability = capabilityService.update(capabilityId, environmentId, statusId, parentCapabilityId, 
				capabilityName, paceOfChange, targetOperatingModel,
				resourceQuality, informationQuality, applicationFit);
		
		assertNotNull(fetchedCapability);
		assertTrue(fetchedCapability instanceof Capability);
		assertEquals(capability.getCapabilityId(), fetchedCapability.getCapabilityId());
		assertEquals(capability.getStatus().getStatusId(), fetchedCapability.getStatus().getStatusId());
		assertEquals(capability.getStatus().getValidityPeriod(), fetchedCapability.getStatus().getValidityPeriod());
		assertEquals(capability.getEnvironment().getEnvironmentId(), fetchedCapability.getEnvironment().getEnvironmentId());
		assertEquals(capability.getEnvironment().getEnvironmentName(), fetchedCapability.getEnvironment().getEnvironmentName());
		assertEquals(capability.getParentCapabilityId(), fetchedCapability.getParentCapabilityId());
		assertEquals(capability.getCapabilityName(), fetchedCapability.getCapabilityName());
		assertEquals(capability.getLevel(), fetchedCapability.getLevel());
		assertEquals(capability.getPaceOfChange(), fetchedCapability.getPaceOfChange());
		assertEquals(capability.getTargetOperatingModel(), fetchedCapability.getTargetOperatingModel());
		assertEquals(capability.getResourceQuality(), fetchedCapability.getResourceQuality());
		assertEquals(capability.getInformationQuality(), fetchedCapability.getInformationQuality());
		assertEquals(capability.getApplicationFit(), fetchedCapability.getApplicationFit());
	}
	
	@Test
	void should_throwInvalidInputException_whenDeleteInputIsInvalid() {
		Integer capabilityId = 0;
		String expected = "Capability ID is not valid.";

		Exception exception = assertThrows(InvalidInputException.class, 
				() -> capabilityService.delete(capabilityId));

		assertEquals(exception.getMessage(), expected);
	}
	
	@Test
	void should_throwIndexDoesNotExistException_whenDeleteCapabilityIdDoesNotExist() {
		Integer id = capability.getCapabilityId();
		String expected = "Capability ID does not exists.";
		BDDMockito.doReturn(false).when(spyCapabilityService).existsById(id);

		Exception exception = assertThrows(IndexDoesNotExistException.class, 
				() -> capabilityService.delete(id));

		assertEquals(exception.getMessage(), expected);
	}
	
	@Test
	void should_throwIndexDoesNotExistException_whenDeleteInputDoesNotExists() {
		Integer capabilityId = capability.getCapabilityId();
		String expected = "Capability ID does not exists.";

		capabilityDAL.deleteById(capabilityId);

		Exception exception = assertThrows(IndexDoesNotExistException.class, 
				() -> capabilityService.get(capabilityId));

		assertEquals(exception.getMessage(), expected);
	}
	
	@Test
	void should_throwInvalidInputException_whenGetCapabilitiesByEnvironmentInputIsInvalid() {
		Integer environmentId = 0;
		String expected = "Environment ID is not valid.";

		Exception exception = assertThrows(InvalidInputException.class, 
				() -> capabilityService.getCapabilitiesByEnvironment(environmentId));

		assertEquals(exception.getMessage(), expected);
	}
	
	@Test
	void should_throwIndexDoesNotExistException_whenGetCapabilitiesByEnvironmentIdDoesNotExist() {
		Integer environmentId = capability.getEnvironment().getEnvironmentId();
		String expected = "Environment ID does not exists.";
		
		BDDMockito.doReturn(false).when(spyEnvironmentService).existsById(environmentId);
		
		Exception exception = assertThrows(ForeignKeyException.class, 
				() -> capabilityService.getCapabilitiesByEnvironment(environmentId));

		assertEquals(exception.getMessage(), expected);
				
	}
	
	@Test
	void should_retrieveValidEnvironment_whenGetCapabilitiesByEnvironmentIdDoesExistAndIsValid() {
		Integer environmentId = capability.getEnvironment().getEnvironmentId();

		BDDMockito.doReturn(true).when(spyEnvironmentService).existsById(environmentId);
		BDDMockito.given(environmentDAL.findById(environmentId)).willReturn(optionalEnvironment);
		Environment fetchedEnvironment = environmentService.get(environmentId);

		assertNotNull(fetchedEnvironment);
		assertTrue(fetchedEnvironment instanceof Environment);
		assertEquals(capability.getEnvironment().getEnvironmentId(), fetchedEnvironment.getEnvironmentId());
		assertEquals(capability.getEnvironment().getEnvironmentName(), fetchedEnvironment.getEnvironmentName());
	}
	
	@Test
	void should_retrieveValidCapabilities_whenGetCapabilitiesByEnvironmentIdDoesExistAndIsValid() {
		Integer environmentId = capability.getEnvironment().getEnvironmentId();

		BDDMockito.doReturn(true).when(spyEnvironmentService).existsById(environmentId);
		BDDMockito.given(environmentDAL.findById(environmentId)).willReturn(optionalEnvironment);
		BDDMockito.given(capabilityDAL.findByEnvironment(environment)).willReturn(capabilities);
		List<Capability> fetchedCapablities = capabilityService.getCapabilitiesByEnvironment(environmentId);

		assertNotNull(fetchedCapablities);
		assertEquals(capabilities.size(), fetchedCapablities.size());
	}
	
	@Test
	void should_throwInvalidInputException_whenGetCapabilitiesByLevelInputIsInvalid() {
		String level = null;
		String expected = "CapabilityLevel is not valid.";

		Exception exception = assertThrows(InvalidInputException.class, 
				() -> capabilityService.getCapabilitiesByLevel(level));

		assertEquals(exception.getMessage(), expected);
	}
	
	@Test
	void should_throwEnumException_whenGetCapabilitiesByLevelInputIsInvalid() {
		String level = "FOUR";
		String expected = "CapabilityLevel is not valid.";

		Exception exception = assertThrows(EnumException.class, 
				() -> capabilityService.getCapabilitiesByLevel(level));

		assertEquals(exception.getMessage(), expected);
	}
	
	@Test
	void should_retrieveValidCapabilities_whenGetCapabilitiesByLevel() {
		String level = "ONE";
		CapabilityLevel capabilityLevel = capability.getLevel();

		BDDMockito.given(capabilityDAL.findByLevel(capabilityLevel)).willReturn(capabilities);
		List<Capability> fetchedCapablities = capabilityService.getCapabilitiesByLevel(level);

		assertNotNull(fetchedCapablities);
		assertEquals(capabilities.size(), fetchedCapablities.size());
	}
	
	@Test
	void should_throwInvalidInputException_whenGetCapabilityChildrenInputIsInvalid() {
		Integer parentId = 0;
		String expected = "Parent ID is not valid.";

		Exception exception = assertThrows(InvalidInputException.class, 
				() -> capabilityService.getCapabilityChildren(parentId));

		assertEquals(exception.getMessage(), expected);
	}
	
//	@Test
//	void should_throwIndexDoesNotExistException_whenGetCapabilityChildrenIdDoesNotExists() {
//		Integer parentId = capability.getParentCapabilityId();
//		String expected = "Parent ID does not exists.";
//
//		BDDMockito.doReturn(false).when(spyCapabilityService).existsById(parentId);
//
//		Exception exception = assertThrows(IndexDoesNotExistException.class,
//				() -> capabilityService.getCapabilityChildren(parentId));
//
//		assertEquals(exception.getMessage(), expected);
//	}
	
	@Test
	void should_retrieveValidCapabilities_whenGetCapabilityChildren() {
		Integer parentId = capability.getParentCapabilityId();
		
		BDDMockito.doReturn(true).when(spyCapabilityService).existsById(parentId);

		BDDMockito.given(capabilityDAL.findByParentCapabilityId(parentId)).willReturn(capabilities);
		List<Capability> fetchedCapablities = capabilityService.getCapabilityChildren(parentId);

		assertNotNull(fetchedCapablities);
		assertEquals(capabilities.size(), fetchedCapablities.size());
	}
	
	@Test
	void should_throwInvalidInputException_whenGetCapabilitiesByParentIdAndLevelParentIdIsInvalid() {
		Integer parentId = 0;
		String level = "ONE";
		String expected = "Parent ID is not valid.";

		Exception exception = assertThrows(InvalidInputException.class, 
				() -> capabilityService.getCapabilitiesByParentIdAndLevel(parentId, level));

		assertEquals(exception.getMessage(), expected);
	}
	
//	@Test
//	void should_throwInvalidInputException_whenGetCapabilitiesByParentIdAndLevelLevelIsInvalid() {
//		Integer parentId = capability.getParentCapabilityId();
//		String level = null;
//		String expected = "CapabilityLevel is not valid.";
//
//		Exception exception = assertThrows(InvalidInputException.class,
//				() -> capabilityService.getCapabilitiesByParentIdAndLevel(parentId, level));
//
//		assertEquals(exception.getMessage(), expected);
//	}
//
//	@Test
//	void should_throwIndexDoesNotExistException_whenGetCapabilitiesByParentIdAndLevelIdDoesNotExists() {
//		Integer parentId = capability.getParentCapabilityId();
//		String level = "ONE";
//		String expected = "Parent ID does not exists.";
//
//		BDDMockito.doReturn(false).when(spyCapabilityService).existsById(parentId);
//
//		Exception exception = assertThrows(IndexDoesNotExistException.class,
//				() -> capabilityService.getCapabilitiesByParentIdAndLevel(parentId, level));
//
//		assertEquals(exception.getMessage(), expected);
//	}
//
//	@Test
//	void should_throwEnumException_whenGetCapabilitiesByParentIdAndLevelInputIsInvalid() {
//		Integer parentId = capability.getParentCapabilityId();
//		String level = "FOUR";
//		String expected = "CapabilityLevel is not valid.";
//
//		BDDMockito.doReturn(true).when(spyCapabilityService).existsById(parentId);
//
//		Exception exception = assertThrows(EnumException.class,
//				() -> capabilityService.getCapabilitiesByParentIdAndLevel(parentId, level));
//
//		assertEquals(exception.getMessage(), expected);
//	}
//
//	@Test
//	void should_retrieveValidCapabilities_whenGetCapabilitiesByParentIdAndLevel() {
//		Integer parentId = capability.getParentCapabilityId();
//		String level = "ONE";
//		CapabilityLevel capabilityLevel = capability.getLevel();
//
//		BDDMockito.doReturn(true).when(spyCapabilityService).existsById(parentId);
//
//		BDDMockito.given(capabilityDAL.findByParentCapabilityIdAndLevel(parentId, capabilityLevel)).willReturn(capabilities);
//		List<Capability> fetchedCapablities = capabilityService.getCapabilitiesByParentIdAndLevel(parentId, level);
//
//		assertNotNull(fetchedCapablities);
//		assertEquals(capabilities.size(), fetchedCapablities.size());
//	}
	
	@Test
	void should_ReturnFalse_whenCapabilityDoesNotExistById() {
		BDDMockito.given(capabilityDAL.existsById(BDDMockito.anyInt())).willReturn(false);

		boolean result = capabilityService.existsById(capability.getCapabilityId());

		assertFalse(result);
	}

	@Test
	void should_ReturnTrue_whenCapabilityDoesExistById() {
		BDDMockito.given(capabilityDAL.existsById(BDDMockito.anyInt())).willReturn(true);

		boolean result = capabilityService.existsById(capability.getCapabilityId());

		assertTrue(result);
	}
	
	@Test
	void should_ReturnTrue_whenCapabilityDoesExistByCapabilityName() {
		BDDMockito.given(capabilityDAL.findByCapabilityName(BDDMockito.any(String.class))).willReturn(optionalCapability);

		boolean result = capabilityService.existsByCapabilityName(capability.getCapabilityName());

		assertTrue(result);
	}

	@Test
	void should_ReturnFalse_whenCapabilityDoesNotExistByCapabilityName() {
		BDDMockito.given(capabilityDAL.findByCapabilityName(BDDMockito.any(String.class))).willReturn(Optional.empty());

		boolean result = capabilityService.existsByCapabilityName(capability.getCapabilityName());

		assertFalse(result);
	}
}
