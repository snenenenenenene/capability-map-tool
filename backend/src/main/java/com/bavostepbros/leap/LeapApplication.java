package com.bavostepbros.leap;

import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

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