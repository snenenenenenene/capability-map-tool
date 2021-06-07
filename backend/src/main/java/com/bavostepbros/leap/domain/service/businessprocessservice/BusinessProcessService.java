package com.bavostepbros.leap.domain.service.businessprocessservice;

import java.util.List;
import java.util.Set;

import com.bavostepbros.leap.domain.model.BusinessProcess;
import com.bavostepbros.leap.domain.model.Capability;

public interface BusinessProcessService {
	BusinessProcess save(String businessProcessName, String businessProcessDescription);

	BusinessProcess get(Integer businessProcessId);

	BusinessProcess update(Integer businessProcessId, String businessProcessName, String businessProcessDescription);

	void delete(Integer businessProcessId);

	BusinessProcess getBusinessProcessByName(String businessProcessName);

	List<BusinessProcess> getAll();
	
	void addCapability(Integer businessProcessId, Integer capabilityId);

	void deleteCapability(Integer businessProcessId, Integer capabilityId);

	Set<Capability> getAllCapabilitiesByBusinessProcessId(Integer businessProcessId);
}
