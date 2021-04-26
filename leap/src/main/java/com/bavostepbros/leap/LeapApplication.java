package com.bavostepbros.leap;

import com.bavostepbros.leap.database.CapabilityDAL;
import com.bavostepbros.leap.model.Capability;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LeapApplication {
    static CapabilityDAL capabilityDAL;

    public static void main(String[] args) {
        SpringApplication.run(LeapApplication.class, args);

        Capability c = new Capability(1, 1, 1, "test", 1, true, null, 1, 1, 1);
        capabilityDAL.saveCapability(c);
        System.out.println(capabilityDAL.getCapabilityById(id));
    }
}
