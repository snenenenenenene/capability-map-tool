package com.bavostepbros.leap;

import com.bavostepbros.leap.database.CapabilityDAL;
import com.bavostepbros.leap.database.EnvironmentDAL;
import com.bavostepbros.leap.model.Capability;
import com.bavostepbros.leap.model.Environment;
import com.bavostepbros.leap.model.Status;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LeapApplication {
	static EnvironmentDAL environmentDAL;
    static CapabilityDAL capabilityDAL;

    public static void main(String[] args) {
        SpringApplication.run(LeapApplication.class, args);
        
        Environment e = new Environment("Test environment");
        Status s = new Status(60);
        Capability c = new Capability(e, s, 1, "test", 1, true, "test", 1, 1, 1);
        
        environmentDAL.saveEnvironment(e);
        capabilityDAL.saveCapability(c);
        System.out.println(capabilityDAL.getCapabilityById(1));
    }
}