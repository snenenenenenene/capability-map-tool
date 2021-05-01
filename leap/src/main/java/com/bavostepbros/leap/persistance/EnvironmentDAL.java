package com.bavostepbros.leap.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bavostepbros.leap.domain.model.Environment;

public interface EnvironmentDAL extends JpaRepository<Environment, Integer>{
	
}
