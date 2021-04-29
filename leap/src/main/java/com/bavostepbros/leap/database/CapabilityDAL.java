package com.bavostepbros.leap.database;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bavostepbros.leap.model.Capability;

public interface CapabilityDAL extends JpaRepository<Capability, Integer> {
	
}
