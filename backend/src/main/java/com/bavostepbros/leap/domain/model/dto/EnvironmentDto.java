package com.bavostepbros.leap.domain.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnvironmentDto {
	private Integer environmentId;
	private String environmentName;
	private List<CapabilityDto> capabilities;
	
	public EnvironmentDto(Integer environmentId, String environmentName) {
		this.environmentId = environmentId;
		this.environmentName = environmentName;
	}
		
}
