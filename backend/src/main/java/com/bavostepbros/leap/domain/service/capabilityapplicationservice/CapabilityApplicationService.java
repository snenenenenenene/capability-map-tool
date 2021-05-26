package com.bavostepbros.leap.domain.service.capabilityapplicationservice;

import java.util.List;

import com.bavostepbros.leap.domain.model.CapabilityApplication;

public interface CapabilityApplicationService {
	CapabilityApplication save(Integer capabilityId, Integer applicationId, Integer efficiencySupport, 
			Integer functionalCoverage, Integer correctnessBusinessFit, Integer futurePotential,
			Integer completeness, Integer correctnessInformationFit, Integer availability);
	
	CapabilityApplication get(Integer capabilityId, Integer applicationId);
	
	CapabilityApplication update(Integer capabilityId, Integer applicationId, Integer efficiencySupport, 
			Integer functionalCoverage, Integer correctnessBusinessFit, Integer futurePotential,
			Integer completeness, Integer correctnessInformationFit, Integer availability);
	
	void delete(Integer capabilityId, Integer applicationId);
	
	List<CapabilityApplication> getCapabilityApplicationsByCapability(Integer capabilityId);
}
