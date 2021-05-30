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
public class ProgramDto {
	private Integer programId;
	private String programName;
	private List<ProjectDto> projects;
	
	public ProgramDto(Integer programId, String programName) {
		this.programId = programId;
		this.programName = programName;
	}
		
}
