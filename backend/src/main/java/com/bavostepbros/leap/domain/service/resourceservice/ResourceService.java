package com.bavostepbros.leap.domain.service.resourceservice;

import java.util.List;
import java.util.Set;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Resource;

public interface ResourceService {
	Resource save(String resourceName, String resourceDescription, Double fullTimeEquivalentYearlyValue);

	Resource get(Integer resourceId);

	Resource update(Integer resourceId, String resourceName, String resourceDescription,
			Double fullTimeEquivalentYearlyValue);
	
	void delete(Integer resourceId);
	
	Resource getResourceByName(String resourceName);
	
	List<Resource> getAll();
	
	Resource addCapability(Integer resourceId, Integer capabilityId);

	void deleteCapability(Integer resourceId, Integer capabilityId);

	Set<Capability> getAllCapabilitiesByResourceId(Integer resourceId);
}
