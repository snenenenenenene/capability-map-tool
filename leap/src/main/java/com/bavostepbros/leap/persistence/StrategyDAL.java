package com.bavostepbros.leap.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bavostepbros.leap.domain.model.Strategy;

public interface StrategyDAL extends JpaRepository<Strategy, Integer> {

}
