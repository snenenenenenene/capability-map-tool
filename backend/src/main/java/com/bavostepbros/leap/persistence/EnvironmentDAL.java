package com.bavostepbros.leap.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bavostepbros.leap.domain.model.Environment;

/**
*
* @author Bavo Van Meel
*
*/
public interface EnvironmentDAL extends JpaRepository<Environment, Integer>{
	Optional<Environment> findByEnvironmentName(String environmentName);
}
