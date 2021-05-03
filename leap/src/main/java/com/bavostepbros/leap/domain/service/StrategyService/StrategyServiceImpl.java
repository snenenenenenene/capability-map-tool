package com.bavostepbros.leap.domain.service.StrategyService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

import com.bavostepbros.leap.domain.model.Strategy;
import com.bavostepbros.leap.persistence.StrategyDAL;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class StrategyServiceImpl implements StrategyService {

	@Autowired
    private StrategyDAL strategyDAL;

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

	@Override
	public boolean existsById(Integer id) {
		boolean result = strategyDAL.existsById(id);
		return result;
	}

	@Override
	public boolean existsByStrategyName(String strategyName) {
		boolean result = strategyDAL.findByStrategyName(strategyName).isEmpty();
		return result;
	}

}
