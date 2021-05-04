package com.bavostepbros.leap.domain.service.StrategyService;

import com.bavostepbros.leap.domain.model.Strategy;

import java.time.LocalDate;
import java.util.List;

public interface StrategyService {
    Strategy save(Integer statusId, LocalDate validityPeriod, String strategyName, 
    		LocalDate timeFrameStart, LocalDate timeFrameEnd);
    Strategy get(Integer id);
    List<Strategy> getAll();
    void update(Strategy strategy);
    void delete(Integer id);
    boolean existsById(Integer id);
	boolean existsByStrategyName(String strategyName);
}
