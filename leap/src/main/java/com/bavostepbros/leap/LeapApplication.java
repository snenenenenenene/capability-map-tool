package com.bavostepbros.leap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.bavostepbros.leap.database")
public class LeapApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeapApplication.class, args);       
        
    }
}