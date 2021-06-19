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

	@Override
	public BusinessProcess save(String businessProcessName, String businessProcessDescription) {
		BusinessProcess businessProcess = new BusinessProcess(businessProcessName, businessProcessDescription);
		return businessProcessDAL.save(businessProcess);
	}

	@Override
	public BusinessProcess get(Integer businessProcessId) {
		Optional<BusinessProcess> businessProcess = businessProcessDAL.findById(businessProcessId);
		businessProcess.orElseThrow(() -> new NullPointerException("Businessprocess does not exist."));
		return businessProcess.get();
	}

	@Override
	public BusinessProcess update(Integer businessProcessId, String businessProcessName,
			String businessProcessDescription) {
		BusinessProcess businessProcess = new BusinessProcess(businessProcessId, businessProcessName,
				businessProcessDescription);
		return businessProcessDAL.save(businessProcess);
	}

	@Override
	public void delete(Integer businessProcessId) {
		businessProcessDAL.deleteById(businessProcessId);
	}

	@Override
	public BusinessProcess getBusinessProcessByName(String businessProcessName) {
		Optional<BusinessProcess> businessProcess = businessProcessDAL.findByBusinessProcessName(businessProcessName);
		businessProcess.orElseThrow(() -> new NullPointerException("Businessprocess does not exist."));
		return businessProcess.get();
	}

	@Override
	public List<BusinessProcess> getAll() {
		return businessProcessDAL.findAll();
	}

	@Override
	public BusinessProcess addCapability(Integer businessProcessId, Integer capabilityId) {
		BusinessProcess businessProcess = get(businessProcessId);
		Capability capability = capabilityService.get(capabilityId);
		businessProcess.addCapability(capability);
		return businessProcessDAL.save(businessProcess);
	}

	@Override
	public void deleteCapability(Integer businessProcessId, Integer capabilityId) {
		BusinessProcess businessProcess = get(businessProcessId);
		Capability capability = capabilityService.get(capabilityId);
		businessProcess.removeCapability(capability);
		businessProcessDAL.save(businessProcess);
		return;
	}

	@Override
	public Set<Capability> getAllCapabilitiesByBusinessProcessId(Integer businessProcessId) {
		BusinessProcess businessProcess = get(businessProcessId);
		return businessProcess.getCapabilities();
	}

}
