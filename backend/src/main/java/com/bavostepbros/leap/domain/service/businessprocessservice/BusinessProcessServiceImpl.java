package com.bavostepbros.leap.domain.service.businessprocessservice;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.model.BusinessProcess;
import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.service.capabilityservice.CapabilityService;
import com.bavostepbros.leap.persistence.BusinessProcessDAL;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BusinessProcessServiceImpl implements BusinessProcessService {

	@Autowired
	private BusinessProcessDAL businessProcessDAL;
	
	@Lazy
	@Autowired
	private CapabilityService capabilityService;

	
	/** 
	 * Saves a new business process.
	 * @param businessProcessName The name of the business process to save.
	 * @param businessProcessDescription The description of the business process to save.
	 * @return BusinessProcess Returns the business process that is saved.
	 */
	@Override
	public BusinessProcess save(String businessProcessName, String businessProcessDescription) {
		BusinessProcess businessProcess = new BusinessProcess(businessProcessName, businessProcessDescription);
		return businessProcessDAL.save(businessProcess);
	}

	
	/** 
	 * Gets a business process from its ID.
	 * @param businessProcessId The ID of the business process you want to get.
	 * @return BusinessProcess Returns the retrieved business process.
	 */
	@Override
	public BusinessProcess get(Integer businessProcessId) {
		Optional<BusinessProcess> businessProcess = businessProcessDAL.findById(businessProcessId);
		businessProcess.orElseThrow(() -> new NullPointerException("Businessprocess does not exist."));
		return businessProcess.get();
	}

	
	/** 
	 * Updates a business process.
	 * @param businessProcessId The ID of the business process that is updated.
	 * @param businessProcessName The (new) name of the business process.
	 * @param businessProcessDescription The (new) description of the business process.
	 * @return BusinessProcess Returns the updated business process.
	 */
	@Override
	public BusinessProcess update(Integer businessProcessId, String businessProcessName,
			String businessProcessDescription) {
		BusinessProcess businessProcess = new BusinessProcess(businessProcessId, businessProcessName,
				businessProcessDescription);
		return businessProcessDAL.save(businessProcess);
	}

	
	/** 
	 * Deletes a business process from the ID.
	 * @param businessProcessId The ID of the business process you want to delete.
	 */
	@Override
	public void delete(Integer businessProcessId) {
		businessProcessDAL.deleteById(businessProcessId);
	}

	
	/** 
	 * Gets a business process by its name.
	 * @param businessProcessName The name of the business process you want to get.
	 * @return BusinessProcess Returns the retrieved business process.
	 */
	@Override
	public BusinessProcess getBusinessProcessByName(String businessProcessName) {
		Optional<BusinessProcess> businessProcess = businessProcessDAL.findByBusinessProcessName(businessProcessName);
		businessProcess.orElseThrow(() -> new NullPointerException("Businessprocess does not exist."));
		return businessProcess.get();
	}

	
	/** 
	 * Gets all business processes.
	 * @return List<BusinessProcess>
	 */
	@Override
	public List<BusinessProcess> getAll() {
		return businessProcessDAL.findAll();
	}

	
	/** 
	 * Adds a capability to a business process.
	 * @param businessProcessId The ID of the business process to which you want to add.
	 * @param capabilityId The ID of the capability you want to add.
	 */
	@Override
	public void addCapability(Integer businessProcessId, Integer capabilityId) {
		BusinessProcess businessProcess = get(businessProcessId);
		Capability capability = capabilityService.get(capabilityId);
		businessProcess.addCapability(capability);
		return;
	}

	
	/** 
	 * Deletes a capablity from a  business process.
	 * @param businessProcessId The ID of the business process form which you want to delete.
	 * @param capabilityId The ID of the capability you want to delete.
	 */
	@Override
	public void deleteCapability(Integer businessProcessId, Integer capabilityId) {
		BusinessProcess businessProcess = get(businessProcessId);
		Capability capability = capabilityService.get(capabilityId);
		businessProcess.addCapability(capability);
		return;
	}

	
	/** 
	 * Gets all capabilities linked to a business process, from its ID.
	 * @param businessProcessId The ID for which you want to get all capabilities.
	 * @return Set<Capability>
	 */
	@Override
	public Set<Capability> getAllCapabilitiesByBusinessProcessId(Integer businessProcessId) {
		BusinessProcess businessProcess = get(businessProcessId);
		return businessProcess.getCapabilities();
	}

}
