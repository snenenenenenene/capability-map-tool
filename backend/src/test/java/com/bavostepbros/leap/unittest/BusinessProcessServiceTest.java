package com.bavostepbros.leap.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.bavostepbros.leap.domain.model.BusinessProcess;
import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.paceofchange.PaceOfChange;
import com.bavostepbros.leap.domain.model.targetoperatingmodel.TargetOperatingModel;
import com.bavostepbros.leap.domain.service.businessprocessservice.BusinessProcessService;
import com.bavostepbros.leap.domain.service.capabilityservice.CapabilityService;
import com.bavostepbros.leap.persistence.BusinessProcessDAL;
import com.bavostepbros.leap.persistence.CapabilityDAL;

@AutoConfigureMockMvc
@SpringBootTest
public class BusinessProcessServiceTest {
	
	@Autowired
	private BusinessProcessService businessProcessService;
	
	@Autowired
	private CapabilityService capabilityService;
	
	@MockBean
	private BusinessProcessDAL businessProcessDAL;
	
	@MockBean
	private CapabilityDAL capabilityDAL;
	
	private BusinessProcess businessProcessFirst;
	private BusinessProcess businessProcessSecond;
	private Status statusFirst;
	private Environment environmentFirst;
	private Capability capabilityFirst;
	private Capability capabilitySecond;
	private List<BusinessProcess> businessProcessList;
	private Set<Capability> capabilities;
	private Optional<BusinessProcess> optionalBusinessProcessFirst;
	private Optional<Capability> optionalCapabilityFirst;
	
	private Integer businessProcessId;
	private String businessProcessName;
	private String businessProcessDescription;
	
	@BeforeEach
	void init() {
		businessProcessFirst = new BusinessProcess(1, "BP 1", "Description 1");
		businessProcessSecond = new BusinessProcess(2, "BP 2", "Description 2");
		statusFirst = new Status(1, LocalDate.of(2021, 5, 9));
		environmentFirst = new Environment(1, "Environment test");
		capabilityFirst = new Capability(1, environmentFirst, statusFirst, 0, "Capability 1", "Description 1",
				PaceOfChange.DIFFERENTIATION, TargetOperatingModel.COORDINATION, 10, 9, 8);
		capabilitySecond = new Capability(2, environmentFirst, statusFirst, capabilityFirst.getCapabilityId(), "Capability 2",
				"Description 2", PaceOfChange.DIFFERENTIATION, TargetOperatingModel.COORDINATION, 10, 9, 8);
		businessProcessList = List.of(businessProcessFirst, businessProcessSecond);
		capabilities = Set.of(capabilityFirst, capabilitySecond);
		optionalBusinessProcessFirst = Optional.of(businessProcessFirst);
		optionalCapabilityFirst = Optional.of(capabilityFirst);
		
		businessProcessId = businessProcessFirst.getBusinessProcessId();
		businessProcessName = businessProcessFirst.getBusinessProcessName();
		businessProcessDescription = businessProcessFirst.getBusinessProcessDescription();
	}
	
	@Test
	void shouldNotBeNull() {
		assertNotNull(businessProcessService);
		assertNotNull(capabilityService);
		assertNotNull(businessProcessDAL);
		assertNotNull(capabilityDAL);
		assertNotNull(businessProcessFirst);
		assertNotNull(businessProcessSecond);
		assertNotNull(businessProcessList);
		assertNotNull(capabilities);
		assertNotNull(statusFirst);
		assertNotNull(environmentFirst);
		assertNotNull(capabilityFirst);
		assertNotNull(capabilitySecond);
		assertNotNull(optionalBusinessProcessFirst);
	}
	
	@Test 
	void should_returnBusinessProcess_whenSaveBusinessProcess() {
		BDDMockito.given(businessProcessDAL.save(BDDMockito.any(BusinessProcess.class))).willReturn(businessProcessFirst);
		
		BusinessProcess businessProcess = businessProcessService.save(businessProcessName, businessProcessDescription);
		
		assertNotNull(businessProcess);
		assertTrue(businessProcess instanceof BusinessProcess);
		testbusinessProcess(businessProcessFirst, businessProcess);
	}
	
	@Test 
	void should_throwNullPointerException_whenGetBusinessProcessByIdInvalidId() {
		String expectedErrorMessage = "Businessprocess does not exist.";
		
		Exception exception = assertThrows(NullPointerException.class, 
				() -> businessProcessService.get(businessProcessId));
		
		assertEquals(expectedErrorMessage, exception.getMessage());
	}
	
	@Test 
	void should_returnBusinessProcess_whenGetBusinessProcessById() {
		BDDMockito.given(businessProcessDAL.findById(businessProcessId)).willReturn(optionalBusinessProcessFirst);
		
		BusinessProcess businessProcess = businessProcessService.get(businessProcessId);
		
		assertNotNull(businessProcess);
		assertTrue(businessProcess instanceof BusinessProcess);
		testbusinessProcess(businessProcessFirst, businessProcess);
	}
	
