package com.bavostepbros.leap.domain.service.capabilityservice;

import java.util.List;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.capabilitylevel.CapabilityLevel;

public interface CapabilityService {
	boolean save(Capability capability);
	Capability get(Integer id);
	List<Capability> getAll();
	void update(Capability capability);
	void delete(Integer id);
	boolean existsById(Integer id);
	boolean existsByCapabilityName(String capabilityName);
	List<Capability> getCapabilitiesByEnvironment(Integer environmentId);
	List<Capability> getCapabilitiesByLevel(CapabilityLevel level);
	List<Capability> getCapabilityChildren(Integer parentId);
	List<Capability> getCapabilitiesByParentIdAndLevel(Integer parentId, CapabilityLevel level);
}
