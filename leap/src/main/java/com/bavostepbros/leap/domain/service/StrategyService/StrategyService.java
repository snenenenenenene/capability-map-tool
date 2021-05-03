package com.bavostepbros.leap.domain.service.StrategyService;

import com.bavostepbros.leap.domain.model.Strategy;

import java.util.List;

public interface StrategyService {
    boolean save(Strategy strategy);
    Strategy get(Integer id);
    List<Strategy> getAll();
    void update(Strategy strategy);
    void delete(Integer id);
    boolean existsById(Integer id);
	boolean existsByStrategyName(String strategyName);
}
