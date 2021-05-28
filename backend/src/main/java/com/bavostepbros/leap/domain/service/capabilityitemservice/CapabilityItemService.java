package com.bavostepbros.leap.domain.service.capabilityitemservice;

import java.util.List;

import com.bavostepbros.leap.domain.model.CapabilityItem;

public interface CapabilityItemService {
	CapabilityItem save(Integer capabilityId, Integer itemId, String strategicImportance);
	
	CapabilityItem get(Integer capabilityId, Integer itemId);
	
	CapabilityItem update(Integer capabilityId, Integer itemId, String strategicImportance);
	
	void delete(Integer capabilityId, Integer itemId);
	
	List<CapabilityItem> getCapabilityItemsByStrategyItem(Integer itemId);
	
	List<CapabilityItem> getCapabilityItemsByCapability(Integer capabilityId);
}
