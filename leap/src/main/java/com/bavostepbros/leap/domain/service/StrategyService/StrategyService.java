package com.bavostepbros.leap.domain.service.StrategyService;

import com.bavostepbros.leap.domain.model.Strategy;

import java.time.LocalDate;
import java.util.List;

public interface StrategyService {
	Strategy save(Integer statusId, LocalDate validityPeriod, String strategyName, LocalDate timeFrameStart,
			LocalDate timeFrameEnd, Integer environmentId, String environmentName);

	Strategy get(Integer id);

	List<Strategy> getAll();

	Strategy update(Integer strategyId, Integer statusId, LocalDate validityPeriod, String strategyName,
			LocalDate timeFrameStart, LocalDate timeFrameEnd, Integer environmentId, String environmentName);

	void delete(Integer id);

	boolean existsById(Integer id);

	boolean existsByStrategyName(String strategyName);
}
