package com.bavostepbros.leap.domain.model.dto;

import java.util.List;

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
	private List<ProjectDto> projects;
	
	public CapabilityDto(Integer capabilityId, EnvironmentDto environment, StatusDto status, Integer parentCapabilityId,
			String capabilityName, CapabilityLevel level, boolean paceOfChange, String targetOperatingModel,
			Integer resourceQuality, Integer informationQuality, Integer applicationFit) {
		this.capabilityId = capabilityId;
		this.environment = environment;
		this.status = status;
		this.parentCapabilityId = parentCapabilityId;
		this.capabilityName = capabilityName;
		this.level = level;
		this.paceOfChange = paceOfChange;
		this.targetOperatingModel = targetOperatingModel;
		this.resourceQuality = resourceQuality;
		this.informationQuality = informationQuality;
		this.applicationFit = applicationFit;
	}	
	
}
