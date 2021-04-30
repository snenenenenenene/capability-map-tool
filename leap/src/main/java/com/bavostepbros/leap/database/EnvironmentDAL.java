package com.bavostepbros.leap.database;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bavostepbros.leap.model.Environment;

public interface EnvironmentDAL extends JpaRepository<Environment, Integer>{
	
}
