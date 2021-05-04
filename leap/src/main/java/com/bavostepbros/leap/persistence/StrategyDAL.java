package com.bavostepbros.leap.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Strategy;

public interface StrategyDAL extends JpaRepository<Strategy, Integer> {
	List<Strategy> findByStrategyName(String strategyName);
	List<Strategy> findByEnvironment(Environment environment);
}
