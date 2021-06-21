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

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Resource;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.paceofchange.PaceOfChange;
import com.bavostepbros.leap.domain.model.targetoperatingmodel.TargetOperatingModel;
import com.bavostepbros.leap.domain.service.resourceservice.ResourceService;
import com.bavostepbros.leap.persistence.CapabilityDAL;
import com.bavostepbros.leap.persistence.ResourceDAL;

@AutoConfigureMockMvc
@SpringBootTest
public class ResourceServiceTest {
	
	@Autowired
	private ResourceService resourceService;
	
	@MockBean
	private ResourceDAL resourceDAL;
	
	@MockBean
	private CapabilityDAL capabilityDAL;
	
	private Resource resourceFirst;
	private Resource resourceSecond;
	private Status statusFirst;
	private Environment environmentFirst;
	private Capability capabilityFirst;
	private List<Resource> resources;
	private Set<Capability> capabilities;
	private Optional<Resource> optionalResourceFirst;
	private Optional<Capability> optionalCapabilityFirst;
	
	private Integer resourceId;
	private String resourceName;
	private String resourceDescription;
	private Double fullTimeEquivalentYearlyValue;
	
	@BeforeEach
	void init() {
		resourceFirst = new Resource(1, "Resource 1", "Good description 1", 20.0);
		resourceSecond = new Resource(2, "Resource 2", "Good description 2", 40.0);
		statusFirst = new Status(1, LocalDate.of(2021, 5, 9));
		environmentFirst = new Environment(1, "Environment test");
		capabilityFirst = new Capability(1, environmentFirst, statusFirst, 0, "Capability 1", "Description 1",
				PaceOfChange.DIFFERENTIATION, TargetOperatingModel.COORDINATION, 10, 2.0, 3.0);
		resources = List.of(resourceFirst, resourceSecond);
		capabilities = Set.of(capabilityFirst);
		optionalResourceFirst = Optional.of(resourceFirst);
		optionalCapabilityFirst = Optional.of(capabilityFirst);
		
		resourceId = resourceFirst.getResourceId();
		resourceName = resourceFirst.getResourceName();
		resourceDescription = resourceFirst.getResourceDescription();
		fullTimeEquivalentYearlyValue = resourceFirst.getFullTimeEquivalentYearlyValue();		
	}
	
	@Test
	void shouldNotBeNull() {
		assertNotNull(resourceService);
		assertNotNull(resourceDAL);
		assertNotNull(capabilityDAL);
		assertNotNull(resourceFirst);
		assertNotNull(resourceSecond);
		assertNotNull(statusFirst);
		assertNotNull(environmentFirst);
		assertNotNull(capabilityFirst);
		assertNotNull(resources);
		assertNotNull(capabilities);
		assertNotNull(optionalResourceFirst);
		assertNotNull(optionalCapabilityFirst);
		assertNotNull(resourceId);
		assertNotNull(resourceName);
		assertNotNull(resourceDescription);
		assertNotNull(fullTimeEquivalentYearlyValue);
	}
	
	@Test 
	void should_returnResource_whenSaveResource() {
		BDDMockito.given(resourceDAL.save(BDDMockito.any(Resource.class))).willReturn(resourceFirst);
		
		Resource resource = resourceService.save(resourceName, resourceDescription, fullTimeEquivalentYearlyValue);
		
		assertNotNull(resource);
		assertTrue(resource instanceof Resource);
		testResource(resourceFirst, resource);
	}
	
	@Test 
	void should_throwNullPointerException_whenGetResourceById() {
		String expectedErrorMessage = "Resource does not exists.";
		
		Exception exception = assertThrows(NullPointerException.class, 
				() -> resourceService.get(resourceId));
		
		assertEquals(expectedErrorMessage, exception.getMessage());
	}
	
	@Test 
	void should_returnResource_whenGetResourceById() {
		BDDMockito.given(resourceDAL.findById(BDDMockito.anyInt())).willReturn(optionalResourceFirst);
		
		Resource resource = resourceService.get(resourceId);
		
		assertNotNull(resource);
		assertTrue(resource instanceof Resource);
		testResource(resourceFirst, resource);
	}
	
