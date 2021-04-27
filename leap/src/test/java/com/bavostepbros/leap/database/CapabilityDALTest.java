package com.bavostepbros.leap.database;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.bavostepbros.leap.model.Capability;
import com.bavostepbros.leap.model.Environment;
import com.bavostepbros.leap.model.Status;

@SpringBootTest
public class CapabilityDALTest {
	
	EnvironmentDAL environmentDAL;
	StatusDAL statusDAL;
	CapabilityDAL capabilityDAL;
	
	@BeforeEach
	public void init() {
		environmentDAL = new EnvironmentDAL();
    	capabilityDAL = new CapabilityDAL();
    	statusDAL = new StatusDAL();
	}

	@Test
	public void saveAndGetCapability() {
		Environment e = new Environment("Test environment");
        environmentDAL.saveEnvironment(e);
        
        Status s = new Status(60);
        statusDAL.saveStatus(s);
        
        Capability c = new Capability(e, s, 1, "test", 1, true, "test", 1, 1, 1);       
        capabilityDAL.saveCapability(c);
        Capability capability = capabilityDAL.getCapabilityById(c.getCapabilityId());
        
        assertNotNull(capability);
        assertEquals("Test environment", capability.getEnvironment().getEnvironmentName());
        assertEquals(60, capability.getStatus().getValidityPeriod());
        assertEquals(1, capability.getParentCapabilityId());
        assertEquals("test", capability.getCapabilityName());
        assertEquals(1, capability.getLevel());
        assertEquals(true, capability.getPaceOfChange());
        assertEquals("test", capability.getTargetOperatingModel());
        assertEquals(1, capability.getResourceQuality());
        assertEquals(1, capability.getInformationQuality());
        assertEquals(1, capability.getApplicationFit());
	}
	
	@Test
	public void updateCapability() {
		Environment e = new Environment("Test environment");
        environmentDAL.saveEnvironment(e);
        
        Status s = new Status(60);
        statusDAL.saveStatus(s);
        
        Capability c = new Capability(e, s, 1, "test", 1, true, "test", 1, 1, 1);       
        capabilityDAL.saveCapability(c);
        Capability capability = capabilityDAL.getCapabilityById(c.getCapabilityId());
        
        capabilityDAL.updateCapability(
        		capability.getCapabilityId(), 
        		e, s, 2, "Test", 2, false, "Test", 2, 2, 2);
        
        Capability updatedCapability = capabilityDAL.getCapabilityById(capability.getCapabilityId());
        
        assertNotNull(updatedCapability);
        assertEquals("Test environment", updatedCapability.getEnvironment().getEnvironmentName());
        assertEquals(60, updatedCapability.getStatus().getValidityPeriod());
        assertEquals(2, updatedCapability.getParentCapabilityId());
        assertEquals("Test", updatedCapability.getCapabilityName());
        assertEquals(2, updatedCapability.getLevel());
        assertEquals(false, updatedCapability.getPaceOfChange());
        assertEquals("Test", updatedCapability.getTargetOperatingModel());
        assertEquals(2, updatedCapability.getResourceQuality());
        assertEquals(2, updatedCapability.getInformationQuality());
        assertEquals(2, updatedCapability.getApplicationFit());
	}
	
	@Test
	public void deleteCapability() {
		Environment e = new Environment("Test environment");
        environmentDAL.saveEnvironment(e);
        
        Status s = new Status(60);
        statusDAL.saveStatus(s);
        
        Capability c = new Capability(e, s, 1, "test", 1, true, "test", 1, 1, 1);       
        capabilityDAL.saveCapability(c);
        Capability capability = capabilityDAL.getCapabilityById(c.getCapabilityId());
        Integer id = capability.getCapabilityId();
        
        capabilityDAL.deleteCapability(id);
        List<Capability> capabilities = capabilityDAL.getAllCapabilities();
        assertNull(capabilities.stream()
        	.filter((cap) -> id.equals(cap.getCapabilityId()))
        	.findAny()
        	.orElse(null));
	}

}
