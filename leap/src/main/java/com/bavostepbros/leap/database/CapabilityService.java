package com.bavostepbros.leap.database;

import java.util.List;

import com.bavostepbros.leap.model.Capability;

public interface CapabilityService {
	boolean save(Capability capability);
	Capability get(Integer id);
	List<Capability> getAll();
	void update(Capability capability);
	void delete(Integer id);
	List<Capability> getCapabilitiesByEnvironment(Integer environmentId);
}
