package com.bavostepbros.leap;

import com.bavostepbros.leap.domain.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.bavostepbros.leap.persistence")
public class LeapApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeapApplication.class, args);
    }
}