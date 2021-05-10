package com.bavostepbros.leap.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.capabilitylevel.CapabilityLevel;

/**
*
* @author Bavo Van Meel
*
*/
public interface CapabilityDAL extends JpaRepository<Capability, Integer> {
	List<Capability> findByEnvironment(Environment environment);
	List<Capability> findByLevel(CapabilityLevel level);
	List<Capability> findByParentCapabilityId(Integer parentId);
	List<Capability> findByParentCapabilityIdAndLevel(Integer parentId, CapabilityLevel level);
	List<Capability> findByCapabilityName(String capabilityName);
}
