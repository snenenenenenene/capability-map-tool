package com.bavostepbros.leap.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Strategy;

public interface StrategyDAL extends JpaRepository<Strategy, Integer> {
	Optional<Strategy> findByStrategyName(String strategyName);
	List<Strategy> findByEnvironment(Environment environment);
}
