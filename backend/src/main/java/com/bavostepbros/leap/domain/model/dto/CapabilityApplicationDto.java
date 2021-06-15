package com.bavostepbros.leap.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/** 
 * @return CapabilityDto
 */

/** 
 * @return ITApplicationDto
 */

/** 
 * @return Integer
 */

/** 
 * @return Integer
 */

/** 
 * @return Integer
 */

/** 
 * @return Integer
 */

/** 
 * @return Integer
 */

/** 
 * @return Integer
 */

/** 
 * @return Integer
 */

/** 
 * @return Integer
 */
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
	
	public CapabilityApplicationDto(ITApplicationDto application, Integer importance, Integer efficiencySupport,
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
