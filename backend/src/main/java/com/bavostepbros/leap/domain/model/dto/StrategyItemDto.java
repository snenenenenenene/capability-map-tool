package com.bavostepbros.leap.domain.model.dto;

import com.bavostepbros.leap.domain.model.Strategy;

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
	private Strategy strategy;
	private String strategyItemName;
	private String description;
}
