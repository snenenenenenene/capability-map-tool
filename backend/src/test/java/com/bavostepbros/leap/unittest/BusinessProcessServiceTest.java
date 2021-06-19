package com.bavostepbros.leap.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.bavostepbros.leap.domain.model.BusinessProcess;
import com.bavostepbros.leap.domain.service.businessprocessservice.BusinessProcessService;
import com.bavostepbros.leap.persistence.BusinessProcessDAL;

@AutoConfigureMockMvc
@SpringBootTest
public class BusinessProcessServiceTest {
	
	@Autowired
	private BusinessProcessService businessProcessService;
	
	@MockBean
	private BusinessProcessDAL businessProcessDAL;
	
	private BusinessProcess businessProcessFirst;
	private BusinessProcess businessProcessSecond;
	private List<BusinessProcess> businessProcessList;
	private Optional<BusinessProcess> optionalBusinessProcessFirst;
	
	private Integer businessProcessId;
	private String businessProcessName;
	private String businessProcessDescription;
	
	@BeforeEach
	void init() {
		businessProcessFirst = new BusinessProcess(1, "BP 1", "Description 1");
		businessProcessSecond = new BusinessProcess(2, "BP 2", "Description 2");
		businessProcessList = List.of(businessProcessFirst, businessProcessSecond);
		optionalBusinessProcessFirst = Optional.of(businessProcessFirst);
		
		businessProcessId = businessProcessFirst.getBusinessProcessId();
		businessProcessName = businessProcessFirst.getBusinessProcessName();
		businessProcessDescription = businessProcessFirst.getBusinessProcessDescription();
	}
	
	@Test
	void shouldNotBeNull() {
		assertNotNull(businessProcessService);
		assertNotNull(businessProcessDAL);
		assertNotNull(businessProcessFirst);
		assertNotNull(businessProcessSecond);
		assertNotNull(businessProcessList);
		assertNotNull(optionalBusinessProcessFirst);
	}
	
//	@Test 
//	void should_throwNoSuchElementException_whenSavedInputNameIsInvalid() {
//		String businessProcessName = "";
//		String expected = "No value present";
//
//		Exception exception = assertThrows(NoSuchElementException.class,
//				() -> businessProcessService.save(businessProcessName, businessProcessDescription));
//
//		assertEquals(expected, exception.getMessage());
//	}
	
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
	
	private void testbusinessProcess(BusinessProcess expectedObject, BusinessProcess actualObject) {
		assertEquals(expectedObject.getBusinessProcessId(), actualObject.getBusinessProcessId());
		assertEquals(expectedObject.getBusinessProcessName(), actualObject.getBusinessProcessName());
		assertEquals(expectedObject.getBusinessProcessDescription(), actualObject.getBusinessProcessDescription());
	}
}
