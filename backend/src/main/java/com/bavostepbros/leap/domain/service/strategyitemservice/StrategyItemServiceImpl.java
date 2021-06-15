package com.bavostepbros.leap.domain.service.strategyitemservice;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.model.Strategy;
import com.bavostepbros.leap.domain.model.StrategyItem;
import com.bavostepbros.leap.domain.service.strategyservice.StrategyService;
import com.bavostepbros.leap.persistence.StrategyItemDAL;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StrategyItemServiceImpl implements StrategyItemService {
	
	@Autowired
	private StrategyItemDAL strategyItemDAL;
	
	@Autowired
	private StrategyService strategyService;

	
	/** 
	 * @param strategyId
	 * @param strategyItemName
	 * @param description
	 * @return StrategyItem
	 */
	@Override
	public StrategyItem save(Integer strategyId, String strategyItemName, String description) {
		Strategy strategy = strategyService.get(strategyId);
		StrategyItem strategyItem = new StrategyItem(strategy, strategyItemName, description);
		return strategyItemDAL.save(strategyItem);
	}

	
	/** 
	 * @param itemId
	 * @return StrategyItem
	 */
	@Override
	public StrategyItem get(Integer itemId) {
		StrategyItem strategyItem = strategyItemDAL.findById(itemId).get();
		return strategyItem;
	}
	
	
	/** 
	 * @param itemId
	 * @param strategyId
	 * @param strategyItemName
	 * @param description
	 * @return StrategyItem
	 */
	@Override
	public StrategyItem update(Integer itemId, Integer strategyId, String strategyItemName, String description) {
		Strategy strategy = strategyService.get(strategyId);
		StrategyItem strategyItem = new StrategyItem(itemId, strategy, strategyItemName, description);
		return strategyItemDAL.save(strategyItem);
	}

	
	/** 
	 * @param itemid
	 */
	@Override
	public void delete(Integer itemid) {
		strategyItemDAL.deleteById(itemid);
	}

	
	/** 
	 * @return List<StrategyItem>
	 */
	@Override
	public List<StrategyItem> getAll() {
		return strategyItemDAL.findAll();
	}

	
	/** 
	 * @param itemId
	 * @return boolean
	 */
	@Override
	public boolean existsById(Integer itemId) {
		return strategyItemDAL.existsById(itemId);
	}

	
	/** 
	 * @param strategyItemName
	 * @return boolean
	 */
	@Override
	public boolean existsByStrategyItemName(String strategyItemName) {
		return !strategyItemDAL.findByStrategyItemName(strategyItemName).isEmpty();
	}

	
	/** 
	 * @param strategyItemName
	 * @return StrategyItem
	 */
	@Override
	public StrategyItem getStrategyItemByStrategyItemName(String strategyItemName) {
		Optional<StrategyItem> strategyItem = strategyItemDAL.findByStrategyItemName(strategyItemName);
		strategyItem.orElseThrow(() -> new NullPointerException("StrategyItem does not exist"));
		return strategyItem.get();
	}

	
	/** 
	 * @param strategyId
	 * @return List<StrategyItem>
	 */
	@Override
	public List<StrategyItem> getStrategyItemsByStrategy(Integer strategyId) {
		Strategy strategy = strategyService.get(strategyId);
		return strategyItemDAL.findByStrategy(strategy);
	}

}
