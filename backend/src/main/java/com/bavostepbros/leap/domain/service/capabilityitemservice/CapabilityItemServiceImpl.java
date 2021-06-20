package com.bavostepbros.leap.domain.service.capabilityitemservice;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.CapabilityItem;
import com.bavostepbros.leap.domain.model.StrategyItem;
import com.bavostepbros.leap.domain.model.strategicimportance.StrategicImportance;
import com.bavostepbros.leap.domain.service.capabilityservice.CapabilityService;
import com.bavostepbros.leap.domain.service.strategyitemservice.StrategyItemService;
import com.bavostepbros.leap.persistence.CapabilityItemDAL;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CapabilityItemServiceImpl implements CapabilityItemService {
	
	@Autowired
	private CapabilityItemDAL capabilityItemDAL;
	
	@Autowired
	private StrategyItemService strategyItemService;
	
	@Autowired
	private CapabilityService capabilityService;
	
	@Override
	public CapabilityItem save(Integer capabilityId, Integer itemId, String strategicImportance) {
		StrategicImportance importance = StrategicImportance.valueOf(strategicImportance);
		Capability capability = capabilityService.get(capabilityId);
		StrategyItem strategyItem = strategyItemService.get(itemId);
		CapabilityItem capabilityItem = new CapabilityItem(capability, strategyItem, importance);
		return capabilityItemDAL.save(capabilityItem);
	}

	@Override
	public CapabilityItem get(Integer capabilityId, Integer itemId) {
		Capability capability = capabilityService.get(capabilityId);
		StrategyItem strategyItem = strategyItemService.get(itemId);
		Optional<CapabilityItem> capabilityItem = capabilityItemDAL.findByCapabilityAndStrategyItem(capability, strategyItem);
		capabilityItem.orElseThrow(() -> new NullPointerException("CapabilityItem does not exist."));
		return capabilityItem.get();
	}

	@Override
	public CapabilityItem update(Integer capabilityId, Integer itemId, String strategicImportance) {
		StrategicImportance importance = StrategicImportance.valueOf(strategicImportance);
		Capability capability = capabilityService.get(capabilityId);
		StrategyItem strategyItem = strategyItemService.get(itemId);
		CapabilityItem capabilityItem = new CapabilityItem(capability, strategyItem, importance);
		return capabilityItemDAL.save(capabilityItem);
	}

	@Override
	public void delete(Integer capabilityId, Integer itemId) {
		Capability capability = capabilityService.get(capabilityId);
		StrategyItem strategyItem = strategyItemService.get(itemId);
		capabilityItemDAL.deleteByCapabilityAndStrategyItem(capability, strategyItem);
	}

	@Override
	public List<CapabilityItem> getCapabilityItemsByStrategyItem(Integer itemId) {
		StrategyItem strategyItem = strategyItemService.get(itemId);
		return capabilityItemDAL.findByStrategyItem(strategyItem);
	}

	@Override
	public List<CapabilityItem> getCapabilityItemsByCapability(Integer capabilityId) {
		Capability capability = capabilityService.get(capabilityId);
		return capabilityItemDAL.findByCapability(capability);
	}

}
