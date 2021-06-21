package com.bavostepbros.leap.domain.service.capabilityinformationservice;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.CapabilityInformation;
import com.bavostepbros.leap.domain.model.Information;
import com.bavostepbros.leap.domain.model.strategicimportance.StrategicImportance;
import com.bavostepbros.leap.domain.service.capabilityservice.CapabilityService;
import com.bavostepbros.leap.domain.service.informationservice.InformationService;
import com.bavostepbros.leap.persistence.CapabilityInformationDAL;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CapabilityInformationServiceImpl implements CapabilityInformationService {
	
	@Autowired
	private CapabilityInformationDAL capabilityInformationDAL;
	
	@Autowired
	private CapabilityService capabilityService;
	
	@Autowired
	private InformationService informationService;
	
	@Override
	public CapabilityInformation save(Integer capabilityId, Integer informationId, String criticality) {
		StrategicImportance importance = StrategicImportance.valueOf(criticality);
		Capability capability = capabilityService.get(capabilityId);
		Information information = informationService.get(informationId);
		CapabilityInformation capabilityInformation = new CapabilityInformation(capability, information, importance);
		return capabilityInformationDAL.save(capabilityInformation);
	}

	@Override
	public CapabilityInformation get(Integer capabilityId, Integer informationId) {
		Capability capability = capabilityService.get(capabilityId);
		Information information = informationService.get(informationId);
		Optional<CapabilityInformation> capabilityInformation = capabilityInformationDAL.findByCapabilityAndInformation(capability, information);
		capabilityInformation.orElseThrow(() -> new NullPointerException("CapabilityInformation does not exist."));
		return capabilityInformation.get();
	}

	@Override
	public CapabilityInformation update(Integer capabilityId, Integer informationId, String criticality) {
		StrategicImportance importance = StrategicImportance.valueOf(criticality);
		Capability capability = capabilityService.get(capabilityId);
		Information information = informationService.get(informationId);
		CapabilityInformation capabilityInformation = new CapabilityInformation(capability, information, importance);
		return capabilityInformationDAL.save(capabilityInformation);
	}

	@Override
	public void delete(Integer capabilityId, Integer informationId) {
		Capability capability = capabilityService.get(capabilityId);
		Information information = informationService.get(informationId);
		capabilityInformationDAL.deleteByCapabilityAndInformation(capability, information);
	}

	@Override
	public List<CapabilityInformation> getCapabilityInformationByCapability(Integer capabilityId) {
		Capability capability = capabilityService.get(capabilityId);
		return capabilityInformationDAL.findByCapability(capability);
	}

	@Override
	public List<CapabilityInformation> getCapabilityInformationByInformation(Integer informationId) {
		Information information = informationService.get(informationId);
		return capabilityInformationDAL.findByInformation(information);
	}

}
