package com.bavostepbros.leap.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.CapabilityItem;
import com.bavostepbros.leap.domain.model.CapabilityItemId;
import com.bavostepbros.leap.domain.model.StrategyItem;

public interface CapabilityItemDAL extends JpaRepository<CapabilityItem, CapabilityItemId> {
	Optional<CapabilityItem> findByCapabilityAndStrategyItem(Capability capability, StrategyItem strategyItem);

	List<CapabilityItem> findByStrategyItem(StrategyItem strategyItem);

	void deleteByCapabilityAndStrategyItem(Capability capability, StrategyItem strategyItem);
	
	List<CapabilityItem> findByCapability(Capability capability);
}
