package com.bavostepbros.leap.database;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bavostepbros.leap.model.Capability;
import com.bavostepbros.leap.model.Environment;

public interface CapabilityDAL extends JpaRepository<Capability, Integer> {
	List<Capability> findByEnvironment(Environment environment);
}
