package com.bavostepbros.leap.domain.service.strategyitemservice;

import java.util.List;

import com.bavostepbros.leap.domain.model.StrategyItem;

public interface StrategyItemService {
	StrategyItem save(Integer strategyId, String strategyItemName, 
			String description);
	
	StrategyItem get(Integer itemId);
	
	StrategyItem update(Integer itemId, Integer strategyId, 
			String strategyItemName, String description);
	
	void delete(Integer itemid);
	
	List<StrategyItem> getAll();
	
	boolean existsById(Integer itemId);

	boolean existsByStrategyItemName(String strategyItemName);
	
	StrategyItem getStrategyItemByStrategyItemName(String strategyItemName);
	
	List<StrategyItem> getStrategyItemsByStrategy(Integer strategyId);
}
