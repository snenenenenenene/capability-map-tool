package com.bavostepbros.leap.domain.model.dto;

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
	private CapabilityDto capability;
	private StrategyItemDto strategyItem;
	private StrategicImportance strategicImportance;
	
	public CapabilityItemDto(StrategyItemDto strategyItem, StrategicImportance strategicImportance) {
		this.strategyItem = strategyItem;
		this.strategicImportance = strategicImportance;
	}	
	
}
