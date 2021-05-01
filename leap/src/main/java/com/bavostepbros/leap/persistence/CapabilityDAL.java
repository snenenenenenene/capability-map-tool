package com.bavostepbros.leap.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Environment;

public interface CapabilityDAL extends JpaRepository<Capability, Integer> {
	List<Capability> findByEnvironment(Environment environment);
}
