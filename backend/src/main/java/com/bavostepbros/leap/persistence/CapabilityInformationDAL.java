package com.bavostepbros.leap.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.CapabilityInformation;
import com.bavostepbros.leap.domain.model.Information;

public interface CapabilityInformationDAL extends JpaRepository<CapabilityInformation, Integer> {
	Optional<CapabilityInformation> findByCapabilityAndInformation(Capability capability, Information information);
	
	void deleteByCapabilityAndInformation(Capability capability, Information information);
	
	List<CapabilityInformation> findByCapability(Capability capability);
}
