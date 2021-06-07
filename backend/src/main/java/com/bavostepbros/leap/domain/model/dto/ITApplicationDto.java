package com.bavostepbros.leap.domain.model.dto;

import java.time.LocalDate;
import java.util.List;

import com.bavostepbros.leap.domain.model.timevalue.TimeValue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ITApplicationDto {
	private Integer itApplicationId;
	private StatusDto status;
	private String name;
	private String version;
	private LocalDate purchaseDate;
	private LocalDate endOfLife;
	private Integer currentScalability;
	private Integer expectedScalability;
	private Integer currentPerformance;
	private Integer expectedPerformance;
	private Integer currentSecurityLevel;
	private Integer expectedSecurityLevel;
	private Integer currentStability;
	private Integer expectedStability;
	private String currencyType;
	private Double costCurrency;
	private Integer currentValue;
	private Double currentYearlyCost;
	private Double acceptedYearlyCost;
	private TimeValue timeValue;
	private List<TechnologyDto> technologies;
	
	public ITApplicationDto(Integer itApplicationId, StatusDto status, String name, String version, LocalDate purchaseDate,
			LocalDate endOfLife, Integer currentScalability, Integer expectedScalability, Integer currentPerformance,
			Integer expectedPerformance, Integer currentSecurityLevel, Integer expectedSecurityLevel,
			Integer currentStability, Integer expectedStability, String currencyType, Double costCurrency,
			Integer currentValue, Double currentYearlyCost, Double acceptedYearlyCost, TimeValue timeValue) {
		this.itApplicationId = itApplicationId;
		this.status = status;
		this.name = name;
		this.version = version;
		this.purchaseDate = purchaseDate;
		this.endOfLife = endOfLife;
		this.currentScalability = currentScalability;
		this.expectedScalability = expectedScalability;
		this.currentPerformance = currentPerformance;
		this.expectedPerformance = expectedPerformance;
		this.currentSecurityLevel = currentSecurityLevel;
		this.expectedSecurityLevel = expectedSecurityLevel;
		this.currentStability = currentStability;
		this.expectedStability = expectedStability;
		this.currencyType = currencyType;
		this.costCurrency = costCurrency;
		this.currentValue = currentValue;
		this.currentYearlyCost = currentYearlyCost;
		this.acceptedYearlyCost = acceptedYearlyCost;
		this.timeValue = timeValue;
	}
	
}
