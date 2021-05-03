package com.bavostepbros.leap.domain.service.StrategyService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

import com.bavostepbros.leap.domain.model.Strategy;
import com.bavostepbros.leap.persistance.StrategyDAL;

@Service
@Transactional
public class StrategyServiceImpl implements StrategyService {

    private final StrategyDAL strategyDAL;

    @Autowired
    public StrategyServiceImpl(StrategyDAL strategyDAL) {
        super();
        this.strategyDAL = strategyDAL;
    }

    @Override
    public boolean save(Strategy strategy) {
        List<Strategy> strategies = getAll();
        List<Strategy> results = strategies.stream()
                .filter(strat -> strategy.getStrategyName().equals(strat.getStrategyName()))
                .collect(Collectors.toList());
        if (results.size() > 0) {
            // TODO add number to duplicate.
            return false;
        } else {
            strategyDAL.save(strategy);
            return true;
        }
    }

    @Override
    public Strategy get(Integer id) {
        Strategy strategy = strategyDAL.findById(id).get();
        return strategy;
    }

    @Override
    public List<Strategy> getAll() {
        List<Strategy> strategies = strategyDAL.findAll();
        return strategies;
    }

    @Override
    public void update(Strategy strategy) {
        strategyDAL.save(strategy);
    }

    @Override
    public void delete(Integer id) {
        strategyDAL.deleteById(id);
    }

}