	@Test 
	void should_returnResource_whenUpdateResource() {
		BDDMockito.given(resourceDAL.save(BDDMockito.any(Resource.class))).willReturn(resourceFirst);
		
		Resource resource = resourceService.update(resourceId, resourceName, resourceDescription, fullTimeEquivalentYearlyValue);
		
		assertNotNull(resource);
		assertTrue(resource instanceof Resource);
		testResource(resourceFirst, resource);
	}
	
	@Test 
	void should_verify_whenDeleteResource() {
		resourceService.delete(resourceId);
		
		Mockito.verify(resourceDAL, Mockito.times(1)).deleteById(Mockito.eq(resourceId));
	}
	
	@Test 
	void should_throwNullPointerException_whenGetResourceByName() {
		String expectedErrorMessage = "Resource does not exists.";
		
		Exception exception = assertThrows(NullPointerException.class, 
				() -> resourceService.getResourceByName(resourceName));
		
		assertEquals(expectedErrorMessage, exception.getMessage());
	}
	
	@Test 
	void should_returnResource_whenGetResourceByName() {
		BDDMockito.given(resourceDAL.findByResourceName(BDDMockito.anyString())).willReturn(optionalResourceFirst);
		
		Resource resource = resourceService.getResourceByName(resourceName);
		
		assertNotNull(resource);
		assertTrue(resource instanceof Resource);
		testResource(resourceFirst, resource);
	}
	
	@Test 
	void should_returnResources_whenGetAllResource() {
		BDDMockito.given(resourceDAL.findAll()).willReturn(resources);
		
		List<Resource> resourcesResult = resourceService.getAll();
		
		assertNotNull(resourcesResult);
		assertEquals(resources.size(), resourcesResult.size());
		testResource(resourceFirst, resourcesResult.get(0));
		testResource(resourceSecond, resourcesResult.get(1));
	}
	
	@Test 
	void should_returnResource_whenAddCapability() {
		BDDMockito.given(resourceDAL.findById(BDDMockito.anyInt())).willReturn(optionalResourceFirst);
		BDDMockito.given(capabilityDAL.findById(capabilityFirst.getCapabilityId())).willReturn(optionalCapabilityFirst);
		BDDMockito.given(resourceDAL.save(BDDMockito.any(Resource.class))).willReturn(resourceFirst);
		
		Resource resource = resourceService.addCapability(resourceId, capabilityFirst.getCapabilityId());
		
		assertNotNull(resource);
		assertTrue(resource instanceof Resource);
		testResource(resourceFirst, resource);
	}
	
	@Test 
	void should_verify_whenDeleteCapability() {
		BDDMockito.given(resourceDAL.findById(BDDMockito.anyInt())).willReturn(optionalResourceFirst);
		BDDMockito.given(capabilityDAL.findById(capabilityFirst.getCapabilityId())).willReturn(optionalCapabilityFirst);
		
		resourceService.deleteCapability(resourceId, capabilityFirst.getCapabilityId());
		
		Mockito.verify(resourceDAL, Mockito.times(1)).save(resourceFirst);
	}
	
	@Test 
	void should_returnResources_whenGetAllCapabilitiesByResourceId() {
		BDDMockito.given(resourceDAL.findById(BDDMockito.anyInt())).willReturn(optionalResourceFirst);
		BDDMockito.given(capabilityDAL.findById(capabilityFirst.getCapabilityId())).willReturn(optionalCapabilityFirst);
		resourceService.addCapability(resourceId, capabilityFirst.getCapabilityId());
		
		Set<Capability> capabilitiesResult = resourceService.getAllCapabilitiesByResourceId(resourceId);
		
		assertNotNull(capabilitiesResult);
		assertEquals(capabilities.size(), capabilitiesResult.size());
	}
	
	private void testResource(Resource expectedObject, Resource actualObject) {
		assertEquals(expectedObject.getResourceId(), actualObject.getResourceId());
		assertEquals(expectedObject.getResourceName(), actualObject.getResourceName());
		assertEquals(expectedObject.getResourceDescription(), actualObject.getResourceDescription());
		assertEquals(expectedObject.getFullTimeEquivalentYearlyValue(), actualObject.getFullTimeEquivalentYearlyValue());
	}
}
