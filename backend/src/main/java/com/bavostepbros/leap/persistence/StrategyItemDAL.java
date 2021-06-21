package com.bavostepbros.leap.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bavostepbros.leap.domain.model.Strategy;
import com.bavostepbros.leap.domain.model.StrategyItem;

public interface StrategyItemDAL extends JpaRepository<StrategyItem, Integer> {
	Optional<StrategyItem> findByStrategyItemName(String strategyItemName);

	List<StrategyItem> findByStrategy(Strategy strategy);
}
