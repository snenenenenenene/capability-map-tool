package com.bavostepbros.leap.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CapabilityApplicationDto {
	private CapabilityDto capability;
	private ITApplicationDto application;
	private Double importance;
	private Integer efficiencySupport;
	private Integer functionalCoverage;
	private Integer correctnessBusinessFit;
	private Integer futurePotential;
	private Integer completeness;
	private Integer correctnessInformationFit;
	private Integer availability;
	
	public CapabilityApplicationDto(ITApplicationDto application, Double importance, Integer efficiencySupport,
			Integer functionalCoverage, Integer correctnessBusinessFit, Integer futurePotential, Integer completeness,
			Integer correctnessInformationFit, Integer availability) {
		this.application = application;
		this.importance = importance;
		this.efficiencySupport = efficiencySupport;
		this.functionalCoverage = functionalCoverage;
		this.correctnessBusinessFit = correctnessBusinessFit;
		this.futurePotential = futurePotential;
		this.completeness = completeness;
		this.correctnessInformationFit = correctnessInformationFit;
		this.availability = availability;
	}
	
}
