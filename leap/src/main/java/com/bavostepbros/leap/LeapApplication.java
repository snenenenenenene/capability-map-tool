package com.bavostepbros.leap;

import com.bavostepbros.leap.database.CapabilityDAL;
import com.bavostepbros.leap.database.EnvironmentDAL;
import com.bavostepbros.leap.database.StatusDAL;
import com.bavostepbros.leap.model.Capability;
import com.bavostepbros.leap.model.Environment;
import com.bavostepbros.leap.model.Status;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LeapApplication {
	static EnvironmentDAL environmentDAL;
    static CapabilityDAL capabilityDAL;
    static StatusDAL statusDAL;

    public static void main(String[] args) {
        SpringApplication.run(LeapApplication.class, args);
        
        environmentDAL = new EnvironmentDAL();
    	capabilityDAL = new CapabilityDAL();
    	statusDAL = new StatusDAL();
        
        Environment e = new Environment("Test environment");
        environmentDAL.saveEnvironment(e);
        
        Status s = new Status(60);
        statusDAL.saveStatus(s);
        
        Capability c = new Capability(e, s, 1, "test", 1, true, "test", 1, 1, 1);       
        capabilityDAL.saveCapability(c);
        System.out.println(capabilityDAL.getCapabilityById(c.getCapabilityId()));
        
        capabilityDAL.updateCapability(
        		c.getCapabilityId(), 
        		e, s, 2, "Test", 2, false, "Test", 2, 2, 2);
        System.out.println(capabilityDAL.getCapabilityById(c.getCapabilityId()));
    }
}