package com.bavostepbros.leap.domain.model.dto;

import com.bavostepbros.leap.domain.model.capabilitylevel.CapabilityLevel;
import com.bavostepbros.leap.domain.model.paceofchange.PaceOfChange;
import com.bavostepbros.leap.domain.model.targetoperatingmodel.TargetOperatingModel;

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
	private String capabilityDescription;
	private CapabilityLevel level;
	private PaceOfChange paceOfChange;
	private TargetOperatingModel targetOperatingModel;
	private Integer resourceQuality;
	private Double informationQuality;
	private Double applicationFit;
	
}
