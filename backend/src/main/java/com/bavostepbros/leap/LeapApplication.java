package com.bavostepbros.leap;

import com.bavostepbros.leap.controller.RoleController;
import com.bavostepbros.leap.domain.model.Role;
import com.bavostepbros.leap.domain.model.User;
import com.bavostepbros.leap.domain.service.roleservice.RoleService;
import com.bavostepbros.leap.domain.service.roleservice.RoleServiceImpl;
import com.bavostepbros.leap.domain.service.userservice.UserService;
import com.bavostepbros.leap.domain.service.userservice.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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