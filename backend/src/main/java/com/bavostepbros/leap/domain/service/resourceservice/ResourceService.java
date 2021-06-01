package com.bavostepbros.leap.domain.service.resourceservice;

import java.util.List;

import com.bavostepbros.leap.domain.model.Resource;

public interface ResourceService {
	Resource save(String resourceName, String resourceDescription, Double fullTimeEquivalentYearlyValue);

	Resource get(Integer resourceId);

	Resource update(Integer resourceId, String resourceName, String resourceDescription,
			Double fullTimeEquivalentYearlyValue);
	
	void delete(Integer resourceId);
	
	Resource getResourceByName(String resourceName);
	
	List<Resource> getAll();
}
