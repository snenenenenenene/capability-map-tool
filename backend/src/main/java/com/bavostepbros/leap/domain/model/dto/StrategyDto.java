package com.bavostepbros.leap.domain.model.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StrategyDto {
	private Integer strategyId;
	private StatusDto status;
	private String strategyName;
	private LocalDate timeFrameStart;
	private LocalDate timeFrameEnd;
	private EnvironmentDto environment;
}
