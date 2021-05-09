package com.bavostepbros.leap.domain.service.StrategyService;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

import com.bavostepbros.leap.domain.customexceptions.DuplicateValueException;
import com.bavostepbros.leap.domain.customexceptions.ForeignKeyException;
import com.bavostepbros.leap.domain.customexceptions.IndexDoesNotExistException;
import com.bavostepbros.leap.domain.customexceptions.InvalidInputException;
import com.bavostepbros.leap.domain.customexceptions.StrategyException;
import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.Strategy;
import com.bavostepbros.leap.domain.service.environmentservice.EnvironmentService;
import com.bavostepbros.leap.domain.service.statusservice.StatusService;
import com.bavostepbros.leap.persistence.EnvironmentDAL;
import com.bavostepbros.leap.persistence.StrategyDAL;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author Lenny Bontenakel, Bavo Van Meel
 *
 */
@Service
@Transactional
@RequiredArgsConstructor
public class StrategyServiceImpl implements StrategyService {

	@Autowired
	private StrategyDAL strategyDAL;

	@Autowired
	private EnvironmentDAL environmentDAL;

	@Autowired
	private EnvironmentService environmentService;

	@Autowired
	private StatusService statusService;

	@Override
	public Strategy save(Integer statusId, String strategyName, LocalDate timeFrameStart, LocalDate timeFrameEnd,
			Integer environmentId) {
		if (statusId == null || statusId.equals(0) || strategyName == null 
    			|| strategyName.isBlank() || strategyName.isEmpty()) {
    		throw new InvalidInputException("Invalid input.");
    	}
    	if (!existsByStrategyName(strategyName)) {
			throw new DuplicateValueException("Strategy name already exists.");
		}
    	if (!statusService.existsById(statusId)) {
			throw new ForeignKeyException("Status ID does not exists.");
		}
		if (!environmentService.existsById(environmentId)) {
			throw new ForeignKeyException("Environment ID does not exists.");
		}
    	
		Status status = statusService.get(statusId);
		Environment environment = environmentService.get(environmentId);
		Strategy strategy = new Strategy(status, strategyName, timeFrameStart, timeFrameEnd, environment);
		Strategy savedStrategy = strategyDAL.save(strategy);
		return savedStrategy;
	}

	@Override
	public Strategy get(Integer id) {
		if (id == null || id.equals(0)) {
			throw new InvalidInputException("Strategy ID is not valid.");
		}
		if (!existsById(id)) {
			throw new IndexDoesNotExistException("Strategy ID does not exists.");
		}
		
		Strategy strategy = strategyDAL.findById(id).get();
		return strategy;
	}

	@Override
	public List<Strategy> getAll() {
		List<Strategy> strategies = strategyDAL.findAll();
		return strategies;
	}

	@Override
	public Strategy update(Integer strategyId, Integer statusId, String strategyName, LocalDate timeFrameStart,
			LocalDate timeFrameEnd, Integer environmentId) {
		if (strategyId == null || strategyId.equals(0) || strategyName == null || 
    			strategyName.isBlank() || strategyName.isEmpty()) {
			throw new InvalidInputException("Invalid input.");
		}
		if (!existsById(strategyId)) {
			throw new StrategyException("Can not update strategy if it does not exist.");
		}
		if (existsByStrategyName(strategyName)) {
			throw new DuplicateValueException("Strategy name already exists.");
		}
		if (!statusService.existsById(statusId)) {
			throw new ForeignKeyException("Status ID does not exists.");
		}
		if (!environmentService.existsById(environmentId)) {
			throw new ForeignKeyException("Environment ID does not exists.");
		}
		
		Status status = statusService.get(statusId);
		Environment environment = environmentService.get(environmentId);
		Strategy strategy = new Strategy(strategyId, status, strategyName, timeFrameStart, timeFrameEnd, environment);
		Strategy updatedStrategy = strategyDAL.save(strategy);
		return updatedStrategy;
	}

	@Override
	public void delete(Integer id) {
		if (id == null || id.equals(0)) {
			throw new InvalidInputException("Strategy ID is not valid.");
		}
		if (!existsById(id)) {
			throw new IndexDoesNotExistException("Strategy ID does not exists.");
		}
		strategyDAL.deleteById(id);
	}

	@Override
	public boolean existsById(Integer id) {
		return strategyDAL.existsById(id);
	}

	@Override
	public boolean existsByStrategyName(String strategyName) {
		return strategyDAL.findByStrategyName(strategyName).isEmpty();
	}

	@Override
	public List<Strategy> getStrategiesByEnvironment(Integer environmentId) {
		if (environmentId == null || environmentId.equals(0)) {
			throw new InvalidInputException("Environment ID is not valid.");
		}
		if (!environmentService.existsById(environmentId)) {
			throw new ForeignKeyException("Environment ID does not exists.");
		}
		
		Environment environment = environmentDAL.findById(environmentId).get();
		List<Strategy> strategies = strategyDAL.findByEnvironment(environment);
		return strategies;
	}

}
