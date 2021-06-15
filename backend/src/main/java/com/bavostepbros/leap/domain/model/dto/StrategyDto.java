package com.bavostepbros.leap.domain.model.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/** 
 * @return Integer
 */

/** 
 * @return StatusDto
 */

/** 
 * @return String
 */

/** 
 * @return LocalDate
 */

/** 
 * @return LocalDate
 */

/** 
 * @return EnvironmentDto
 */

/** 
 * @return List<StrategyItemDto>
 */
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
	private List<StrategyItemDto> strategyItems;
	
	public StrategyDto(Integer strategyId, StatusDto status, String strategyName, LocalDate timeFrameStart,
			LocalDate timeFrameEnd, EnvironmentDto environment) {
		this.strategyId = strategyId;
		this.status = status;
		this.strategyName = strategyName;
		this.timeFrameStart = timeFrameStart;
		this.timeFrameEnd = timeFrameEnd;
		this.environment = environment;
	}	
	
}
