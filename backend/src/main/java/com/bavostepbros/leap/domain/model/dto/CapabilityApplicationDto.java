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
	private Integer importance;
	private Integer efficiencySupport;
	private Integer functionalCoverage;
	private Integer correctnessBusinessFit;
	private Integer futurePotential;
	private Integer completeness;
	private Integer correctnessInformationFit;
	private Integer availability;
}
