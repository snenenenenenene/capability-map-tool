package com.bavostepbros.leap.domain.service.capabilityapplicationservice;

import java.util.List;

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
	
	
	/** 
	 * @param capabilityId
	 * @param applicationId
	 * @param efficiencySupport
	 * @param functionalCoverage
	 * @param correctnessBusinessFit
	 * @param futurePotential
	 * @param completeness
	 * @param correctnessInformationFit
	 * @param availability
	 * @return CapabilityApplication
	 */
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

	
	/** 
	 * @param capabilityId
	 * @param applicationId
	 * @return CapabilityApplication
	 */
	@Override
	public CapabilityApplication get(Integer capabilityId, Integer applicationId) {
		Capability capability = capabilityService.get(capabilityId);
		ITApplication itApplication = itApplicationService.get(applicationId);
		return capabilityApplicationDAL.findByCapabilityAndApplication(capability, itApplication).get();
	}

	
	/** 
	 * @param capabilityId
	 * @param applicationId
	 * @param efficiencySupport
	 * @param functionalCoverage
	 * @param correctnessBusinessFit
	 * @param futurePotential
	 * @param completeness
	 * @param correctnessInformationFit
	 * @param availability
	 * @return CapabilityApplication
	 */
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

	
	/** 
	 * @param capabilityId
	 * @param applicationId
	 */
	@Override
	public void delete(Integer capabilityId, Integer applicationId) {
		Capability capability = capabilityService.get(capabilityId);
		ITApplication itApplication = itApplicationService.get(applicationId);
		capabilityApplicationDAL.deleteByCapabilityAndApplication(capability, itApplication);
		return;
	}

	
	/** 
	 * @param capabilityId
	 * @return List<CapabilityApplication>
	 */
	@Override
	public List<CapabilityApplication> getCapabilityApplicationsByCapability(Integer capabilityId) {
		Capability capability = capabilityService.get(capabilityId);
		return capabilityApplicationDAL.findByCapability(capability);
	}

	
	/** 
	 * @param applicationId
	 * @return List<CapabilityApplication>
	 */
	@Override
	public List<CapabilityApplication> getCapabilityApplicationsByApplication(Integer applicationId) {
		ITApplication itApplication = itApplicationService.get(applicationId);
		return capabilityApplicationDAL.findByApplication(itApplication);
	}

}
