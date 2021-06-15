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
	 * Saves a new capabilityApplication
	 * A capablityApplication is a link between a capability and a IT application.
	 * @param capabilityId The ID of the capability which is linked.
	 * @param applicationId The ID of the application which is linked.
	 * @param efficiencySupport The score of how efficient the support is.
	 * @param functionalCoverage The score of how the functional coverage is.
	 * @param correctnessBusinessFit The score of how the business fit and how correct it is.
	 * @param futurePotential The score of how there is future potential.
	 * @param completeness The score of how complete the capabilityApplication is.
	 * @param correctnessInformationFit The score of how correct the information fit is.
	 * @param availability The availability score.
	 * @return CapabilityApplication Returns the newly saved capabilityApplication.
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
	 * Gets a capabilityApplication link from the capability and application ID's which form the link.
	 * @param capabilityId The capability ID of the link.
	 * @param applicationId The application ID of the link.
	 * @return CapabilityApplication Returns the retrieved capabilityApplication.
	 */
	@Override
	public CapabilityApplication get(Integer capabilityId, Integer applicationId) {
		Capability capability = capabilityService.get(capabilityId);
		ITApplication itApplication = itApplicationService.get(applicationId);
		return capabilityApplicationDAL.findByCapabilityAndApplication(capability, itApplication).get();
	}

	
	/** 
	 * Updates a capabilityApplication.
	 * @param capabilityId The ID of the capability which forms the link.
	 * @param applicationId The ID of the application which forms the link.
	 * @param efficiencySupport The (new) efficiencySupport score.
	 * @param functionalCoverage The (new) functionalCoverage score.
	 * @param correctnessBusinessFit The (new) correctnessBusinessFit score.
	 * @param futurePotential The (new) futurePotential score.
	 * @param completeness The (new) completeness score.
	 * @param correctnessInformationFit The (new) correctnessInformationFit score.
	 * @param availability The (new) availability score.
	 * @return CapabilityApplication Returns the updated capabilityApplication.
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
	 * Deletes a capabilityApplication using the capability and application ID's which form the link.
	 * @param capabilityId The ID of the capability forming the link.
	 * @param applicationId The ID of the application forming the link.
	 */
	@Override
	public void delete(Integer capabilityId, Integer applicationId) {
		Capability capability = capabilityService.get(capabilityId);
		ITApplication itApplication = itApplicationService.get(applicationId);
		capabilityApplicationDAL.deleteByCapabilityAndApplication(capability, itApplication);
		return;
	}

	
	/** 
	 * Gets all capabilityApplications linked to a capability using the capability ID.
	 * @param capabilityId The ID of the capability.
	 * @return List<CapabilityApplication> Returns the list of capabilityApplications.
	 */
	@Override
	public List<CapabilityApplication> getCapabilityApplicationsByCapability(Integer capabilityId) {
		Capability capability = capabilityService.get(capabilityId);
		return capabilityApplicationDAL.findByCapability(capability);
	}

	
	/** 
	 * Gets all capabilityApplications linked to a application using the application ID.
	 * @param applicationId The ID of the application.
	 * @return List<CapabilityApplication> Returns a list of capabilityApplications.
	 */
	@Override
	public List<CapabilityApplication> getCapabilityApplicationsByApplication(Integer applicationId) {
		ITApplication itApplication = itApplicationService.get(applicationId);
		return capabilityApplicationDAL.findByApplication(itApplication);
	}

}
