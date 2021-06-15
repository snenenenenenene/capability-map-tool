package com.bavostepbros.leap.domain.service.capabilityitemservice;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.customexceptions.EnumException;
import com.bavostepbros.leap.domain.customexceptions.ForeignKeyException;
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
	
	
	/** 
	 * @param capabilityId
	 * @param itemId
	 * @param strategicImportance
	 * @return CapabilityItem
	 */
	@Override
	public CapabilityItem save(Integer capabilityId, Integer itemId, String strategicImportance) {
		if (capabilityId == null || capabilityId.equals(0)) {
			throw new ForeignKeyException("Capability ID is invalid.");
		}
		if (itemId == null || itemId.equals(0)) {
			throw new ForeignKeyException("StrategyItem ID is invalid.");
		}
		if (Arrays.stream(StrategicImportance.values())
				.noneMatch((stratImportance) -> stratImportance.name().equals(strategicImportance))) {
			throw new EnumException("StrategicImportance is not valid.");
		}
				
		StrategicImportance importance = StrategicImportance.valueOf(strategicImportance);
		Capability capability = capabilityService.get(capabilityId);
		StrategyItem strategyItem = strategyItemService.get(itemId);
		CapabilityItem capabilityItem = new CapabilityItem(capability, strategyItem, importance);
		return capabilityItemDAL.save(capabilityItem);
	}

	
	/** 
	 * @param capabilityId
	 * @param itemId
	 * @return CapabilityItem
	 */
	@Override
	public CapabilityItem get(Integer capabilityId, Integer itemId) {
		if (capabilityId == null || capabilityId.equals(0)) {
			throw new ForeignKeyException("Capability ID is invalid.");
		}
		if (itemId == null || itemId.equals(0)) {
			throw new ForeignKeyException("StrategyItem ID is invalid.");
		}
		
		Capability capability = capabilityService.get(capabilityId);
		StrategyItem strategyItem = strategyItemService.get(itemId);
		CapabilityItem capabilityItem = capabilityItemDAL.findByCapabilityAndStrategyItem(capability, strategyItem).get();
		return capabilityItem;
	}

	
	/** 
	 * @param capabilityId
	 * @param itemId
	 * @param strategicImportance
	 * @return CapabilityItem
	 */
	@Override
	public CapabilityItem update(Integer capabilityId, Integer itemId, String strategicImportance) {
		if (capabilityId == null || capabilityId.equals(0)) {
			throw new ForeignKeyException("Capability ID is invalid.");
		}
		if (itemId == null || itemId.equals(0)) {
			throw new ForeignKeyException("StrategyItem ID is invalid.");
		}
		if (Arrays.stream(StrategicImportance.values())
				.noneMatch((stratImportance) -> stratImportance.name().equals(strategicImportance))) {
			throw new EnumException("StrategicImportance is not valid.");
		}
		
		StrategicImportance importance = StrategicImportance.valueOf(strategicImportance);
		Capability capability = capabilityService.get(capabilityId);
		StrategyItem strategyItem = strategyItemService.get(itemId);
		CapabilityItem capabilityItem = new CapabilityItem(capability, strategyItem, importance);
		return capabilityItemDAL.save(capabilityItem);
	}

	
	/** 
	 * @param capabilityId
	 * @param itemId
	 */
	@Override
	public void delete(Integer capabilityId, Integer itemId) {
		if (capabilityId == null || capabilityId.equals(0)) {
			throw new ForeignKeyException("Capability ID is invalid.");
		}
		if (itemId == null || itemId.equals(0)) {
			throw new ForeignKeyException("StrategyItem ID is invalid.");
		}
		
		Capability capability = capabilityService.get(capabilityId);
		StrategyItem strategyItem = strategyItemService.get(itemId);
		capabilityItemDAL.deleteByCapabilityAndStrategyItem(capability, strategyItem);
	}

	
	/** 
	 * @param itemId
	 * @return List<CapabilityItem>
	 */
	@Override
	public List<CapabilityItem> getCapabilityItemsByStrategyItem(Integer itemId) {
		if (itemId == null || itemId.equals(0)) {
			throw new ForeignKeyException("StrategyItem ID is invalid.");
		}
		
		StrategyItem strategyItem = strategyItemService.get(itemId);
		return capabilityItemDAL.findByStrategyItem(strategyItem);
	}

	
	/** 
	 * @param capabilityId
	 * @return List<CapabilityItem>
	 */
	@Override
	public List<CapabilityItem> getCapabilityItemsByCapability(Integer capabilityId) {
		Capability capability = capabilityService.get(capabilityId);
		return capabilityItemDAL.findByCapability(capability);
	}

}
