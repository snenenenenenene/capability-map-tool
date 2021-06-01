package com.bavostepbros.leap.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StrategyItemDto {
	private Integer itemId;
	private StrategyDto strategy;
	private String strategyItemName;
	private String description;
	
	public StrategyItemDto(Integer itemId, String strategyItemName, String description) {
		this.itemId = itemId;
		this.strategyItemName = strategyItemName;
		this.description = description;
	}
	
}
