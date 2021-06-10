package com.bavostepbros.leap.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.CapabilityApplication;
import com.bavostepbros.leap.domain.model.CapabilityApplicationId;
import com.bavostepbros.leap.domain.model.ITApplication;

public interface CapabilityApplicationDAL extends JpaRepository<CapabilityApplication, CapabilityApplicationId> {
	Optional<CapabilityApplication> findByCapabilityAndApplication(Capability capability, ITApplication itApplication);

	List<CapabilityApplication> findByCapability(Capability capability);

	void deleteByCapabilityAndApplication(Capability capability, ITApplication itApplication);
	
	List<CapabilityApplication> findByApplication(ITApplication itApplication);
	
}
