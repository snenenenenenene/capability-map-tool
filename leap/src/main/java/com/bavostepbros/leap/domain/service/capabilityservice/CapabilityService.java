package com.bavostepbros.leap.domain.service.capabilityservice;

import java.util.List;

import com.bavostepbros.leap.domain.model.Capability;

public interface CapabilityService {
	boolean save(Capability capability);
	Capability get(Integer id);
	List<Capability> getAll();
	void update(Capability capability);
	void delete(Integer id);
	List<Capability> getCapabilitiesByEnvironment(Integer environmentId);
}
