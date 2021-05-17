package com.bavostepbros.leap.domain.service.strategyservice;

import com.bavostepbros.leap.domain.model.Strategy;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Lenny Bontenakel, Bavo Van Meel
 *
 */
public interface StrategyService {
	Strategy save(Integer statusId, String strategyName, LocalDate timeFrameStart, LocalDate timeFrameEnd,
			Integer environmentId);

	Strategy get(Integer id);

	List<Strategy> getAll();

	Strategy update(Integer strategyId, Integer statusId, String strategyName, LocalDate timeFrameStart,
			LocalDate timeFrameEnd, Integer environmentId);

	void delete(Integer id);

	boolean existsById(Integer id);

	boolean existsByStrategyName(String strategyName);

	List<Strategy> getStrategiesByEnvironment(Integer environmentId);
}
