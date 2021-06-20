package com.bavostepbros.leap.domain.service.capabilityapplicationservice;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.CapabilityApplication;
import com.bavostepbros.leap.domain.model.ITApplication;
import com.bavostepbros.leap.domain.service.capabilityservice.CapabilityService;
import com.bavostepbros.leap.domain.service.itapplicationservice.ITApplicationService;
import com.bavostepbros.leap.persistence.CapabilityApplicationDAL;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CapabilityApplicationServiceImpl implements CapabilityApplicationService {
	
	@Autowired
	private CapabilityApplicationDAL capabilityApplicationDAL;
	
	@Autowired
	private CapabilityService capabilityService;
	
	@Autowired
	private ITApplicationService itApplicationService;
	
	@Override
	public CapabilityApplication save(Integer capabilityId, Integer applicationId, Integer efficiencySupport,
			Integer functionalCoverage, Integer correctnessBusinessFit, Integer futurePotential, Integer completeness,
			Integer correctnessInformationFit, Integer availability) {
		Capability capability = capabilityService.get(capabilityId);
		ITApplication itApplication = itApplicationService.get(applicationId);
		CapabilityApplication capabilityApplication = new CapabilityApplication(capability, itApplication, 0, 
				efficiencySupport, functionalCoverage, correctnessBusinessFit, futurePotential, completeness,
				correctnessInformationFit, availability);
		return capabilityApplicationDAL.save(capabilityApplication);
	}

	@Override
	public CapabilityApplication get(Integer capabilityId, Integer applicationId) {
		Capability capability = capabilityService.get(capabilityId);
		ITApplication itApplication = itApplicationService.get(applicationId);
		Optional<CapabilityApplication> capabilityApplication = capabilityApplicationDAL
				.findByCapabilityAndApplication(capability, itApplication);
		capabilityApplication.orElseThrow(() -> new NullPointerException("CapabilityApplication does not exist."));
		return capabilityApplication.get();
	}

	@Override
	public CapabilityApplication update(Integer capabilityId, Integer applicationId, Integer efficiencySupport,
			Integer functionalCoverage, Integer correctnessBusinessFit, Integer futurePotential, Integer completeness,
			Integer correctnessInformationFit, Integer availability) {
		Capability capability = capabilityService.get(capabilityId);
		ITApplication itApplication = itApplicationService.get(applicationId);
		CapabilityApplication capabilityApplication = new CapabilityApplication(capability, itApplication, 0, 
				efficiencySupport, functionalCoverage, correctnessBusinessFit, futurePotential, completeness,
				correctnessInformationFit, availability);
		return capabilityApplicationDAL.save(capabilityApplication);
	}

	@Override
	public void delete(Integer capabilityId, Integer applicationId) {
		Capability capability = capabilityService.get(capabilityId);
		ITApplication itApplication = itApplicationService.get(applicationId);
		capabilityApplicationDAL.deleteByCapabilityAndApplication(capability, itApplication);
		return;
	}

	@Override
	public List<CapabilityApplication> getCapabilityApplicationsByCapability(Integer capabilityId) {
		Capability capability = capabilityService.get(capabilityId);
		return capabilityApplicationDAL.findByCapability(capability);
	}

	@Override
	public List<CapabilityApplication> getCapabilityApplicationsByApplication(Integer applicationId) {
		ITApplication itApplication = itApplicationService.get(applicationId);
		return capabilityApplicationDAL.findByApplication(itApplication);
	}

}
