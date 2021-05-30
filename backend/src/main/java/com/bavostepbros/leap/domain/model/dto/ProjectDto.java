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
public class ProjectDto {
	private Integer projectId;
	private String projectName;
	private ProgramDto program;
	private StatusDto status;
	private List<CapabilityDto> capabilities;
	
	public ProjectDto(Integer projectId, String projectName, ProgramDto program, StatusDto status) {
		this.projectId = projectId;
		this.projectName = projectName;
		this.program = program;
		this.status = status;
	}
	
}
