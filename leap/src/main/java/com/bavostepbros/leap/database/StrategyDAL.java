package com.bavostepbros.leap.database;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bavostepbros.leap.model.Strategy;

public interface StrategyDAL extends JpaRepository<Strategy, Integer> {

}
