package com.bavostepbros.leap.domain.model.dto;

import com.bavostepbros.leap.domain.model.Capability;
import com.bavostepbros.leap.domain.model.StrategyItem;
import com.bavostepbros.leap.domain.model.strategicimportance.StrategicImportance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CapabilityItemDto {
	private Capability capability;
	private StrategyItem strategyItem;
	private StrategicImportance strategicImportance;
}
