package com.bavostepbros.leap.domain.service.resourceservice;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.Resource;
import com.bavostepbros.leap.domain.service.capabilityservice.CapabilityService;
import com.bavostepbros.leap.persistence.ResourceDAL;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

	@Autowired
	private ResourceDAL resourceDAL;
	
	@Autowired
	private CapabilityService capabilityService;

	@Override
	public Resource save(String resourceName, String resourceDescription, Double fullTimeEquivalentYearlyValue) {
		Resource resource = new Resource(resourceName, resourceDescription, fullTimeEquivalentYearlyValue);
		return resourceDAL.save(resource);
	}

	@Override
	public Resource get(Integer resourceId) {
		Optional<Resource> resource = resourceDAL.findById(resourceId);
		resource.orElseThrow(() -> new NullPointerException("Resource does not exists."));
		return resource.get();
	}

	@Override
	public Resource update(Integer resourceId, String resourceName, String resourceDescription,
			Double fullTimeEquivalentYearlyValue) {
		Resource resource = new Resource(resourceId, resourceName, resourceDescription, fullTimeEquivalentYearlyValue);
		return resourceDAL.save(resource);
	}

	@Override
	public void delete(Integer resourceId) {
		resourceDAL.deleteById(resourceId);
	}

	@Override
	public Resource getResourceByName(String resourceName) {
		Optional<Resource> resource = resourceDAL.findByResourceName(resourceName);
		resource.orElseThrow(() -> new NullPointerException("Resource does not exists."));
		return resource.get();
	}

	@Override
	public List<Resource> getAll() {
		return resourceDAL.findAll();
	}

	@Override
	public void addCapability(Integer resourceId, Integer capabilityId) {
		Resource resource = get(resourceId);
		Capability capability = capabilityService.get(capabilityId);
		resource.addCapability(capability);
		return;
	}

	@Override
	public void deleteCapability(Integer resourceId, Integer capabilityId) {
		Resource resource = get(resourceId);
		Capability capability = capabilityService.get(capabilityId);
		resource.removeCapability(capability);
		return;
	}

	@Override
	public List<Capability> getAllCapabilitiesByResourceId(Integer resourceId) {
		Resource resource = get(resourceId);
		return resource.getCapabilities();
	}

}
