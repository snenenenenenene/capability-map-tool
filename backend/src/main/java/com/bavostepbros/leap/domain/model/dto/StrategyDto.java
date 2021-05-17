package com.bavostepbros.leap.domain.model.dto;

import java.time.LocalDate;

import com.bavostepbros.leap.domain.model.Environment;
import com.bavostepbros.leap.domain.model.Status;

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
	private Status status;
	private String strategyName;
	private LocalDate timeFrameStart;
	private LocalDate timeFrameEnd;
	private Environment environment;
}