	@Test 
	void should_returnBusinessProcess_whenUpdateBusinessProcess() {
		BDDMockito.given(businessProcessDAL.save(BDDMockito.any(BusinessProcess.class))).willReturn(businessProcessFirst);
		
		BusinessProcess businessProcess = businessProcessService.update(businessProcessId, businessProcessName, businessProcessDescription);
		
		assertNotNull(businessProcess);
		assertTrue(businessProcess instanceof BusinessProcess);
		testbusinessProcess(businessProcessFirst, businessProcess);
	}
	
	@Test 
	void should_verify_whenDeleteBusinessProcess() {
		businessProcessService.delete(businessProcessId);
		
		Mockito.verify(businessProcessDAL, Mockito.times(1)).deleteById(Mockito.eq(businessProcessId));
	}
	
	@Test 
	void should_throwNullPointerException_whenGetBusinessProcessByNameInvalidName() {
		String expectedErrorMessage = "Businessprocess does not exist.";
		
		Exception exception = assertThrows(NullPointerException.class, 
				() -> businessProcessService.getBusinessProcessByName(businessProcessName));
		
		assertEquals(expectedErrorMessage, exception.getMessage());
	}
	
	@Test 
	void should_returnBusinessProcess_whenGetBusinessProcessByName() {
		BDDMockito.given(businessProcessDAL.findByBusinessProcessName(businessProcessName)).willReturn(optionalBusinessProcessFirst);
		
		BusinessProcess businessProcess = businessProcessService.getBusinessProcessByName(businessProcessName);
		
		assertNotNull(businessProcess);
		assertTrue(businessProcess instanceof BusinessProcess);
		testbusinessProcess(businessProcessFirst, businessProcess);
	}
	
	@Test 
	void should_returnBusinessProcessList_whenGetAllBusinessProcess() {
		BDDMockito.given(businessProcessDAL.findAll()).willReturn(businessProcessList);
		
		List<BusinessProcess> businessProcess = businessProcessService.getAll();
		
		assertNotNull(businessProcess);
		assertEquals(businessProcessList.size(), businessProcess.size());
		testbusinessProcess(businessProcessFirst, businessProcess.get(0));
		testbusinessProcess(businessProcessSecond, businessProcess.get(1));
	}
	
	@Test 
	void should_returnBusinessProcess_whenAddCapability() {
		BDDMockito.given(businessProcessDAL.findById(businessProcessId)).willReturn(optionalBusinessProcessFirst);
		BDDMockito.given(capabilityDAL.findById(capabilityFirst.getCapabilityId())).willReturn(optionalCapabilityFirst);
		BDDMockito.given(businessProcessDAL.save(BDDMockito.any(BusinessProcess.class))).willReturn(businessProcessFirst);
		
		BusinessProcess businessProcess = businessProcessService.addCapability(businessProcessId, capabilityFirst.getCapabilityId());
		
		assertNotNull(businessProcess);
		assertTrue(businessProcess instanceof BusinessProcess);
		testbusinessProcess(businessProcessFirst, businessProcess);
	}
	
	@Test 
	void should_verify_whenDeleteCapability() {
		BDDMockito.given(businessProcessDAL.findById(businessProcessId)).willReturn(optionalBusinessProcessFirst);
		BDDMockito.given(capabilityDAL.findById(capabilityFirst.getCapabilityId())).willReturn(optionalCapabilityFirst);
		
		businessProcessService.deleteCapability(businessProcessId, capabilityFirst.getCapabilityId());
		
		Mockito.verify(businessProcessDAL, Mockito.times(1)).save(businessProcessFirst);
	}
	
	@Test 
	void should_returnCapabilities_whenGetAllCapabilitiesByBusinessProcessId() {
		BDDMockito.given(businessProcessDAL.findById(businessProcessId)).willReturn(optionalBusinessProcessFirst);
		BDDMockito.given(capabilityDAL.findById(businessProcessId)).willReturn(optionalCapabilityFirst);
		businessProcessService.addCapability(businessProcessId, capabilityFirst.getCapabilityId());
		
		Set<Capability> capabilitiesResult = businessProcessService.getAllCapabilitiesByBusinessProcessId(businessProcessId);
		
		assertNotNull(capabilitiesResult);
		// -1 because we test only 1 linked capability here
		assertEquals(capabilities.size() - 1, capabilitiesResult.size());
	}
	
	private void testbusinessProcess(BusinessProcess expectedObject, BusinessProcess actualObject) {
		assertEquals(expectedObject.getBusinessProcessId(), actualObject.getBusinessProcessId());
		assertEquals(expectedObject.getBusinessProcessName(), actualObject.getBusinessProcessName());
		assertEquals(expectedObject.getBusinessProcessDescription(), actualObject.getBusinessProcessDescription());
	}
}
