package com.bavostepbros.leap.domain.service.strategyitemservice;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.ForeignKeyException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
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

	@Override
	public StrategyItem save(Integer strategyId, String strategyItemName, String description) {
		if (strategyItemName == null || strategyItemName.isBlank() || strategyItemName.isEmpty()) {
			throw new InvalidInputException("Invalid input.");
		}
		if (strategyId == null || strategyId.equals(0)) {
			throw new InvalidInputException("Environment ID is invalid.");
		}
		if (existsByStrategyItemName(strategyItemName)) {
			throw new DuplicateValueException("Strategy item name already exists.");
		}
		if (!strategyService.existsById(strategyId)) {
			throw new ForeignKeyException("Strategy ID does not exists.");
		}
		
		Strategy strategy = strategyService.get(strategyId);
		StrategyItem strategyItem = new StrategyItem(strategy, strategyItemName, description);
		return strategyItemDAL.save(strategyItem);
	}

	@Override
	public StrategyItem get(Integer itemId) {
		if (itemId == null || itemId.equals(0)) {
			throw new InvalidInputException("Strategy ID is not valid.");
		}
		if (!existsById(itemId)) {
			throw new IndexDoesNotExistException("Strategy ID does not exists.");
		}
		
		StrategyItem strategyItem = strategyItemDAL.findById(itemId).get();
		return strategyItem;
	}
	
	@Override
	public StrategyItem update(Integer itemId, Integer strategyId, String strategyItemName, String description) {
		if (itemId == null || itemId.equals(0) || strategyItemName == null || 
				strategyItemName.isBlank() || strategyItemName.isEmpty()) {
			throw new InvalidInputException("Invalid input.");
		}
		if (strategyId == null || strategyId.equals(0)) {
			throw new InvalidInputException("Environment ID is invalid.");
		}
		if (!strategyService.existsById(strategyId)) {
			throw new ForeignKeyException("Strategy ID does not exists.");
		}
		
		Strategy strategy = strategyService.get(strategyId);
		StrategyItem strategyItem = new StrategyItem(itemId, strategy, strategyItemName, description);
		return strategyItemDAL.save(strategyItem);
	}

	@Override
	public void delete(Integer itemid) {
		if (itemid == null || itemid.equals(0)) {
			throw new InvalidInputException("Strategy ID is not valid.");
		}
		strategyItemDAL.deleteById(itemid);
	}

	@Override
	public List<StrategyItem> getAll() {
		return strategyItemDAL.findAll();
	}

	@Override
	public boolean existsById(Integer itemId) {
		return strategyItemDAL.existsById(itemId);
	}

	@Override
	public boolean existsByStrategyItemName(String strategyItemName) {
		return !strategyItemDAL.findByStrategyItemName(strategyItemName).isEmpty();
	}

	@Override
	public StrategyItem getStrategyItemByStrategyItemName(String strategyItemName) {
		if (strategyItemName == null || strategyItemName.isBlank() || 
				strategyItemName.isEmpty()) {
			throw new InvalidInputException("Invalid input.");
		}
		return strategyItemDAL.findByStrategyItemName(strategyItemName).get();
	}

	@Override
	public List<StrategyItem> getStrategyItemsByStrategy(Integer strategyId) {
		if (strategyId == null || strategyId.equals(0)) {
			throw new InvalidInputException("Strategy ID is not valid.");
		}
		if (!strategyService.existsById(strategyId)) {
			throw new ForeignKeyException("Strategy ID does not exists.");
		}
		
		Strategy strategy =strategyService.get(strategyId);
		return strategyItemDAL.findByStrategy(strategy);
	}

}
