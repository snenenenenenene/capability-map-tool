package com.bavostepbros.leap.domain.service.strategyservice;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.Strategy;
import com.bavostepbros.leap.domain.service.environmentservice.EnvironmentService;
import com.bavostepbros.leap.domain.service.statusservice.StatusService;
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
	private EnvironmentService environmentService;

	@Autowired
	private StatusService statusService;

	
	/** 
	 * @param statusId
	 * @param strategyName
	 * @param timeFrameStart
	 * @param timeFrameEnd
	 * @param environmentId
	 * @return Strategy
	 */
	@Override
	public Strategy save(Integer statusId, String strategyName, LocalDate timeFrameStart, LocalDate timeFrameEnd,
			Integer environmentId) {    	
		Status status = statusService.get(statusId);
		Environment environment = environmentService.get(environmentId);
		Strategy strategy = new Strategy(status, strategyName, timeFrameStart, timeFrameEnd, environment);
		Strategy savedStrategy = strategyDAL.save(strategy);
		return savedStrategy;
	}

	
	/** 
	 * @param id
	 * @return Strategy
	 */
	@Override
	public Strategy get(Integer id) {
		Strategy strategy = strategyDAL.findById(id).get();
		return strategy;
	}

	
	/** 
	 * @return List<Strategy>
	 */
	@Override
	public List<Strategy> getAll() {
		List<Strategy> strategies = strategyDAL.findAll();
		return strategies;
	}

	
	/** 
	 * @param strategyId
	 * @param statusId
	 * @param strategyName
	 * @param timeFrameStart
	 * @param timeFrameEnd
	 * @param environmentId
	 * @return Strategy
	 */
	@Override
	public Strategy update(Integer strategyId, Integer statusId, String strategyName, LocalDate timeFrameStart,
			LocalDate timeFrameEnd, Integer environmentId) {
		Status status = statusService.get(statusId);
		Environment environment = environmentService.get(environmentId);
		Strategy strategy = new Strategy(strategyId, status, strategyName, timeFrameStart, timeFrameEnd, environment);
		Strategy updatedStrategy = strategyDAL.save(strategy);
		return updatedStrategy;
	}

	
	/** 
	 * @param id
	 */
	@Override
	public void delete(Integer id) {
		strategyDAL.deleteById(id);
	}

	
	/** 
	 * @param id
	 * @return boolean
	 */
	@Override
	public boolean existsById(Integer id) {
		return strategyDAL.existsById(id);
	}

	
	/** 
	 * @param strategyName
	 * @return boolean
	 */
	@Override
	public boolean existsByStrategyName(String strategyName) {
		return strategyDAL.findByStrategyName(strategyName).isPresent();
	}

	
	/** 
	 * @param environmentId
	 * @return List<Strategy>
	 */
	@Override
	public List<Strategy> getStrategiesByEnvironment(Integer environmentId) {
		Environment environment = environmentService.get(environmentId);
		List<Strategy> strategies = strategyDAL.findByEnvironment(environment);
		return strategies;
	}

	
	/** 
	 * @param strategyName
	 * @return Strategy
	 */
	@Override
	public Strategy getStrategyByStrategyname(String strategyName) {
    	Optional<Strategy> strategy = strategyDAL.findByStrategyName(strategyName);
    	strategy.orElseThrow(() -> new NullPointerException("Strategy does not exist"));
		return strategy.get();
	}

}
