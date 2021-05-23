package com.bavostepbros.leap.domain.model.dto;

import java.time.LocalDate;
import java.util.List;

import com.bavostepbros.leap.domain.model.Status;
import com.bavostepbros.leap.domain.model.Technology;

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
	private Status status;
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
	private String costCurrency;
	private String currentValue;
	private Double currentYearlyCost;
	private LocalDate timeValue;
	private List<Technology> technologies;
}
