package com.bavostepbros.leap.domain.model.dto;

import com.bavostepbros.leap.domain.model.capabilitylevel.CapabilityLevel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CapabilityDto {
	private Integer capabilityId;
	private EnvironmentDto environment;
	private StatusDto status;
	private Integer parentCapabilityId;
	private String capabilityName;
	private CapabilityLevel level;
	private boolean paceOfChange;
	private String targetOperatingModel;
	private Integer resourceQuality;
	private Integer informationQuality;
	private Integer applicationFit;	
	
}
