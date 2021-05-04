package com.bavostepbros.leap.domain.service.StrategyService;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.Strategy;
import com.bavostepbros.leap.persistence.StrategyDAL;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StrategyServiceImpl implements StrategyService {

	@Autowired
	private StrategyDAL strategyDAL;

	@Override
	public Strategy save(Integer statusId, LocalDate validityPeriod, String strategyName, LocalDate timeFrameStart,
			LocalDate timeFrameEnd, Integer environmentId, String environmentName) {
		Status status = new Status(statusId, validityPeriod);
		Environment environment = new Environment(environmentId, environmentName);
		Strategy strategy = new Strategy(status, strategyName, timeFrameStart, timeFrameEnd, environment);
		Strategy savedStrategy = strategyDAL.save(strategy);
		return savedStrategy;
	}

	@Override
	public Strategy get(Integer id) {
		Strategy strategy = strategyDAL.findById(id).get();
		return strategy;
	}

	@Override
	public List<Strategy> getAll() {
		List<Strategy> strategies = strategyDAL.findAll();
		return strategies;
	}

	@Override
	public Strategy update(Integer strategyId, Integer statusId, LocalDate validityPeriod, String strategyName,
			LocalDate timeFrameStart, LocalDate timeFrameEnd, Integer environmentId, String environmentName) {
		Status status = new Status(statusId, validityPeriod);
		Environment environment = new Environment(environmentId, environmentName);
		Strategy strategy = new Strategy(strategyId, status, strategyName, timeFrameStart, timeFrameEnd, environment);
		Strategy updatedStrategy = strategyDAL.save(strategy);
		return updatedStrategy;
	}

	@Override
	public void delete(Integer id) {
		strategyDAL.deleteById(id);
	}

	@Override
	public boolean existsById(Integer id) {
		boolean result = strategyDAL.existsById(id);
		return result;
	}

	@Override
	public boolean existsByStrategyName(String strategyName) {
		boolean result = strategyDAL.findByStrategyName(strategyName).isEmpty();
		return result;
	}

}
