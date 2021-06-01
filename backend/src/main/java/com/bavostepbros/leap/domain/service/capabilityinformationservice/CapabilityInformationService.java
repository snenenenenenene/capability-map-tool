package com.bavostepbros.leap.domain.service.capabilityinformationservice;

import java.util.List;

import com.bavostepbros.leap.domain.model.CapabilityInformation;

public interface CapabilityInformationService {
	CapabilityInformation save(Integer capabilityId, Integer informationId, String criticality);
	
	CapabilityInformation get(Integer capabilityId, Integer informationId);
	
	CapabilityInformation update(Integer capabilityId, Integer informationId, String criticality);
	
	void delete(Integer capabilityId, Integer informationId);
	
	List<CapabilityInformation> getCapabilityInformationByCapability(Integer capabilityId);
}
