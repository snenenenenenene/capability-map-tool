package com.bavostepbros.leap.domain.model.dto;

import com.bavostepbros.leap.domain.model.strategicimportance.StrategicImportance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/** 
 * @return CapabilityDto
 */

/** 
 * @return InformationDto
 */

/** 
 * @return StrategicImportance
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CapabilityInformationDto {
	private CapabilityDto capability;
	private InformationDto information;
	private StrategicImportance criticality;
	
	public CapabilityInformationDto(InformationDto information, StrategicImportance criticality) {
		this.information = information;
		this.criticality = criticality;
	}
	
}
