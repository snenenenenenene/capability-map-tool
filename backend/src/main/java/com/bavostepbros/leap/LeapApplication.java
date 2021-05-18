package com.bavostepbros.leap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.bavostepbros.leap.domain.model.capabilitylevel.CapabilityLevel;

@SpringBootApplication
@EnableJpaRepositories("com.bavostepbros.leap.persistence")
public class LeapApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeapApplication.class, args);
    }
}