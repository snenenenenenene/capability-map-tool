package com.bavostepbros.leap.database;

import com.bavostepbros.leap.model.Strategy;
import java.util.List;

public interface StrategyService {
    boolean save(Strategy strategy);
    Strategy get(Integer id);
    List<Strategy> getAll();
    void update(Strategy strategy);
    void delete(Integer id);
}
